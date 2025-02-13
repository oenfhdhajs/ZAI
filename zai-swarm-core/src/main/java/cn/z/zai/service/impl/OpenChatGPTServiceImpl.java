package cn.z.zai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.z.zai.common.constant.OpenChatGptConstants;
import cn.z.zai.common.enums.ChatActionEnum;
import cn.z.zai.dto.request.chat.ChatCompletionRequest;
import cn.z.zai.dto.request.chat.ChatMessage;
import cn.z.zai.dto.request.chat.ChatMessageRole;
import cn.z.zai.dto.request.chat.DefaultSseStreamListener;
import cn.z.zai.dto.request.chat.OpenHttpRequest;
import cn.z.zai.dto.request.chat.ZAIChatRequest;
import cn.z.zai.dto.request.chat.ZAITextChatContent;
import cn.z.zai.service.AiBusinessService;
import cn.z.zai.service.OpenChatGPTService;
import cn.z.zai.util.BaseSseEmitterServerUtil;
import cn.z.zai.util.InnerUUidUtils;
import cn.z.zai.util.JsonUtil;
import cn.z.zai.util.OpenHttpUtil;
import cn.z.zai.util.ZAIUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


@Slf4j
@Service
public class OpenChatGPTServiceImpl implements OpenChatGPTService, OpenChatGptConstants {
    @Autowired
    private OpenHttpUtil openHttpUtil;

    @Value("${open.apiKey}")
    private String apiKey;

    private LinkedBlockingQueue<String> queue;
    @Autowired
    private AiBusinessService aiBusinessService;
    @Autowired
    private JsonUtil jsonUtil;

    @PostConstruct
    public void init() {
        queue = new LinkedBlockingQueue(Lists.newArrayList(apiKey.split(",")));
    }

    @Override
    @Async("asyncServiceExecutor")
    public void asyncStreamChat(ZAIChatRequest request, String connId) {
        InnerUUidUtils innerUUidUtils = new InnerUUidUtils();

        String oneQuestId = innerUUidUtils.nextStr();
        request.setOneQuestId(oneQuestId);
        log.info("webBot start request is {}", request);
        if (aiBusinessService.sessionLimitedCheck(request.getTgUserId())) {
            ZAITextChatContent build = ZAITextChatContent.builder()
                    .text("You’ve reached the maximum chat limit for today, please come back tomorrow!").build();
            build.setAction(ChatActionEnum.TEXT.getAction());
            build.setOneQuestId(request.getOneQuestId());
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(build));
            BaseSseEmitterServerUtil.close(connId);
            return;
        }

        List<ChatMessage> chatMessages = aiBusinessService.buildChatMessage(connId);
        if (CollUtil.isEmpty(chatMessages)) {
            chatMessages = new ArrayList<>();
        }
        aiBusinessService.asyncSaveChatDetail(connId, ZAIUtil.buildZAILineDetailByAIChatRequest(request), oneQuestId);

        ChatMessage chatMessage = ChatMessage.builder().role(ChatMessageRole.USER.value()).content(request.getContent()).build();
        chatMessages.add(chatMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .build();

        DefaultSseStreamListener listener = new DefaultSseStreamListener(connId, aiBusinessService, jsonUtil, request);
        defaultRequest(chatCompletionRequest);
        OpenHttpRequest build = OpenHttpRequest.builder()
                .url(CHAT_URL)
                .authorization(getAuth())
                .bodyParams(chatCompletionRequest)
                .build();
        openHttpUtil.streamPost(build, listener);
    }

    private void defaultRequest(ChatCompletionRequest request) {
        request.setStream(Boolean.TRUE);
        if (StrUtil.isEmpty(request.getModel())) {
            request.setModel(CHAT_MODEL);
        }
        if (request.getMaxTokens() == null) {
            request.setMaxTokens(CHAT_MAX_TOKEN);
        }
        if (request.getN() == null) {
            request.setN(1);
        }
        //todo system content
        if (StrUtil.isNotEmpty(CHAT_ASSISTANT)) {
            ChatMessage systemMsg = ChatMessage.builder().role(ChatMessageRole.ASSISTANT.value()).content(CHAT_ASSISTANT).build();
            request.getMessages().add(0, systemMsg);
        }
        if (StrUtil.isNotEmpty(CHAT_SYSTEM)) {
            ChatMessage systemMsg = ChatMessage.builder().role(ChatMessageRole.SYSTEM.value()).content(CHAT_SYSTEM).build();
            request.getMessages().add(0, systemMsg);
        }
        for (ChatMessage message : request.getMessages()) {
            if (StrUtil.isEmpty(message.getRole())) {
                message.setRole(ChatMessageRole.USER.value());
            }
        }
    }

    public synchronized String getAuth() {
        try {
            String auth = queue.take();
            queue.put(auth);
            return auth;
        } catch (InterruptedException e) {
            log.error("take auth ERROR:", e);
        }
        return null;
    }
}
