package cn.z.zai.controller;

import cn.z.zai.config.encrypt.annotation.UnSignAndEncrypt;
import cn.z.zai.dto.Response;
import cn.z.zai.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("")
public class HealthController {


    @UnSignAndEncrypt
    @GetMapping("/health-check")
    public LocalDateTime check(){
        return LocalDateTime.now();
    }

    @UnSignAndEncrypt
    @GetMapping("/health-check2")
    public Response<?> check2(){
        log.info("health check(v0.0.2-newAWS) "+ System.currentTimeMillis());
        return Response.success(ContextHolder.getIp() + "health check(v0.0.2-newAWS) ");
    }


}
