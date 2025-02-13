package cn.z.zai.service.impl;

import cn.z.zai.common.constant.ErrorConstant;
import cn.z.zai.common.enums.ChatActionEnum;
import cn.z.zai.dao.ZAiLineDetailDao;
import cn.z.zai.dto.Response;
import cn.z.zai.dto.entity.ZAiLineDetail;
import cn.z.zai.dto.request.chat.ZAIBaseChatContent;
import cn.z.zai.dto.request.chat.ZAITransferBuyTokenContent;
import cn.z.zai.dto.request.chat.ZAITransferConfirmationContent;
import cn.z.zai.dto.vo.WebBotTransaction;
import cn.z.zai.service.WebBotMsgService;
import cn.z.zai.util.ContextHolder;
import cn.z.zai.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class WebBotMsgServiceImpl implements WebBotMsgService {

    @Autowired
    private ZAiLineDetailDao zAiLineDetailDao;
    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public Response<?> updateZAiLineDetailTSStatus(WebBotTransaction webBotTransaction) {

        String oneId = webBotTransaction.getOneQuestId();

        List<ZAiLineDetail> zAiLineDetails = zAiLineDetailDao.queryByOneQuestIdId(oneId);

        if (CollectionUtils.isEmpty(zAiLineDetails)) {
            return Response.fail(ErrorConstant.NOT_AVAILABLE, "oneId not mach!");
        }
        ZAiLineDetail zAiLineDetailInfo = zAiLineDetails.stream().filter(s -> s.getType() == 1)
                .collect(Collectors.toList()).stream().findFirst().orElse(null);

        if (Objects.isNull(zAiLineDetailInfo)) {
            return Response.fail(ErrorConstant.NOT_AVAILABLE, "oneId not mach!");
        }

        if (ContextHolder.getUserId().compareTo(zAiLineDetailInfo.getTgUserId()) != 0) {
            return Response.fail(ErrorConstant.NOT_AVAILABLE, "oneId not mach for your!");
        }

        Integer id = zAiLineDetailInfo.getId();
        String showContent = zAiLineDetailInfo.getShowContent();

        List<Object> zaiBaseChatContents = jsonUtil.string2Obj(showContent, new TypeReference<List<Object>>() {
        });
        // zAiLineDetailDao.updateTransactionStatus();

        String action = webBotTransaction.getAction();

        /**
         * BUY_TOKEN("buyToken"), SELL_TOKEN("sellToken"), TRANSFER_CONFIRMATION("transferConfirmation"),
         */

        if (!StringUtils.equalsAnyIgnoreCase(action, ChatActionEnum.BUY_TOKEN.getAction(),
                ChatActionEnum.SELL_TOKEN.getAction(), ChatActionEnum.TRANSFER_CONFIRMATION.getAction())) {
            return Response.fail(ErrorConstant.NOT_AVAILABLE, "oneId not mach for your!");
        }

        List<Object> newList = new ArrayList<>();

        for (Object zaiBaseChatContent : zaiBaseChatContents) {
            ZAIBaseChatContent temp =
                    jsonUtil.string2Obj(jsonUtil.obj2String(zaiBaseChatContent), ZAIBaseChatContent.class);
            if (StringUtils.equalsIgnoreCase(temp.getAction(), action)) {
                if (StringUtils.equalsIgnoreCase(ChatActionEnum.BUY_TOKEN.getAction(), action)) {
                    ZAITransferBuyTokenContent buy =
                            jsonUtil.string2Obj(jsonUtil.obj2String(zaiBaseChatContent), ZAITransferBuyTokenContent.class);
                    buy.setTransferStatus(webBotTransaction.getTransferStatus());
                    newList.add(buy);
                } else if (StringUtils.equalsIgnoreCase(ChatActionEnum.SELL_TOKEN.getAction(), action)) {
                    ZAITransferConfirmationContent sell = jsonUtil.string2Obj(jsonUtil.obj2String(zaiBaseChatContent),
                            ZAITransferConfirmationContent.class);
                    sell.setTransferStatus(webBotTransaction.getTransferStatus());
                    newList.add(sell);
                } else if (StringUtils.equalsIgnoreCase(ChatActionEnum.TRANSFER_CONFIRMATION.getAction(), action)) {
                    ZAITransferConfirmationContent ts = jsonUtil.string2Obj(jsonUtil.obj2String(zaiBaseChatContent),
                            ZAITransferConfirmationContent.class);
                    ts.setTransferStatus(webBotTransaction.getTransferStatus());
                    newList.add(ts);
                } else {
                    newList.add(zaiBaseChatContent);
                }

            } else {
                newList.add(zaiBaseChatContent);
            }

        }

        ZAiLineDetail zAiLineDetail = new ZAiLineDetail();
        zAiLineDetail.setId(id);
        zAiLineDetail.setShowContent(jsonUtil.obj2String(newList));

        zAiLineDetailDao.updateTransactionStatus(zAiLineDetail);

        return Response.success();
    }
}
