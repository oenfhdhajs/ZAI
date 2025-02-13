package cn.z.zai.dto.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ChatReq implements Serializable {
    private static final long serialVersionUID = 8644302619058542490L;

    @NotNull(message = "msgId is empty")
    private String messageId;
}
