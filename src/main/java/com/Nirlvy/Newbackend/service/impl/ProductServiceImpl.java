package com.Nirlvy.Newbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.common.ResultCode;
import com.Nirlvy.Newbackend.entity.Log;
import com.Nirlvy.Newbackend.entity.Product;
import com.Nirlvy.Newbackend.entity.UpdateProductDTO;
import com.Nirlvy.Newbackend.exception.ServiceException;
import com.Nirlvy.Newbackend.mapper.ProductMapper;
import com.Nirlvy.Newbackend.service.IFreezerService;
import com.Nirlvy.Newbackend.service.ILogService;
import com.Nirlvy.Newbackend.service.IProductService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final IFreezerService freezerService;
    private final ILogService logService;

    public ProductServiceImpl(IFreezerService freezerService, @Lazy ILogService logService) {
        this.freezerService = freezerService;
        this.logService = logService;
    }

    @Override
    public Double getTotal(String device, String name, Integer count) {
        return getOne(new QueryWrapper<Product>()
                .eq("Name", name)
                .eq("Device", device))
                .getPrice() * count;
    }

    @Override
    public Product getTotalAndSeType(String device, String type) {
        return getOne(new QueryWrapper<Product>()
                .like("Type", type)
                .eq("Device", device)
                .select("Price,Type,SeType,Device")
                .groupBy("Price,Type,SeType"));
    }

    @Override
    public List<Map<String, Object>> getPTD(String type) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        if (type != null) {
            wrapper.like("Type", type);
        }
        return listMaps(wrapper
                .select("Price, Type, Device")
                .groupBy("Price, Type, Device"));
    }

    @Override
    public Result updateData(UpdateProductDTO updateProductDTO) {
        if (BeanUtil.hasNullField(updateProductDTO)) {
            throw new ServiceException(ResultCode.INVALID_PARAMS, null);
        }
        if (getOne(new QueryWrapper<Product>()
                .eq("Name", updateProductDTO.getName())
                .eq("Device", updateProductDTO.getDevice()).last("limit 1")) == null) {
            throw new ServiceException(ResultCode.UNKNOWN_PRODUCT_AND_FREEZER, null);
        }
        Integer count = getOne(new QueryWrapper<Product>()
                .eq("Device", updateProductDTO.getDevice())
                .eq("Name", updateProductDTO.getName()))
                .getCount();
        if (updateProductDTO.getPriceOrCount() ? updateProductDTO.getNum() <= 0 : updateProductDTO.getNum() + count < 0) {
            throw new ServiceException(ResultCode.INVALID_PARAMS, null);
        }
        Log log = new Log();
        log.setDevice(updateProductDTO.getDevice());
        log.setType(getOne(new QueryWrapper<Product>().eq("name", updateProductDTO.getName())).getType());
        log.setTime(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        log.setCpeople(updateProductDTO.getCpeople());
        log.setPrice(updateProductDTO.getPriceOrCount() ? updateProductDTO.getNum() : null);
        log.setCount(updateProductDTO.getPriceOrCount() ? null : updateProductDTO.getNum().intValue());
        Product product = new Product();
        if (updateProductDTO.getPriceOrCount()) {
            product.setPrice(updateProductDTO.getNum());
        } else {
            product.setCount(count + updateProductDTO.getNum().intValue());
        }
        try {
            logService.save(log);
            update(product, new QueryWrapper<Product>()
                    .eq("Device", updateProductDTO.getDevice())
                    .eq("Name", updateProductDTO.getName()));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
        return Result.success();
    }

    @Override
    public Result add(Product product) {
        String name = product.getName();
        String device = product.getDevice();
        try {
            if (freezerService.getById(device) != null) {
                if (getOne(new QueryWrapper<Product>().eq("Name", name).eq("Device", device)) != null) {
                    save(product);
                } else {
                    throw new ServiceException(ResultCode.PRODUCT_ALREADY_EXISTS, null);
                }
            } else {
                throw new ServiceException(ResultCode.UNKNOWN_FREEZER, null);
            }
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
        return Result.success();
    }
}
