package com.Nirlvy.Newbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.common.ResultCode;
import com.Nirlvy.Newbackend.entity.Freezer;
import com.Nirlvy.Newbackend.entity.Log;
import com.Nirlvy.Newbackend.entity.Product;
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
import java.util.stream.Stream;

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

    private QueryWrapper<Log> pageWrapper(Integer pageNum, Integer pageSize, List<String> deviceList, String startTime, String endTime) {
        if (pageNum <= 0 || pageSize <= 0) {
            throw new ServiceException(ResultCode.FAULT_PAGENUM_OR_PAGESIZE, null);
        }
        if (DateUtil.parse(startTime).compareTo(DateUtil.parse(endTime)) > 0) {
            throw new ServiceException(ResultCode.INVALID_PARAMS, null);
        }
        QueryWrapper<Log> queryWrapper = new QueryWrapper<Log>().eq("iO", false).between("time", startTime, endTime);
        if (CollUtil.isNotEmpty(deviceList)) {
            queryWrapper.in("device", deviceList);
        }
        return queryWrapper;
    }

    private List<Map<String, Object>> countList(QueryWrapper<Log> wrapper, Integer productOrOutletsOrPoint) {
        if (productOrOutletsOrPoint == 0) {
            wrapper.select("device, type, price * count(*) as totalPrice, count(*) as count");
            wrapper.groupBy("device, type, price");
        } else {
            wrapper.select("device, price * count(*) as totalPrice, count(*) as count");
            wrapper.groupBy("device, price");
        }
        List<Map<String, Object>> countList = listMaps(wrapper);
        if (productOrOutletsOrPoint > 0) {
            for (Map<String, Object> newMap : countList) {
                Freezer freezer = freezerService.getOne(new QueryWrapper<Freezer>()
                        .eq("id", newMap.get("device"))
                        .select("location, seLocation")
                );
                newMap.put("location", freezer.getLocation());
                if (productOrOutletsOrPoint == 2) {
                    newMap.put("seLocation", freezer.getSeLocation());
                }
            }
        }
        Map<String, List<Map<String, Object>>> groupMap = countList.stream()
                .collect(Collectors.groupingBy(m -> m.get(productOrOutletsOrPoint == 0 ?
                        "type" : productOrOutletsOrPoint == 1 ? "location" : "device").toString()));
        return groupMap.entrySet().stream()
                .map(e -> {
                    String group = e.getKey();
                    Double price = e.getValue().stream()
                            .mapToDouble(m -> Double.parseDouble(m.get("totalPrice").toString()))
                            .sum();
                    Long count = e.getValue().stream()
                            .mapToLong(m -> Long.parseLong(m.get("count").toString()))
                            .sum();
                    Map<String, Object> map = new HashMap<>();
                    switch (productOrOutletsOrPoint) {
                        case 0 -> {
                            map.put("Type", group);
                            map.put("SeType", productService.getOne(new QueryWrapper<Product>()
                                    .eq("type", group)
                                    .select("seType")
                                    .last("limit 1")
                            ).getSeType());
                            map.put("Img", productService
                                    .getOne(new QueryWrapper<Product>()
                                            .eq("type", group)
                                            .eq("device", e.getValue().get(0).get("device"))
                                            .select("img")
                                            .last("limit 1")).getImg());
                        }
                        case 1 -> {
                            map.put("Location", group);
                            map.put("DeviceAvg", NumberUtil.round(price / e.getValue().size(), 2).doubleValue());
                        }
                        case 2 -> {
                            map.put("Device", group);
                            map.put("Location", e.getValue().get(0).get("location"));
                            map.put("SeLocation", e.getValue().get(0).get("seLocation"));
                        }
                    }
                    map.put("Total Price", price);
                    map.put("Count", count);
                    map.put("Ave", NumberUtil.round(price / count, 2));
                    map.put("DeviceCount", e.getValue().size());
                    return map;
                }).toList();
    }

    private List<Map<String, Object>> sort(List<Map<String, Object>> mapList, String sort, Integer pageNum, Integer pageSize) {
        if (mapList.size() == 0) {
            return mapList;
        }
        if ("Desc".equals(sort)) {
            Collections.reverse(mapList);
        }
        mapList = mapList.subList(
                Math.min((pageNum - 1) * pageSize, mapList.size()),
                Math.min((pageNum * pageSize), mapList.size()));
        Map<String, Object> totalMap = new HashMap<>();
        Double totalPrice = mapList.stream()
                .mapToDouble(map -> (Double) map.get("Total Price"))
                .sum();
        Long totalCount = mapList.stream()
                .mapToLong(map -> (Long) map.get("Count"))
                .sum();
        totalMap.put("Total Price", totalPrice);
        totalMap.put("Total Count", totalCount);
        totalMap.put("Total Ave", NumberUtil.round(totalPrice / totalCount, 2));
        mapList.add(totalMap);
        return mapList;
    }

    @Override
    public Result top3List(List<String> top3List) {
        List<Map<String, Object>> countList = listMaps(
                new QueryWrapper<Log>()
                        .eq("iO", false)
                        .in("device", top3List)
                        .select("type, count(*) as count")
                        .groupBy("type")
                        .orderByDesc("count")
                        .last("limit 3"));
        return Result.success(countList.stream().map(map -> (String) map.get("Product")).collect(Collectors.toList()));
    }

    @Override
    public Result pointPage(Integer pageNum, Integer pageSize, String sort, String deviceOrLocation,
                            String startTime, String endTime) {
        List<String> deviceList = freezerService.listObjs(new QueryWrapper<Freezer>()
                .like("id", deviceOrLocation)
                .select("id")).stream().map(Object::toString).toList();
        List<String> locationList = freezerService.listObjs(new QueryWrapper<Freezer>()
                .like("location", deviceOrLocation)
                .select("id")).stream().map(Object::toString).toList();
        List<String> idList = Stream.concat(
                        deviceList.stream(),
                        locationList.stream()
                ).filter(Objects::nonNull)
                .toList();
        if (idList.size() == 0 && deviceOrLocation != null) {
            return Result.success();
        }
        QueryWrapper<Log> wrapper = pageWrapper(pageNum, pageSize, idList, startTime, endTime);
        List<Map<String, Object>> countList = countList(wrapper, 2);
        countList = new ArrayList<>(countList.stream()
                .sorted(Comparator.comparingDouble(m -> (Double) m.get("Total Price")))
                .toList());
        return Result.success(sort(countList, sort, pageNum, pageSize));
    }

    @Override
    public Result pricePage(Integer pageNum, Integer pageSize, String type) {
        if (pageNum < 0 || pageSize <= 0) {
            throw new ServiceException(ResultCode.FAULT_PAGENUM_OR_PAGESIZE, null);
        }
        List<Map<String, Object>> productList = productService.getPTD(type);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Map<String, Object> productMap : productList) {
            Float Price = (Float) productMap.get("price");
            String Type = (String) productMap.get("type");
            String Device = (String) productMap.get("device");
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("Price", Price);
            resultMap.put("Type", Type);
            resultMap.put("Device", Device);
            resultMap.put("Img", productService
                    .getOne(new QueryWrapper<Product>()
                            .eq("type", Type)
                            .eq("device", Device)
                            .select("img")
                            .last("limit 1")).getImg());
            List<Log> logs = list(new QueryWrapper<Log>()
                    .eq("type", Type)
                    .eq("device", Device)
                    .and(w -> w.eq("iO", true).or().isNull("iO"))
                    .orderByDesc("time")
                    .last("limit 1"));
            if (!logs.isEmpty()) {
                Log log = logs.get(0);
                resultMap.put("Time", log.getTime());
                resultMap.put("Cpeople", log.getCpeople());
            }
            mapList.add(resultMap);
        }
        mapList = mapList.subList(
                Math.min((pageNum - 1) * pageSize, mapList.size()),
                Math.min((pageNum * pageSize), mapList.size()));
        return Result.success(mapList);
    }

    @Override
    public Result productPage(Integer pageNum, Integer pageSize, String sort, String sortType, String location, String Device, String startTime, String endTime) {
        List<String> deviceList = Device == null ? new ArrayList<>() : List.of(Device);
        if (location != null) {
            List<String> list = freezerService.listObjs(new QueryWrapper<Freezer>()
                            .eq("location", location)
                            .select("id"))
                    .stream()
                    .map(Object::toString)
                    .toList();
            if (!list.contains(Device) && Device != null) {
                throw new ServiceException(ResultCode.UNKNOWN_FREEZER, null);
            }
            deviceList = Device == null ? deviceList : list;
        }
        QueryWrapper<Log> wrapper = pageWrapper(pageNum, pageSize, deviceList, startTime, endTime);
        List<Map<String, Object>> mapList = countList(wrapper, 0);
        if ("price".equals(sortType)) {
            mapList = new ArrayList<>(mapList.stream()
                    .sorted(Comparator.comparingDouble(m -> (Double) m.get("Total Price")))
                    .toList());
        } else if ("count".equals(sortType)) {
            mapList = new ArrayList<>(mapList.stream()
                    .sorted(Comparator.comparingDouble(m -> (Long) m.get("Count")))
                    .toList());
        }
        return Result.success(sort(mapList, sort, pageNum, pageSize));
    }

    @Override
    public Result outletsPage(Integer pageNum, Integer pageSize, String sort, String sortType, String startTime, String endTime) {
        QueryWrapper<Log> wrapper = pageWrapper(pageNum, pageSize, null, startTime, endTime);
        List<Map<String, Object>> mapList = countList(wrapper, 1);
        if ("Total Price".equals(sortType)) {
            mapList = new ArrayList<>(mapList.stream()
                    .sorted(Comparator.comparingDouble(m -> (Double) m.get("Total Price")))
                    .toList());
        } else if ("DeviceAvg".equals(sortType)) {
            mapList = new ArrayList<>(mapList.stream()
                    .sorted(Comparator.comparingDouble(m -> (Double) m.get("DeviceAvg")))
                    .toList());
        }
        return Result.success(sort(mapList, sort, pageNum, pageSize));
    }

    @Override
    public Result uploadOrSoldDays(String choose, String upOrSold) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        if (Objects.equals(choose, "month")) {
            int month = calendar.get(Calendar.MONTH) + 1;
            QueryWrapper<Log> wrapper = new QueryWrapper<>();
            wrapper.apply("YEAR(time) = {0} and MONTH(time) = {1}", year, month);
            wrapper.eq("iO", upOrSold)
                    .select("DAY(time) as day", "count(*) as count")
                    .groupBy("DAY(time)");
            List<Map<String, Object>> list = listMaps(wrapper);
            int[] counts = new int[32];
            int sum = 0;
            for (Map<String, Object> map : list) {
                int day = (int) map.get("day");
                int count = ((Long) map.get("count")).intValue();
                counts[day - 1] = count;
                sum += count;
            }
            counts[31] = sum;
            return Result.success(counts);
        } else {
            QueryWrapper<Log> wrapper = new QueryWrapper<>();
            wrapper.apply("YEAR(time) = {0}", year);
            wrapper.eq("iO", upOrSold)
                    .select("MONTH(time) as month", "count(*) as count")
                    .groupBy("MONTH(time)");
            List<Map<String, Object>> list = listMaps(wrapper);
            int[] counts = new int[13];
            int sum = 0;
            for (Map<String, Object> map : list) {
                int month = (int) map.get("month");
                int count = ((Long) map.get("count")).intValue();
                counts[month - 1] = count;
                sum += count;
            }
            counts[12] = sum;
            return Result.success(counts);
        }
    }

}
