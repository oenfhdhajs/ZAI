package cn.z.zai.dto.vo;

import cn.z.zai.common.enums.KafkaUserMsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class KafkaUserMsgVo implements Serializable {

    private static final long serialVersionUID = -2348112490725064627L;
    /**
     * @see KafkaUserMsgTypeEnum
     */
    private String type;

    private String userData;
}
