package cn.z.zai.filter;

import cn.z.zai.common.constant.ErrorConstant;
import cn.z.zai.common.constant.RedisCacheConstant;
import cn.z.zai.common.constant.ResponseCodeConstant;
import cn.z.zai.dto.Response;
import cn.z.zai.service.GeoIpInfoService;
import cn.z.zai.util.ContextHolder;
import cn.z.zai.util.JsonUtil;
import cn.z.zai.util.JwtUtils;
import cn.z.zai.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class AuthorizationFilter implements Filter {

    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${spring.profiles.active}")
    private String env;
    @Autowired
    private GeoIpInfoService geoIpInfoService;


    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setHeader("Access-Control-Expose-Headers", "Date");

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Integer upgradeStatus = redisUtil.get(RedisCacheConstant.UPGRADE_STATUS, Integer.class);
        if (Objects.equals(upgradeStatus, -1) && !request.getRequestURI().contains("/updateStatus")) {
            this.print(servletResponse, Response.fail(ResponseCodeConstant.UPGRADE));
            return;
        }
        ContextHolder.setAppVersion(request.getHeader("appVersion"));
        MDC.put("logTrackId", ContextHolder.getRequestId());
        if (isIgnoreUrl(request)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String token = request.getHeader(jwtUtils.getHeader());
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(jwtUtils.getHeader());
        }
        if (StringUtils.isBlank(token)) {
            this.print(servletResponse, Response.fail(ResponseCodeConstant.ERROR_GLOBAL_UN_AUTH));
            return;
        }
        Claims claims = jwtUtils.getClaimByToken(token);
        if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
            this.print(servletResponse, Response.fail(ResponseCodeConstant.ERROR_GLOBAL_UN_AUTH));
            return;
        }
        String key = String.format(RedisCacheConstant.AUTH_TOKEN_KEY, claims.getId());
        Object tokenCache = redisUtil.get(key);
        if (!token.equals(tokenCache)) {
            this.print(servletResponse, Response.fail(ResponseCodeConstant.ERROR_GLOBAL_UN_AUTH));
            return;
        }
        redisUtil.expire(key, RedisCacheConstant.EXPIRE_TIME_OUT_DAY_3, TimeUnit.SECONDS);

        String userIdString = claims.getId().split("_")[0];
        if (StringUtils.isEmpty(userIdString)) {
            this.print(servletResponse, Response.fail(ResponseCodeConstant.ERROR_GLOBAL_USER_NOT_FOUNT));
            return;
        }
        BigInteger userId = BigInteger.valueOf(Long.parseLong(userIdString));
        if (userId.compareTo(BigInteger.ZERO) <= 0) {
            this.print(servletResponse, Response.fail(ResponseCodeConstant.ERROR_GLOBAL_USER_NOT_FOUNT));
            return;
        }
        ContextHolder.setUserId(userId);

        if (geoIpInfoService.needAccessDenied()) {
            log.error("This service is not available in your region. {}", ContextHolder.getUserId());
            this.print(servletResponse, Response.fail(ErrorConstant.NOT_AVAILABLE, "This service is not available in your region."));
            return;
        }

        MDC.put("logTrackId", ContextHolder.getRequestId() + "_" + ContextHolder.getUserId());

        String requestURI = request.getServletPath();
        if (!"/health-check".equalsIgnoreCase(requestURI)) {
            log.info("request uri :{}", requestURI);
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            ContextHolder.clear();
            MDC.clear();
        }
    }

    private void print(ServletResponse response, Response result) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().print(jsonUtil.obj2String(result));
    }

    private boolean isIgnoreUrl(HttpServletRequest request) {
        // Quick matching to ensure performance
        if (jwtUtils.getExcludeUrl().contains(request.getRequestURI())) {
            return true;
        }
        // Match Ant paths one by one
        for (String url : jwtUtils.getExcludeUrl()) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }
}