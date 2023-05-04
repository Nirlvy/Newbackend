package com.Nirlvy.Newbackend.service.impl;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.common.ResultCode;
import com.Nirlvy.Newbackend.entity.FeedBack;
import com.Nirlvy.Newbackend.exception.ServiceException;
import com.Nirlvy.Newbackend.mapper.FeedBackMapper;
import com.Nirlvy.Newbackend.service.IFeedBackService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-08
 */
@Service
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements IFeedBackService {

    @Override
    public Result add(FeedBack feedBack) {
        try {
            save(feedBack);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
        return Result.success();
    }

    @Override
    public Result getList(String device) {
        try {
            if ("null".equals(device)) {
                return Result.success(list());
            } else {
                return Result.success(list(new QueryWrapper<FeedBack>().like("device", device)));
            }
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
    }

    @Override
    public Result alter(FeedBack feedBack) {
        try {
            updateById(feedBack);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
        return Result.success();
    }

}
