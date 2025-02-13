package cn.z.zai.dto.request.chat;

import java.io.Serializable;
import java.util.Map;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenHttpRequest implements Serializable {
    private static final long serialVersionUID = 283304771767500312L;

    private String authorization;

    /**
     *
     */
    private String url;

    /**
     *
     */
    private Map<String,String> headers;

    /**
     *
     */
    private Object params;

    /**
     *
     */
    private Object bodyParams;
}
