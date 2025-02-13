package cn.z.zai.dao;

import cn.z.zai.dto.entity.GeoIpInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GeoIpInfoDao {


    void addInfo(GeoIpInfo geoIpInfo);


    List<GeoIpInfo> selectInfoList(GeoIpInfo geoIpInfo);

}
