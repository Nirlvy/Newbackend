package com.Nirlvy.Newbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.common.ResultCode;
import com.Nirlvy.Newbackend.entity.Ulogin;
import com.Nirlvy.Newbackend.entity.User;
import com.Nirlvy.Newbackend.exception.ServiceException;
import com.Nirlvy.Newbackend.mapper.UserMapper;
import com.Nirlvy.Newbackend.service.IUserService;
import com.Nirlvy.Newbackend.utils.TokenUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private User getUserInfo(Ulogin ulogin) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", ulogin.getUserName());
        queryWrapper.eq("password", ulogin.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
        return one;
    }

    @Override
    public Result login(Ulogin ulogin) {
        String userName = ulogin.getUserName();
        String password = ulogin.getPassword();
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            throw new ServiceException(ResultCode.USERNAME_OR_PASSWORD_IS_BLANK, null);
        }
        User one = getUserInfo(ulogin);
        if (one != null) {
            BeanUtil.copyProperties(one, ulogin, true);
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            ulogin.setToken(token);
            return Result.success(ulogin);
        } else {
            throw new ServiceException(ResultCode.USERNAME_OR_PASSWORD_INCORRECT, null);
        }
    }

    @Override
    public Result register(Ulogin ulogin) {
        String userName = ulogin.getUserName();
        String password = ulogin.getPassword();
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            throw new ServiceException(ResultCode.USERNAME_OR_PASSWORD_IS_BLANK, null);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", userName);
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(ulogin, one, true);
            save(one);
            User userinfo = getUserInfo(ulogin);
            ulogin.setId(userinfo.getId());
            String token = TokenUtils.genToken(userinfo.getId().toString(), one.getPassword());
            ulogin.setToken(token);
        } else {
            throw new ServiceException(ResultCode.USERNAME_ALREADY_EXISTS, null);
        }
        return Result.success(ulogin);
    }

    @Override
    public IPage<User> findPage(Integer id, String userName, String createTime,
                                Integer pageNum, Integer pageSize) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (id != null)
            queryWrapper.eq("id", id);
        queryWrapper.like("userName", userName);
        if (!"null".equals(createTime))
            queryWrapper.like("createTime", createTime);
        return page(page, queryWrapper);
    }

    @Override
    public void export(HttpServletResponse response) throws Exception {
        List<User> list = list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=data.xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }

    @Override
    @Transactional
    public boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> list = reader.readAll(User.class);
        saveOrUpdateBatch(list);
        return true;
    }

    @Override
    @Transactional
    public boolean sOu(User user) {
        if (Objects.equals(user.getPassword(), "")) {
            user.setPassword(null);
        }
        return saveOrUpdate(user);
    }

}
