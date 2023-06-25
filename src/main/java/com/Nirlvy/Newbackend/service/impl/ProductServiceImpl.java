package com.Nirlvy.Newbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.common.ResultCode;
import com.Nirlvy.Newbackend.entity.*;
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
    public List<Map<String, Object>> getPTD(String type) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        if (type != null) {
            wrapper.like("type", type);
        }
        return listMaps(wrapper
                .select("price, type, device")
                .groupBy("price, type, device"));
    }

    @Override
    public Result updateData(UpdateProductDTO updateProductDTO) {
        if (BeanUtil.hasNullField(updateProductDTO, "img", "cpeople")) {
            throw new ServiceException(ResultCode.INVALID_PARAMS, null);
        }
        if (getOne(new QueryWrapper<Product>()
                .eq("type", updateProductDTO.getType())
                .eq("device", updateProductDTO.getDevice())) == null) {
            throw new ServiceException(ResultCode.UNKNOWN_PRODUCT_AND_FREEZER, null);
        }
        Integer count = getOne(new QueryWrapper<Product>()
                .eq("device", updateProductDTO.getDevice())
                .eq("type", updateProductDTO.getType()))
                .getCount();
        if (updateProductDTO.getPriceOrCount() ? updateProductDTO.getNum() <= 0 : updateProductDTO.getNum() + count < 0) {
            throw new ServiceException(ResultCode.INVALID_PARAMS, null);
        }
        Log log = new Log();
        log.setIo(updateProductDTO.getPriceOrCount() ? null : updateProductDTO.getNum() > 0);
        log.setDevice(updateProductDTO.getDevice());
        log.setType(updateProductDTO.getType());
        log.setTime(LocalDateTime.now());
        log.setCpeople(updateProductDTO.getCpeople());
        log.setPrice(updateProductDTO.getPriceOrCount() ? updateProductDTO.getNum() : getOne(new QueryWrapper<Product>().eq("device", updateProductDTO.getDevice()).eq("type", updateProductDTO.getType())).getPrice());
        log.setCount(updateProductDTO.getPriceOrCount() ? null : Math.abs(updateProductDTO.getNum().intValue()));
        Product product = new Product();
        if (updateProductDTO.getPriceOrCount()) {
            product.setPrice(updateProductDTO.getNum());
        } else {
            product.setCount(count + updateProductDTO.getNum().intValue());
        }
        try {
            logService.save(log);
            update(product, new QueryWrapper<Product>()
                    .eq("device", updateProductDTO.getDevice())
                    .eq("type", updateProductDTO.getType()));
            Freezer freezer = freezerService.getById(updateProductDTO.getDevice());
            freezer.setSupplyTime(LocalDateTime.now());
            freezerService.updateById(freezer);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SYSTEM_ERROR, e);
        }
        return Result.success();
    }

    @Override
    public Result add(AddProductDTO product) {
        if (BeanUtil.hasNullField(product, "id")) {
            throw new ServiceException(ResultCode.INVALID_PARAMS, null);
        }
        String type = product.getType();
        String device = product.getDevice();
        if (freezerService.getById(device) != null) {
            if (getOne(new QueryWrapper<Product>().eq("type", type).eq("device", device)) == null) {
                Product one = new Product();
                BeanUtil.copyProperties(product, one, true);
                save(one);
                Log log = new Log();
                log.setIo(true);
                log.setDevice(device);
                log.setPrice(product.getPrice());
                log.setType(type);
                log.setCount(product.getCount());
                log.setTime(LocalDateTime.now());
                log.setCpeople(product.getCpeople());
                logService.save(log);
                Freezer freezer = freezerService.getById(device);
                freezer.setSupplyTime(LocalDateTime.now());
                freezerService.updateById(freezer);
            } else {
                throw new ServiceException(ResultCode.PRODUCT_ALREADY_EXISTS, null);
            }
        } else {
            throw new ServiceException(ResultCode.UNKNOWN_FREEZER, null);
        }
        return Result.success();
    }
}
