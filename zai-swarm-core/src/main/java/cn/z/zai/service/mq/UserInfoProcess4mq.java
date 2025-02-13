package cn.z.zai.service.mq;

import cn.z.zai.common.constant.RedisCacheConstant;
import cn.z.zai.common.constant.UserRedisConstant;
import cn.z.zai.common.enums.KafkaUserMsgTypeEnum;
import cn.z.zai.dao.UserDao;
import cn.z.zai.dto.entity.User;
import cn.z.zai.dto.vo.KafkaUserMsgVo;
import cn.z.zai.dto.vo.UserVo;
import cn.z.zai.service.UserMqProcessAbstract;
import cn.z.zai.util.JsonUtil;
import cn.z.zai.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Objects;


@Service
@Slf4j
public class UserInfoProcess4mq extends UserMqProcessAbstract {
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserDao clickerDao;

    @Override
    public String type() {
        return KafkaUserMsgTypeEnum.USER_INFO.getType();
    }

    @Override
    public void process(KafkaUserMsgVo kafkaUserMsgVo) {

        if (Objects.isNull(kafkaUserMsgVo) || StringUtils.isEmpty(kafkaUserMsgVo.getUserData())) {
            log.warn("UserInfoProcess4mq in message isempty");
        }

        User clicker = jsonUtil.string2Obj(kafkaUserMsgVo.getUserData(), User.class);

        String userInfoCacheKey = String.format(UserRedisConstant.USER_INFO, clicker.getTgUserId());

        String userInitCacheKey = String.format(UserRedisConstant.USER_INIT, clicker.getTgUserId());

        User userInitInfoCache = redisUtil.get(userInitCacheKey, User.class);

        UserVo userVo = redisUtil.get(userInfoCacheKey, UserVo.class);

        if (!StringUtils.isAllEmpty(clicker.getAddress(), clicker.getSalt(), clicker.getVerifyCode())) {
            if (Objects.nonNull(userVo)) {
                if (StringUtils.isNotEmpty(userVo.getAddress())) {
                    clicker.setAddress(userVo.getAddress());
                }
                if (StringUtils.isNotEmpty(userVo.getSalt())) {
                    clicker.setSalt(userVo.getSalt());
                }
                if (StringUtils.isNotEmpty(userVo.getVerifyCode())) {
                    clicker.setVerifyCode(userVo.getVerifyCode());
                }
            }
        }

        BigInteger tgUserId = clickerDao.getTgUserId(clicker.getTgUserId());
        if (Objects.isNull(tgUserId) || tgUserId.compareTo(BigInteger.ZERO) <= 0) {
            try {
                // Use userInitCache add
                clickerDao.insert(userInitInfoCache);

            } catch (DuplicateKeyException e) {
                log.warn("repeat add user ->", e);
            } catch (Exception e) {
                log.error("userInfoxxx-> {}", userInitInfoCache);
                log.error("YYYYYY  -> {}", e.getMessage());
                log.error("XXXX  ->", e);
            }
            // use this message update
            clickerDao.updateUser(clicker);
            redisUtil.setExSeconds(userInitCacheKey, userInitInfoCache, RedisCacheConstant.EXPIRE_TIME_OUT_MINUTE_1);

        } else {
            clickerDao.updateUser(clicker);
        }

    }

}
