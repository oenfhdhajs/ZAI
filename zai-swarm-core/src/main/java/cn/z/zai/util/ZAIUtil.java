package cn.z.zai.util;

import cn.hutool.core.collection.CollUtil;
import cn.z.zai.common.enums.ChatActionEnum;
import cn.z.zai.common.enums.ZAIPlatformEnum;
import cn.z.zai.common.enums.ZAITypeEnum;
import cn.z.zai.dto.entity.ZAiLineDetail;
import cn.z.zai.dto.request.chat.ChatCompletionChoice;
import cn.z.zai.dto.request.chat.ChatCompletionResponse;
import cn.z.zai.dto.request.chat.ChatMessage;
import cn.z.zai.dto.request.chat.ChatMessageRole;
import cn.z.zai.dto.request.chat.StreamChatMessage;
import cn.z.zai.dto.request.chat.ZAIBaseChatContent;
import cn.z.zai.dto.request.chat.ZAIChatRequest;
import cn.z.zai.dto.request.chat.ZAITextChatContent;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.sol4k.Base58;
import org.sol4k.PublicKey;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
public class ZAIUtil {

    public static ZAiLineDetail packageGptMessageContent(List<ChatCompletionResponse> responseList, String messageId, List<ZAIBaseChatContent> chatMessage) {
        String content = packageGptMessageContent(responseList);
        ZAiLineDetail build = new ZAiLineDetail();
        build.setMessageId(messageId);
        build.setType(ZAITypeEnum.answer.getType());
        build.setContent(content);
        build.setRole(ChatMessageRole.ASSISTANT.value());
        build.setPlatform(ZAIPlatformEnum.CHAT_GPT.getType());
        build.setChatContent(chatMessage);
        if (chatMessage == null) {
            ZAITextChatContent chatContent = ZAITextChatContent.builder().text(build.getContent()).build();
            chatContent.setAction(ChatActionEnum.TEXT.getAction());
            build.setChatContent(Collections.singletonList(chatContent));
        }
        return build;
    }

    public static String packageGptMessageContent(List<ChatCompletionResponse> responseList) {
        String streamContent =
                responseList.stream()
                        .flatMap(chunk -> chunk.getChoices().stream())
                        .map(ChatCompletionChoice::getDelta)
                        .filter(Objects::nonNull)
                        .map(StreamChatMessage::getContent)
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining());
        String content =
                responseList.stream()
                        .flatMap(chunk -> chunk.getChoices().stream())
                        .map(ChatCompletionChoice::getMessage)
                        .filter(Objects::nonNull)
                        .map(ChatMessage::getContent)
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining());
        return streamContent + content;
    }

    public static List<ChatMessage> buildByZAILineDetail(List<ZAiLineDetail> list) {
        if (CollUtil.isNotEmpty(list)) {
            List<ChatMessage> chatList = Lists.newArrayList();
            for (ZAiLineDetail zAiLineDetail : list) {
                ChatMessage build = ChatMessage.builder().role(zAiLineDetail.getRole()).content(zAiLineDetail.getContent()).build();
                chatList.add(build);
            }
            return chatList;
        }
        return Collections.emptyList();
    }

    public static ZAiLineDetail buildZAILineDetailByAIChatRequest(ZAIChatRequest request) {
        ZAiLineDetail build = new ZAiLineDetail();
        build.setTgUserId(request.getTgUserId());
        ZAITextChatContent content = ZAITextChatContent.builder().text(request.getContent()).build();
        content.setAction(ChatActionEnum.TEXT.getAction());
        build.setChatContent(Collections.singletonList(content));
        build.setContent(request.getContent());
        build.setMessageId(request.getMessageId());
        build.setPlatform(ZAIPlatformEnum.CHAT_GPT.getType());
        build.setRole(ChatMessageRole.USER.value());
        build.setType(ZAITypeEnum.question.getType());
        return build;
    }


    //========Web Auth========

    public static final String VERIFY_PREFIX = "By signing, you agree to ZAI's Terms of Use and Privacy Policy. Your authentication nonce is:%s";

    public static Boolean verifySignature(String signature, String walletAddress, String uId) {
        log.info("web auth param: sign={} ,addr={} ,uId={}", signature, walletAddress, uId);
        String message = String.format(VERIFY_PREFIX, uId);
        byte[] messageBytes = message.getBytes();
        PublicKey publicKey = new PublicKey(walletAddress);
        byte[] signatureBytes = Base58.decode(signature);
        return publicKey.verify(signatureBytes, messageBytes);
    }


}
