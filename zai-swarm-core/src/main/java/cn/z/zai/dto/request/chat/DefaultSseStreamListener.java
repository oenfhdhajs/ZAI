package cn.z.zai.dto.request.chat;

import cn.hutool.core.util.StrUtil;
import cn.z.zai.common.enums.ChatActionEnum;
import cn.z.zai.dto.entity.ZAiLineDetail;
import cn.z.zai.service.AiBusinessService;
import cn.z.zai.util.BaseSseEmitterServerUtil;
import cn.z.zai.util.JsonUtil;
import cn.z.zai.util.ZAIUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;


@Slf4j
@Data
@AllArgsConstructor
public class DefaultSseStreamListener extends EventSourceListener {

    private String connId;

    private AiBusinessService aiBusinessService;

    private JsonUtil jsonUtil;

    private List<ChatCompletionResponse> chunks = Lists.newArrayList();

    private boolean isAnswerText = true;

    private boolean isFirstParse = true;

    private boolean isFirstEmptyCharacter = Boolean.TRUE;

    private List<ZAIBaseChatContent>  chatContent = null;

    private BigInteger tgUserId;

    private String oneQuestId;

    public DefaultSseStreamListener(String connId, AiBusinessService aiBusinessService, JsonUtil jsonUtil, ZAIChatRequest request) {
        this.connId = connId;
        this.aiBusinessService = aiBusinessService;
        this.jsonUtil = jsonUtil;
        this.tgUserId = request.getTgUserId();
        this.oneQuestId = request.getOneQuestId();
    }


    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if (data.equals("[DONE]")) {
            if (!isAnswerText) {
                String json = ZAIUtil.packageGptMessageContent(chunks);
                log.info("userId is {}, connId is {},oneQuestId is {} >>>{}",tgUserId, connId,  oneQuestId,json);
                chatContent = aiBusinessService.coreBusiness(json, connId, tgUserId, oneQuestId);
            }

            ZAIBaseChatContent zaiBaseChatContent = new ZAIBaseChatContent();
            zaiBaseChatContent.setAction(ChatActionEnum.STOP.getAction());
            zaiBaseChatContent.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId,jsonUtil.obj2String(zaiBaseChatContent));
            BaseSseEmitterServerUtil.close(connId);
            return;
        }
        ChatCompletionResponse response = jsonUtil.string2Obj(data, ChatCompletionResponse.class);
        chunks.add(response);
        if (isFirstParse){
            String content = ZAIUtil.packageGptMessageContent(chunks);
            if (StrUtil.isNotEmpty(content) && content.startsWith("{")){
                isAnswerText = false;
            }
            if (StrUtil.isNotEmpty(content)){
                isFirstParse = false;
            }
        }
        if (isAnswerText) {

            String content = ZAIUtil.packageGptMessageContent(Lists.newArrayList(response));
            if (isFirstEmptyCharacter && StringUtils.isEmpty(content)) {
                return;
            }
            isFirstEmptyCharacter = Boolean.FALSE;
            ZAITextChatContent build = ZAITextChatContent.builder().text(content).build();
            build.setAction(ChatActionEnum.TEXT.getAction());
            build.setOneQuestId(oneQuestId);
            BaseSseEmitterServerUtil.sendMessage(connId, jsonUtil.obj2String(build));
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        ZAiLineDetail zAiLineDetail = ZAIUtil.packageGptMessageContent(chunks, connId,chatContent);
        aiBusinessService.asyncSaveChatDetail(connId,zAiLineDetail,oneQuestId);
        eventSource.cancel();
    }

    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        try {
            StringBuilder sb = new StringBuilder("chet gpt error");
            if (Objects.nonNull(response)) {
                String responseText = response.body().string();
                sb.append(responseText);
            }
            log.error("ERROR={},Throwable={}",sb,t == null ? "" :t.getMessage());
        } catch (Exception e) {
            log.warn("onFailure error:{}", e);
        } finally {
            eventSource.cancel();
        }
    }
}
