package cn.z.zai.dao;

import cn.z.zai.dto.vo.TokenDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TokenDetailDao {


    TokenDetailVo queryByAddress(@Param("address") String address);


    void addTokenDetail(TokenDetailVo vo);

    void updateTokenDetail(TokenDetailVo vo);


}
