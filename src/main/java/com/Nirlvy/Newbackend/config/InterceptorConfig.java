// package com.Nirlvy.Newbackend.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// import com.Nirlvy.Newbackend.config.interceptor.JwtInterceptor;

// @Configuration
// public class InterceptorConfig implements WebMvcConfigurer {

//     @Override
//     public void addInterceptors(InterceptorRegistry registry) {
//         registry.addInterceptor(jwtInterceptor())
//                 .addPathPatterns("/**") // 拦截所有请求，根据token判断是否合法决定登录
//                 .excludePathPatterns("/user/login", "/user/register", "/swagger-ui.html", "/swagger-ui/**",
//                         "/v3/api-docs/**", "/webjars/**", "/swagger-resources/**");
//         WebMvcConfigurer.super.addInterceptors(registry);
//     }

//     @Bean
//     JwtInterceptor jwtInterceptor() {
//         return new JwtInterceptor();
//     }
// }
