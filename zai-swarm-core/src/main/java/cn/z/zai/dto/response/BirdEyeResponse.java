package cn.z.zai.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class BirdEyeResponse<T> implements Serializable {

    private Boolean success;

    private T data;

    private String message;

    private Integer statusCode;
}
