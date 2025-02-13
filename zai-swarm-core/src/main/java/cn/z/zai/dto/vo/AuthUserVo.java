package cn.z.zai.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @Description:
 */
@ApiModel("Auth Result Vo")
@Data
public class AuthUserVo extends BaseTgUserVo {

    @ApiModelProperty(name = "auth token" ,required = true)
    private String token;


    private BigInteger superiorsTgUserId;

}
