package cn.z.zai.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class GeoIpInfo extends BaseEntity {

    private String ip;


    private String country;

    private String city;

}
