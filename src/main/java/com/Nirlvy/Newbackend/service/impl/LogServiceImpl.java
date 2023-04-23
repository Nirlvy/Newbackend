package com.Nirlvy.Newbackend.service.impl;

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

    private QueryWrapper<Log> pageWrapper(Integer pageNum, Integer pageSize, List<Object> deviceList, String device, String startTime, String endTime) {
        if (pageNum <= 0 || pageSize <= 0) {
            throw new ServiceException(ResultCode.FAULT_PAGENUM_OR_PAGESIZE, null);
        }
        QueryWrapper<Log> wrapper = new QueryWrapper<Log>()
                .eq("IO", false)
                .between("Time", startTime, endTime);
        if (deviceList != null && !deviceList.isEmpty()) {
            wrapper.in("Device", deviceList);
        }
        if (device != null) {
            wrapper.eq("Device", device);
        }
        return wrapper;
    }

    private List<Map<String, Object>> countList(QueryWrapper<Log> wrapper, boolean rep) {
        List<Map<String, Object>> countList = listMaps(wrapper
                .select("Device, Type, count(*) as count")
                .groupBy("Device, Type"));
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Map<String, Object> countMap : countList) {
            boolean repeat = false;
            String device = (String) countMap.get("Device");
            String type = (String) countMap.get("Type");
            Long count = (Long) countMap.get("count");
            Product product = productService.getTotalAndSeType(device, type);
            for (Map<String, Object> repeatMap : mapList) {
                if (type.equals(repeatMap.get("Type"))) {
                    repeatMap.put("Total Price", ((Number) repeatMap.get("Total Price")).doubleValue() + product.getPrice());
                    repeatMap.put("count", ((Number) repeatMap.get("count")).longValue() + count);
                    repeat = rep;
                    break;
                }
            }
            if (repeat) {
                continue;
            }
            countMap.put("Total Price", product.getPrice() * count);
            countMap.put("SeType", product.getSeType());
            countMap.put("deviceCount", countList.stream()
                    .map(data -> (String) data.get("Device"))
                    .filter(Objects::nonNull)
                    .distinct()
                    .count());
            mapList.add(countMap);
        }
        return mapList;
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
        QueryWrapper<Log> wrapper = pageWrapper(pageNum, pageSize, null, device, startTime, endTime);
        List<Map<String, Object>> countList = listMaps(wrapper
                .select("Device, Product, count(*) as count")
                .groupBy("Device, Product"));
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Map<String, Object> countMap : countList) {
            boolean repeat = false;
            device = (String) countMap.get("Device");
            String product = (String) countMap.get("Product");
            Long count = (Long) countMap.get("count");
            Double totalPrice = productService.getTotal(device, product, count.intValue());
            for (Map<String, Object> repeatMap : mapList) {
                if ((repeatMap.get("Device")).equals(device)) {
                    repeatMap.put("Total Price", ((Number) repeatMap.get("Total Price")).doubleValue() + totalPrice);
                    repeatMap.put("Count", ((Number) repeatMap.get("Count")).longValue() + count);
                    repeat = true;
                    break;
                }
            }
            if (repeat) {
                continue;
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("Device", device);
            resultMap.put("Total Price", totalPrice);
            resultMap.put("Count", count);
            List<Freezer> freezers = freezerService.getFreezerLocation(device);
            if (!freezers.isEmpty()) {
                Freezer freezer = freezers.get(0);
                if (Objects.equals(freezer.getLocation(), location)) {
                    mapList.add(resultMap);
                    continue;
                }
                resultMap.put("Location", freezer.getLocation());
                resultMap.put("SeLocation", freezer.getSeLocation());
                mapList.add(resultMap);
            }
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
                    .eq("IO", false)
                    .eq("Count", 0)
                    .orderByDesc("Time")
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
        List<Object> deviceList = new ArrayList<>();
        if (location != null) {
            deviceList = freezerService.listObjs(new QueryWrapper<Freezer>()
                    .eq("Location", location)
                    .select("Id"));
        }
        QueryWrapper<Log> wrapper = pageWrapper(pageNum, pageSize, deviceList, Device, startTime, endTime);
        List<Map<String, Object>> mapList = countList(wrapper, true);
        mapList.forEach(map -> map.remove("Device"));
        if ("price".equals(sortType)) {
            mapList = new ArrayList<>(mapList.stream()
                    .sorted(Comparator.comparingDouble(m -> (Double) m.get("Total Price")))
                    .toList());
        } else if ("count".equals(sortType)) {
            mapList = new ArrayList<>(mapList.stream()
                    .sorted(Comparator.comparingDouble(m -> (Long) m.get("count")))
                    .toList());
        }
        if ("Desc".equals(sort)) {
            Collections.reverse(mapList);
        }
        mapList = mapList.subList(
                Math.min((pageNum - 1) * pageSize, mapList.size()),
                Math.min((pageNum * pageSize), mapList.size()));
        return Result.success(mapList);
    }

    @Override
    public Result outletsPage(Integer pageNum, Integer pageSize, String sort, String sortType, String startTime, String endTime) {
        QueryWrapper<Log> wrapper = pageWrapper(pageNum, pageSize, null, null, startTime, endTime);
        List<Map<String, Object>> mapList = countList(wrapper, false);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            boolean repeat = false;
            String device = (String) map.get("Device");
            Double totalPrice = (Double) map.get("Total Price");
            Long deviceCount = (Long) map.get("deviceCount");
            Long count = (Long) map.get("count");
            String location = freezerService.getOne(new QueryWrapper<Freezer>()
                            .eq("Id", device)
                            .select("Location"))
                    .getLocation();
            for (Map<String, Object> repeatMap : mapList) {
                if (location.equals(repeatMap.get("Location"))) {
                    repeatMap.put("Total Price", ((Number) repeatMap.get("Total Price")).doubleValue() + totalPrice);
                    repeatMap.put("deviceCount", ((Number) repeatMap.get("deviceCount")).longValue() + deviceCount);
                    repeatMap.put("count", ((Number) repeatMap.get("count")).longValue() + count);
                    repeat = true;
                    break;
                }
            }
            if (repeat) {
                continue;
            }
            map.put("Location", location);
            map.put("Avg", totalPrice / deviceCount);
            map.remove("Type");
            map.remove("SeType");
            map.remove("Device");
            result.add(map);
        }
        List<Map<String, Object>> noneFreezerList = freezerService.listMaps(new QueryWrapper<Freezer>()
                .isNotNull("Location")
                .select("Location,count(*) as Count")
                .groupBy("Location"));
        for (Map<String, Object> noneFreezer : noneFreezerList) {
            boolean repeat = false;
            for (Map<String, Object> map : result) {
                if (map.get("Location").equals(noneFreezer.get("Location"))) {
                    repeat = true;
                    break;
                }
            }
            if (repeat) {
                continue;
            }
            noneFreezer.put("Total Price", 0.0);
            noneFreezer.put("Avg", 0.0);
            noneFreezer.put("deviceCount", noneFreezer.get("Count"));
            noneFreezer.put("count", 0L);
            noneFreezer.remove("Count");
            result.add(noneFreezer);
        }
        if ("price".equals(sortType)) {
            result = new ArrayList<>(result.stream()
                    .sorted(Comparator.comparingDouble(m -> (Double) m.get("Total Price")))
                    .toList());
        } else if ("avg".equals(sortType)) {
            result = new ArrayList<>(result.stream()
                    .sorted(Comparator.comparingDouble(m -> (Long) m.get("Avg")))
                    .toList());
        }
        if ("Desc".equals(sort)) {
            Collections.reverse(result);
        }
        result = result.subList(
                Math.min((pageNum - 1) * pageSize, result.size()),
                Math.min((pageNum * pageSize), result.size()));
        return Result.success(result);
    }

}
