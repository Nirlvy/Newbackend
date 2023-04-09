package com.Nirlvy.Newbackend.service;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.FeedBack;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-08
 */
public interface IFeedBackService extends IService<FeedBack> {

    Result add(FeedBack feedBack);

    Result getList(String device);

    Result alter(FeedBack feedBack);

}
