package com.Nirlvy.Newbackend.service.impl;

import com.Nirlvy.Newbackend.common.ResultCode;
import com.Nirlvy.Newbackend.entity.Product;
import com.Nirlvy.Newbackend.exception.ServiceException;
import com.Nirlvy.Newbackend.mapper.ProductMapper;
import com.Nirlvy.Newbackend.service.IProductService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-07
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Override
    public Double getTotal(String Device, String Name, Integer Count) {
        return getOne(new QueryWrapper<Product>().eq("Name", Name).eq("Device", Device)).getPrice() * Count;
    }

    @Override
    public List<Map<String, Object>> getPTD(String type) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        if (type != null) {
            wrapper.eq("Type", type);
        }
        return listMaps(wrapper
                .select("Price, Type, Device")
                .groupBy("Price, Type, Device"));
    }

    @Override
    public boolean alter(Product product) {
        String Device = product.getDevice();
        String Type = product.getType();
        if (Device == null || Type == null) {
            throw new ServiceException(ResultCode.TYPE_OR_DEVICE_IS_BLANK, null);
        }
        return update(product, new QueryWrapper<Product>()
                .eq("Device", Device)
                .eq("Type", Type));
    }
}
