package cn.z.zai.dto.vo.kafka;

import java.io.Serializable;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class KafkaSendMsgVo implements Serializable {

    private static final long serialVersionUID = 4608605768164734204L;

    private String topic;

    private String messageData;
}
