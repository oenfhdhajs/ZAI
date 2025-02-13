package cn.z.zai.service;

import cn.z.zai.dto.entity.User;
import cn.z.zai.dto.vo.UserVo;

import java.math.BigInteger;

public interface UserHandleService {

    void initUser(User clicker);

    UserVo getUserInfo(BigInteger tgUserId);

    void updateUserInfo(BigInteger tgUserId, User clicker);

}
