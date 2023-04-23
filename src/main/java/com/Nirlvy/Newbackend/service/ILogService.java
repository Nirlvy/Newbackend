package com.Nirlvy.Newbackend.service;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-07
 */
public interface ILogService extends IService<Log> {

    Result top3List(List<String> top3List);

    Result pointPage(Integer pageNum, Integer pageSize, String sort, String location, String device,
                     String startTime,
                     String endTime);

    Result pricePage(Integer pageNum, Integer pageSize, String type);

    Result productPage(Integer pageNum, Integer pageSize, String sort, String sortType, String location, String device, String startTime, String endTime);

    Result outletsPage(Integer pageNum, Integer pageSize, String sort, String sortType, String startTime, String endTime);

}
