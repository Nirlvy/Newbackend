package com.Nirlvy.Newbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.common.ResultCode;
import com.Nirlvy.Newbackend.entity.Freezer;
import com.Nirlvy.Newbackend.exception.ServiceException;
import com.Nirlvy.Newbackend.mapper.FreezerMapper;
import com.Nirlvy.Newbackend.service.IFreezerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-07
 */
@Service
public class FreezerServiceImpl extends ServiceImpl<FreezerMapper, Freezer> implements IFreezerService {

    @Override
    public Result add(Freezer freezer) {
        String id = freezer.getId();
        if (StrUtil.isBlank(id)) {
            throw new ServiceException(ResultCode.FREEZER_IS_BLANK, null);
        }
        Freezer one;
        try {
            one = getOne(new QueryWrapper<Freezer>().eq("Id", id));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
        if (one == null) {
            one = new Freezer();
            BeanUtil.copyProperties(freezer, one, true);
            save(one);
        } else {
            throw new ServiceException(ResultCode.FREEZER_ALREADY_EXISTS, null);
        }
        return Result.success();
    }

    @Override
    public List<String> top3List(String location, String selocation) {
        return list(new QueryWrapper<Freezer>().eq("Location", location).eq("SeLocation", selocation)).stream()
                .map(Freezer::getId).collect(Collectors.toList());
    }

    @Override
    public List<Freezer> getFreezerLocation(String Id) {
        return list(new QueryWrapper<Freezer>().eq("Id", Id).select("Id", "Location", "SeLocation"));
    }

}
