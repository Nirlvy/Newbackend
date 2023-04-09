package com.Nirlvy.Newbackend.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.Nirlvy.Newbackend.entity.User;
import com.Nirlvy.Newbackend.service.IUserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
public class TokenUtils {

    private static IUserService staticUserService;

    @Resource
    private IUserService userService;

    /**
     * 生成token
     *
     * @return token
     */
    public static String genToken(String userId, String sign) {
        return JWT.create()
                .withAudience(userId)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 获取当前登录用户信息（暂时没用）
     *
     * @return user对象
     */
    public static User getCurrentUser() {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                String token = request.getHeader("token");
                if (StrUtil.isNotBlank(token)) {
                    String userId = JWT.decode(token).getAudience().get(0);
                    return staticUserService.getById(Integer.valueOf(userId));
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }
}
