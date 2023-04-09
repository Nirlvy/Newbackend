package com.Nirlvy.Newbackend.service;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.Freezer;
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
public interface IFreezerService extends IService<Freezer> {

    Result add(Freezer freezer);

    List<String> top3List(String location, String selocation);

    List<Freezer> getFreezerLocation(String Id);

}
