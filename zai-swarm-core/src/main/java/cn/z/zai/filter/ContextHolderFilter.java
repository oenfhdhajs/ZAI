package cn.z.zai.filter;

import cn.z.zai.dto.Response;
import cn.z.zai.util.ContextHolder;
import cn.z.zai.util.IpUtil;
import cn.z.zai.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Order(-1)
@Component
public class ContextHolderFilter implements Filter {
    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            ContextHolder.setIp(IpUtil.getIpAddr(request));

            String reqId = ContextHolder.getRequestId();
            if (StringUtils.isBlank(reqId)) {
                ContextHolder.setRequestId(UUID.randomUUID().toString());
            }
            chain.doFilter(servletRequest, response);
        } finally {
            ContextHolder.clear();
        }

    }

    @Override
    public void destroy() {
        ContextHolder.clear();
    }

    void setResponse(HttpServletResponse httpServletResponse, Integer code, String message) throws IOException {
        Response<?> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.getWriter().write(jsonUtil.obj2String(response));
    }

}
