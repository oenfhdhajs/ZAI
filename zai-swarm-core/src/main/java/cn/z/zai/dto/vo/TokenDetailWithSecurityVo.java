package cn.z.zai.dto.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TokenDetailWithSecurityVo extends TokenDetailVo{

    private Integer alarm;
}
