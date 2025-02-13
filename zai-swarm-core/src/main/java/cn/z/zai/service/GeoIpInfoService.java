package cn.z.zai.service;


import cn.z.zai.dto.entity.GeoIpInfo;

public interface GeoIpInfoService {


    void addNotExist(GeoIpInfo geoIpInfo);


    Boolean needAccessDenied();


}
