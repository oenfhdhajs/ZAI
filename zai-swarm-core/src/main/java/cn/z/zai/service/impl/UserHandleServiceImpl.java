package cn.z.zai.service.impl;

import cn.z.zai.common.constant.RedisCacheConstant;
import cn.z.zai.common.constant.UserRedisConstant;
import cn.z.zai.dao.UserDao;
import cn.z.zai.dto.entity.User;
import cn.z.zai.dto.vo.UserVo;
import cn.z.zai.mq.producer.UserProducer;
import cn.z.zai.service.UserHandleService;
import cn.z.zai.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserHandleServiceImpl implements UserHandleService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserProducer userProducer;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void initUser(User clicker) {

        if (Objects.isNull(clicker) || Objects.isNull(clicker.getTgUserId())) {
            return;
        }

        String userInfoCacheKey = String.format(UserRedisConstant.USER_INFO, clicker.getTgUserId());

        String userInitCacheKey = String.format(UserRedisConstant.USER_INIT, clicker.getTgUserId());

        UserVo clickerCacheInfo = redisUtil.get(userInfoCacheKey, UserVo.class);

        if (Objects.isNull(clickerCacheInfo)) {
            BigInteger tgUserId = userDao.getTgUserId(clicker.getTgUserId());
            if (Objects.isNull(tgUserId) || tgUserId.compareTo(BigInteger.ZERO) <= 0) {
                UserVo clickerCache = new UserVo();
                BeanUtils.copyProperties(clicker, clickerCache);
                clickerCache.setLamports(0L);
                clickerCache.setInvitedFriends(0);
                clickerCache.setCommission(BigDecimal.ZERO);
                clickerCache.setDay(LocalDate.now());

                if (Objects.nonNull(clicker.getSuperiorsTgUserId())) {
                    UserVo userInfo = getUserInfo(clicker.getSuperiorsTgUserId());
                    if (Objects.nonNull(userInfo)) {
                        clickerCache.setSuperiorsAddress(userInfo.getAddress());
                    }
                }
                User user = new User();
                BeanUtils.copyProperties(clickerCache, user);
                try {
                    userDao.insert(user);
                } catch (DuplicateKeyException e) {
                    log.warn("login add user ->", e);
                }
                redisUtil.setExSeconds(userInitCacheKey, clickerCache, RedisCacheConstant.EXPIRE_TIME_OUT_DAY_3);
                redisUtil.setExSeconds(userInfoCacheKey, clickerCache, RedisCacheConstant.EXPIRE_TIME_OUT_DAY_3);
                userProducer.sendUserInfo(clicker);

                if (Objects.equals(clicker.getSource(), 1)) {

                    String webAdd = String.format(UserRedisConstant.USER_WEB_ADDRESS, clicker.getAddress());
                    redisUtil.setExSeconds(webAdd, clicker.getTgUserId(), RedisCacheConstant.EXPIRE_TIME_OUT_DAY_4);
                }

                if (Objects.nonNull(clicker.getSuperiorsTgUserId())
                        && clicker.getSuperiorsTgUserId().compareTo(BigInteger.ZERO) > 0) {
                    incInvitedFriends(clicker.getSuperiorsTgUserId());
                }

                return;
            }
            userProducer.sendUserInfo(clicker);
            return;
        }

        if (BooleanUtils.isFalse(clickerCacheInfo.getIsActive()) && BooleanUtils.isTrue(clicker.getIsActive())) {
            clickerCacheInfo.setIsActive(Boolean.TRUE);
            redisUtil.setExSeconds(userInfoCacheKey, clickerCacheInfo, RedisCacheConstant.EXPIRE_TIME_OUT_DAY_3);
            userProducer.sendUserInfo(clicker);
        }

    }

    @Override
    public UserVo getUserInfo(BigInteger tgUserId) {

        String userInfoCacheKey = String.format(UserRedisConstant.USER_INFO, tgUserId);

        UserVo clickerCacheInfo = redisUtil.get(userInfoCacheKey, UserVo.class);

        if (Objects.nonNull(clickerCacheInfo)) {
            delSetValue(clickerCacheInfo);
            return clickerCacheInfo;
        }

        UserVo clicker = userDao.getUserByTgUserId(tgUserId);
        if (Objects.isNull(clicker)) {
            return null;
        }

        redisUtil.setExSeconds(userInfoCacheKey, clicker, RedisCacheConstant.EXPIRE_TIME_OUT_DAY_3);

        if (Objects.equals(clicker.getSource(), 1)) {
            String webAdd = String.format(UserRedisConstant.USER_WEB_ADDRESS, clicker.getAddress());
            redisUtil.setExSeconds(webAdd, clicker.getTgUserId(), RedisCacheConstant.EXPIRE_TIME_OUT_DAY_4);
        }

        delSetValue(clicker);

        return clicker;
    }

    private void delSetValue(UserVo clicker) {
        if (Objects.isNull(clicker.getLamports())) {
            clicker.setLamports(0L);
        }
        if (Objects.isNull(clicker.getInvitedFriends())) {
            clicker.setInvitedFriends(0);
        }
        if (Objects.isNull(clicker.getCommission())) {

            clicker.setCommission(BigDecimal.ZERO);
        }
        if (Objects.isNull(clicker.getPointsCoin())) {
            clicker.setPointsCoin(0L);
        }
    }

    @Override
    public void updateUserInfo(BigInteger tgUserId, User clicker) {
        if (Objects.isNull(tgUserId)) {
            log.info("updateUserInfo user is empty");
            return;
        }
        String userInfoCacheKey = String.format(UserRedisConstant.USER_INFO, tgUserId);

        UserVo clickerCacheInfo = redisUtil.get(userInfoCacheKey, UserVo.class);
        if (Objects.isNull(clickerCacheInfo)) {
            UserVo userInfo = getUserInfo(tgUserId);
            if (Objects.isNull(userInfo)) {
                log.error("updateUserInfo user is empty, user is {}, param is {}", tgUserId, clicker);
                return;
            }
            clickerCacheInfo = userInfo;
        }

        RLock lockKey = redissonClient.getLock(String.format("zShot_updateUserInfo_lock:%s", tgUserId));
        try {
            boolean b = lockKey.tryLock(31, 5, TimeUnit.SECONDS);
            if (b) {
                UserVo retry = redisUtil.get(userInfoCacheKey, UserVo.class);
                if (Objects.nonNull(clicker.getIsActive())) {
                    retry.setIsActive(clicker.getIsActive());
                }

                if (Objects.nonNull(clicker.getSource())) {
                    retry.setSource(clicker.getSource());
                }
                //-- update
                if (StringUtils.isNotBlank(clicker.getAddress())) {
                    retry.setAddress(clicker.getAddress());
                }

                if (Objects.nonNull(clicker.getLamports())) {
                    retry.setLamports(clicker.getLamports());
                }
                //-- update
                if (StringUtils.isNotBlank(clicker.getSalt())) {
                    retry.setSalt(clicker.getSalt());
                }
                //-- update
                if (StringUtils.isNotBlank(clicker.getVerifyCode())) {
                    retry.setVerifyCode(clicker.getVerifyCode());
                }

                if (Objects.nonNull(clicker.getYieldRate())) {
                    retry.setYieldRate(clicker.getYieldRate());
                }

                if (Objects.nonNull(clicker.getIncInvitedFriends())) {
                    retry.setInvitedFriends(Objects.isNull(retry.getInvitedFriends()) ? 0
                            : retry.getInvitedFriends() + clicker.getIncInvitedFriends());
                }

                if (Objects.nonNull(clicker.getIncCommission())) {
                    retry.setCommission(Objects.isNull(retry.getCommission()) ? BigDecimal.ZERO
                            : retry.getCommission().add(clicker.getIncCommission()));
                }

                if (Objects.nonNull(clicker.getIsExport())) {
                    retry.setIsExport(clicker.getIsExport());
                }

                redisUtil.setExSeconds(userInfoCacheKey, retry, RedisCacheConstant.EXPIRE_TIME_OUT_DAY_3);

                if (Objects.equals(clickerCacheInfo.getSource(), 1)) {
                    String webAdd = String.format(UserRedisConstant.USER_WEB_ADDRESS, clicker.getAddress());
                    redisUtil.setExSeconds(webAdd, clicker.getTgUserId(), RedisCacheConstant.EXPIRE_TIME_OUT_DAY_4);
                }

                userProducer.sendUserInfo(clicker);
            }
        } catch (InterruptedException e) {
            log.error("updateUserInfo lock error, e->", e);
        } finally {
            if (lockKey.isLocked()) {
                if (lockKey.isHeldByCurrentThread()) {
                    lockKey.unlock();
                }
            }

        }

    }

    public void incInvitedFriends(BigInteger userId) {
        User user = new User();
        user.setTgUserId(userId);
        user.setIncInvitedFriends(1);
        ;
        updateUserInfo(userId, user);
    }

}
