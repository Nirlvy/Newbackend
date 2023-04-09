package com.Nirlvy.Newbackend.service;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.Ulogin;
import com.Nirlvy.Newbackend.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-07
 */
public interface IUserService extends IService<User> {

    IPage<User> findPage(Integer id, String userName, String createTime, Integer pageNum, Integer pageSize);

    void export(HttpServletResponse response) throws Exception;

    boolean imp(MultipartFile file) throws Exception;

    boolean sOu(User user);

    Result login(Ulogin ulogin);

    Result register(Ulogin ulogin);

}
