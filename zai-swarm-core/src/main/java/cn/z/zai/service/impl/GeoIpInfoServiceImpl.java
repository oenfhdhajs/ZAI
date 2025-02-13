package cn.z.zai.service.impl;

import cn.z.zai.dao.GeoIpInfoDao;
import cn.z.zai.dto.entity.GeoIpInfo;
import cn.z.zai.service.GeoIpInfoService;
import cn.z.zai.util.ContextHolder;
import cn.z.zai.util.IFileUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@Slf4j
@Service
public class GeoIpInfoServiceImpl implements GeoIpInfoService {

    //, "United States"
    private static final List<String> DENIED_COUNTRY = Arrays.asList("China");

    private static final String GEOIP_DB_PATH = "/geo/GeoLite2-City.mmdb";

    @Autowired
    private GeoIpInfoDao geoIpInfoDao;

    @Autowired
    private IFileUtils iFileUtils;

    @Autowired
    @Qualifier("asyncServiceExecutor")
    private Executor executors;


    private DatabaseReader dbReader;


    @PostConstruct
    public void init() throws Exception {

        String imageString = iFileUtils.getImageString(GEOIP_DB_PATH);
       /* ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:" + GEOIP_DB_PATH);
        File file = resource.getFile();*/
        File database = new File(imageString);
        dbReader = new DatabaseReader.Builder(database).build();
    }

    @Override
    @Synchronized
    public void addNotExist(GeoIpInfo geoIpInfo) {

        List<GeoIpInfo> geoIpInfos = geoIpInfoDao.selectInfoList(geoIpInfo);
        if (CollectionUtils.isEmpty(geoIpInfos)) {
            geoIpInfoDao.addInfo(geoIpInfo);
        }
    }

    public Boolean needAccessDenied() {
        try {
            String ip = ContextHolder.getIp();
            InetAddress addr = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(addr);

            Country country = response.getCountry();
            City city = response.getCity();
            // log.info("ip-> {}, Country-> {},  City-{}", ip, country.getName(), city.getName());
            for (String s : DENIED_COUNTRY) {
                if (StringUtils.equalsIgnoreCase(s, country.getName())) {
                    CompletableFuture.runAsync(() -> {
                        GeoIpInfo geoIpInfo = new GeoIpInfo();
                        geoIpInfo.setCity(city.getName());
                        geoIpInfo.setCountry(country.getName());
                        geoIpInfo.setIp(ip);
                        this.addNotExist(geoIpInfo);
                    }, executors);
                    return Boolean.TRUE;
                }
            }
        } catch (UnknownHostException e) {
            log.warn("GeoIp UnknownHostException ->{}", e.getMessage());
        } catch (AddressNotFoundException e) {
            log.warn("GeoIp AddressNotFoundException ->{}", e.getMessage());
        } catch (IOException e) {
            log.error("GeoIp IOException ->-> ", e);
        } catch (GeoIp2Exception e) {
            log.error("GeoIp GeoIp2Exception ->->", e);
        }

        return Boolean.FALSE;
    }
}
