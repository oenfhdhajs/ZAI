package cn.z.zai.dto.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @Description:
 */
@Data
public class AuthSo {

    @ApiParam(name = "init data" ,required = true ,type = "string")
    private String initData;


    private String query;

    private Integer source;

    private String address;

    private String signature;
}
