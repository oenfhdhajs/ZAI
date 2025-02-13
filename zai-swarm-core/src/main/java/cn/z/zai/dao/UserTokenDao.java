package cn.z.zai.dao;

import cn.z.zai.dto.entity.UserToken;
import cn.z.zai.dto.vo.UserTokenVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Mapper
@Repository
public interface UserTokenDao {


    List<UserTokenVo> getTokenList(BigInteger tgUserId);


    void deleteTokenList(BigInteger tgUserId);

    void insertTokenList(List<UserToken> tokenList);

}
