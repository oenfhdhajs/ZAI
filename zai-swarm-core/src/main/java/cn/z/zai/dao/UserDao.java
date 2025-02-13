package cn.z.zai.dao;

import cn.z.zai.dto.entity.User;
import cn.z.zai.dto.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Mapper
@Repository
public interface UserDao {

    int insert(User user);

    int updateUser(User user);

    BigInteger getTgUserId(@Param("tgUserId") BigInteger tgUserId);

    UserVo getUserByTgUserId(BigInteger tgUserId);


}
