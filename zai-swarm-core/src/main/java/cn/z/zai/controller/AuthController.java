package cn.z.zai.controller;

import cn.z.zai.common.constant.ErrorConstant;
import cn.z.zai.common.constant.RedisCacheConstant;
import cn.z.zai.common.constant.UserRedisConstant;
import cn.z.zai.config.encrypt.annotation.UnSignAndEncrypt;
import cn.z.zai.dto.Response;
import cn.z.zai.dto.vo.AuthSo;
import cn.z.zai.dto.vo.UserVo;
import cn.z.zai.service.UserService;
import cn.z.zai.util.InnerUUidUtils;
import cn.z.zai.util.JsonUtil;
import cn.z.zai.util.RedisUtil;
import cn.z.zai.util.SolanaAddressValidator;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(tags = "Auth Controller,begin from this")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("web verify_number method")
    @GetMapping("/verify_number")
    public Response<Map<String, String>> verifyNumber(@RequestParam("walletAddress")String walletAddress) {

        if (!SolanaAddressValidator.isValidSolanaAddress(walletAddress)) {
            log.error("[verifyNumber] validSolanaAddress false, address is {}", walletAddress);
            return Response.fail(ErrorConstant.NOT_AVAILABLE, "walletAddress format error!");
        }

        InnerUUidUtils uidUtils = new InnerUUidUtils();
        long uid = uidUtils.nextId();
        String key = String.format(RedisCacheConstant.WEB_BOT_AUTH_ADDRESS, walletAddress);
        redisUtil.setEx(key,uid+"",RedisCacheConstant.WEB_BOT_AUTH_ADDRESS_TIME_OUT,TimeUnit.SECONDS);
        HashMap<String, String> map = Maps.newHashMap();
        map.put("uId",uid+"");
        return Response.success(map);
    }

    @ApiOperation("auth method")
    @PostMapping("/me")
    @UnSignAndEncrypt
    public Response<UserVo> auth(@RequestBody @Validated AuthSo so) {
        log.info("me start {}", so);

        UserVo auth = userService.auth(so);
        if (StringUtils.isNotEmpty(auth.getAddress())) {
            Response<UserVo> success = Response.success(auth);
            log.info("me end {}", jsonUtil.obj2String(success));
            return success;
        }
        boolean first = redisUtil.setIfAbsent(String.format(UserRedisConstant.USER_ME, auth.getTgUserId()), "1", 30,
            TimeUnit.SECONDS);
        if (first) {
            Response<UserVo> success = Response.success(auth);
            log.info("me end {}", jsonUtil.obj2String(success));
            return success;
        }
        log.info("me again");
        return Response.fail(ErrorConstant.NOT_AVAILABLE, "try again!");

    }
}
