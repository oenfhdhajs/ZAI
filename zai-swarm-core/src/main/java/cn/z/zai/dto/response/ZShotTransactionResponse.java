package cn.z.zai.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZShotTransactionResponse<T> implements Serializable {

    private Integer code;

    private T result;

    private String message;
}
