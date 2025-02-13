package cn.z.zai.service;

import cn.z.zai.dto.request.chat.ZAIChatRequest;


public interface OpenChatGPTService {

    void asyncStreamChat(ZAIChatRequest request, String connId);
}
