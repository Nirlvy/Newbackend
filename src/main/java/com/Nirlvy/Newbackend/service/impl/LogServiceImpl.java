package com.Nirlvy.Newbackend.service.impl;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.common.ResultCode;
import com.Nirlvy.Newbackend.entity.Freezer;
import com.Nirlvy.Newbackend.entity.Log;
import com.Nirlvy.Newbackend.exception.ServiceException;
import com.Nirlvy.Newbackend.mapper.LogMapper;
import com.Nirlvy.Newbackend.service.IFreezerService;
import com.Nirlvy.Newbackend.service.ILogService;
import com.Nirlvy.Newbackend.service.IProductService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
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
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    private final IProductService productService;

    private final IFreezerService freezerService;

    public LogServiceImpl(IProductService productService, IFreezerService freezerService) {
        this.productService = productService;
        this.freezerService = freezerService;
    }

    @Override
    public Result top3List(List<String> top3List) {
        List<Map<String, Object>> countList = listMaps(
                new QueryWrapper<Log>()
                        .eq("IO", false)
                        .in("Device", top3List)
                        .select("Product, count(*) as count")
                        .groupBy("Product")
                        .orderByDesc("count")
                        .last("limit 3"));
        return Result.success(countList.stream().map(map -> (String) map.get("Product")).collect(Collectors.toList()));
    }

    @Override
    public Result pointPage(Integer pageNum, Integer pageSize, String sort, String location,
                            String device, String startTime, String endTime) {
        if (pageNum < 0 || pageSize <= 0) {
            throw new ServiceException(ResultCode.FAULT_PAGENUM_OR_PAGESIZE, null);
        }
        QueryWrapper<Log> wrapper = new QueryWrapper<Log>()
                .eq("IO", false)
                .between("Time", startTime, endTime);
        if (device != null) {
            wrapper.eq("Device", device);
        }
        List<Map<String, Object>> countList = listMaps(wrapper
                .select("Device, Product, count(*) as count")
                .groupBy("Product"));
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Map<String, Object> countMap : countList) {
            device = (String) countMap.get("Device");
            String product = (String) countMap.get("Product");
            Long count = (Long) countMap.get("count");
            Double totalPrice = productService.getTotal(device, product, count.intValue());
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("Device", device);
            resultMap.put("Total Price", totalPrice);
            resultMap.put("Count", count);
            List<Freezer> freezers = freezerService.getFreezerLocation(device);
            if (!freezers.isEmpty()) {
                Freezer freezer = freezers.get(0);
                if (Objects.equals(freezer.getLocation(), location)) {
                    continue;
                }
                resultMap.put("Location", freezer.getLocation());
                resultMap.put("SeLocation", freezer.getSeLocation());
            }
            mapList.add(resultMap);
        }
        List<Map<String, Object>> sortedList = mapList.stream()
                .sorted(Comparator.comparingDouble(m -> (Double) m.get("Total Price")))
                .collect(Collectors.toList());
        if ("Desc".equals(sort)) {
            Collections.reverse(sortedList);
        }
        sortedList = sortedList.subList(
                Math.min((pageNum - 1) * pageSize, sortedList.size()),
                Math.min((pageNum * pageSize), sortedList.size()));
        return Result.success(sortedList);
    }

    @Override
    public Result pricePage(Integer pageNum, Integer pageSize, String type) {
        if (pageNum < 0 || pageSize <= 0) {
            throw new ServiceException(ResultCode.FAULT_PAGENUM_OR_PAGESIZE, null);
        }
        List<Map<String, Object>> productList = productService.getPTD(type);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Map<String, Object> productMap : productList) {
            Float Price = (Float) productMap.get("Price");
            String Type = (String) productMap.get("Type");
            String Device = (String) productMap.get("Device");
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("Price", Price);
            resultMap.put("Type", Type);
            resultMap.put("Device", Device);
            List<Log> logs = list(new QueryWrapper<Log>()
                    .eq("Type", Type)
                    .eq("Device", Device)
                    .eq("IO", true)
                    .orderByDesc("Time")
                    .last("limit 1"));
            if (!logs.isEmpty()) {
                Log log = logs.get(0);
                resultMap.put("Time", log.getTime());
                resultMap.put("Cpeople", log.getCpeople());
            }
            mapList.add(resultMap);
        }
        return Result.success(mapList);
    }

}
