//package com.Nirlvy.Newbackend.config.interceptor;
//
//import cn.hutool.core.util.StrUtil;
//import com.Nirlvy.Newbackend.common.ResultCode;
//import com.Nirlvy.Newbackend.entity.User;
//import com.Nirlvy.Newbackend.exception.ServiceException;
//import com.Nirlvy.Newbackend.service.IUserService;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//public class JwtInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private IUserService userService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
//        String token = request.getHeader("token");
//        // 如果不是映射到方法直接通过
//        if (!(handler instanceof HandlerMethod)) {
//            return true;
//        }
//        // 执行认证
//        if (StrUtil.isBlank(token)) {
//            throw new ServiceException(ResultCode.TOKEN_NOT_EXISTS, null);
//        }
//        // 获取token的userId
//        String userId;
//        try {
//            userId = JWT.decode(token).getAudience().get(0);
//        } catch (JWTDecodeException j) {
//            throw new ServiceException(ResultCode.FAKE_TOKEN, null);
//        }
//        // 根据token中的userid查询数据库
//        User user = userService.getById(userId);
//        if (user == null) {
//            throw new ServiceException(ResultCode.UNKNOWN_USER, null);
//        }
//        // 验证token
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
//        try {
//            jwtVerifier.verify(token);
//        } catch (JWTVerificationException e) {
//            throw new ServiceException(ResultCode.TOKEN_ERROR, null);
//        }
//
//        return true;
//    }
//
//}
