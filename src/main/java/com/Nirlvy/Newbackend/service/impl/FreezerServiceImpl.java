package com.Nirlvy.Newbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.common.ResultCode;
import com.Nirlvy.Newbackend.entity.Freezer;
import com.Nirlvy.Newbackend.entity.Log;
import com.Nirlvy.Newbackend.exception.ServiceException;
import com.Nirlvy.Newbackend.mapper.FreezerMapper;
import com.Nirlvy.Newbackend.service.IFreezerService;
import com.Nirlvy.Newbackend.service.ILogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final ILogService logService;

    public FreezerServiceImpl(@Lazy ILogService logService) {
        this.logService = logService;
    }

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
        if (location.isBlank() || selocation.isBlank()) {
            throw new ServiceException(ResultCode.FAULT_LOCATION, null);
        }
        List<Freezer> freezerList = list(new QueryWrapper<Freezer>().eq("Location", location).eq("SeLocation", selocation));
        if (CollectionUtils.isEmpty(freezerList)) {
            throw new ServiceException(ResultCode.FAULT_LOCATION, null);
        }
        return freezerList.stream().map(Freezer::getId).collect(Collectors.toList());
    }

    @Override
    public List<Freezer> getFreezerLocation(String id) {
        return list(new QueryWrapper<Freezer>().eq("id", id).select("id", "location", "seLocation"));
    }

    @Override
    public Result locationList() {
        List<Map<String, Object>> data = listMaps(new QueryWrapper<Freezer>()
                .select("location,seLocation").isNotNull("location"));
        Map<String, List<Object>> resultMap = new HashMap<>();
        for (Map<String, Object> row : data) {
            String key = (String) row.get("location");
            Object value = row.get("seLocation");
            if (resultMap.containsKey(key)) {
                resultMap.get(key).add(value);
            } else {
                List<Object> list = new ArrayList<>();
                list.add(value);
                resultMap.put(key, list);
            }
        }
        return Result.success(resultMap);
    }

    @Override
    @Transactional
    public Result removeBatch(List<Integer> ids) {
        try {
            removeBatchByIds(ids);
            logService.remove(new QueryWrapper<Log>().in("device", ids));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
        return Result.success();
    }

}
