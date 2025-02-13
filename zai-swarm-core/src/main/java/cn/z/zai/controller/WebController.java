package cn.z.zai.controller;

import cn.z.zai.config.ratelimiter.AccessInterceptor;
import cn.z.zai.dto.Response;
import cn.z.zai.dto.vo.WebBotTransaction;
import cn.z.zai.service.WebBotMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/web")
public class WebController {


    @Autowired
    private WebBotMsgService webBotMsgService;


    /**
     *
     *
     * @return
     */
    @AccessInterceptor(key = "user", permitsPerSecond = 2)
    @PostMapping("/webBot/msg/status")
    public Response<?> webBotMsgUpdate(@RequestBody WebBotTransaction check) {

        return webBotMsgService.updateZAiLineDetailTSStatus(check);

    }

}
