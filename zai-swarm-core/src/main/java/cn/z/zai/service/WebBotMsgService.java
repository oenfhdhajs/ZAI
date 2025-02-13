package cn.z.zai.service;

import cn.z.zai.dto.Response;
import cn.z.zai.dto.vo.WebBotTransaction;

public interface WebBotMsgService {

    Response<?> updateZAiLineDetailTSStatus(WebBotTransaction webBotTransaction);
}
