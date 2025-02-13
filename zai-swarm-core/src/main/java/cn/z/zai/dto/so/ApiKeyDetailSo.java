package cn.z.zai.dto.so;

import lombok.Data;


@Data
public class ApiKeyDetailSo {

    private Integer type;

    private String url;

    private String apiKey;

    private Integer weight;

    private String ext;
}
