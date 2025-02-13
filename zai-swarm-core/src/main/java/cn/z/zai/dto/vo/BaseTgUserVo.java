package cn.z.zai.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @Description: TelegramUser
 */
@Data
public class BaseTgUserVo {

    @ApiModelProperty(name = "tgUserId" ,required = true)
    private BigInteger tgUserId;

    @ApiModelProperty(name = "isBot" ,required = true)
    private Boolean isBot = false;

    @ApiModelProperty(name = "firstName" ,required = true)
    private String firstName;

    @ApiModelProperty(name = "lastName" ,required = true)
    private String lastName;

    @ApiModelProperty(name = "userName" ,required = true)
    private String userName;

    @ApiModelProperty(name = "languageCode" ,required = true)
    private String languageCode;
}
