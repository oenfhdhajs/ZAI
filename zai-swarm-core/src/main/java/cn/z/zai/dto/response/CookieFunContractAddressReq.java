package cn.z.zai.dto.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class CookieFunContractAddressReq implements Serializable {
    private static final long serialVersionUID = 2562861668875064194L;
    private String interval = "_7Days";
}
