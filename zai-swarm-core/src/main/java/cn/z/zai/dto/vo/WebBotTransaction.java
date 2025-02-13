package cn.z.zai.dto.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WebBotTransaction {

    @NotNull(message = "oneQuestId is empty")
    private String oneQuestId;

    @NotNull(message = "transferStatus is empty")
    private Integer transferStatus;

    @NotNull(message = "action is empty")
    private String action;
}
