package cn.z.zai.dto.request.chat;

import cn.z.zai.dto.vo.TokenTendencyMaxVo;
import lombok.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ZAITextAndOHLCChatContent extends ZAIBaseChatContent{

    private String symbol;

    private String name;

    private String imageUrl;


    private String text;

    private List<TokenTendencyMaxVo> list;
}
