package cn.z.zai.dao;

import cn.z.zai.dto.entity.TokenSecurityDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TokenSecurityDetailDao {

    void addTokenSecurityDetail(TokenSecurityDetail tokenSecurityDetail);


    TokenSecurityDetail getTokenSecurityDetail(String tokenAddress);
}
