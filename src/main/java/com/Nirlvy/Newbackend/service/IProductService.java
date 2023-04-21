package com.Nirlvy.Newbackend.service;

import com.Nirlvy.Newbackend.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-07
 */
public interface IProductService extends IService<Product> {

    Double getTotal(String device, String name, Integer count);

    Product getTotalAndSeType(String device, String type);

    List<Map<String, Object>> getPTD(String type);

    boolean alter(Product product);
}
