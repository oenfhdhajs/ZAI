package cn.z.zai.dto.request.chat;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ZAITextChatContent extends ZAIBaseChatContent{

    private  String text;
}
