package com.Nirlvy.Newbackend.service;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.AddProductDTO;
import com.Nirlvy.Newbackend.entity.Product;
import com.Nirlvy.Newbackend.entity.UpdateProductDTO;
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

    List<Map<String, Object>> getPTD(String type);

    Result updateData(UpdateProductDTO updateProductDTO);

    Result add(AddProductDTO product);
}
