package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.service.IFreezerService;
import com.Nirlvy.Newbackend.service.ILogService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/log")
public class LogController {

    private final ILogService logService;

    private final IFreezerService freezerService;

    public LogController(ILogService logService, IFreezerService freezerService) {
        this.logService = logService;
        this.freezerService = freezerService;
    }

    @PostMapping("/pointPage")
    public Result pointPage(@RequestParam Integer pageNum,
                            @RequestParam Integer pageSize,
                            @RequestParam(defaultValue = "Desc") String sort,
                            @RequestParam(required = false) String location,
                            @RequestParam(required = false) String device,
                            @RequestParam(defaultValue = "1970-1-1 0:0:0") String startTime,
                            @RequestParam(defaultValue = "2999-12-31 23:59:59") String endTime) {
        return logService.pointPage(pageNum, pageSize, sort, location, device, startTime, endTime);
    }

    @PostMapping("/top3List")
    public Result top3List(@RequestParam String Location, @RequestParam String seLocation) {
        return logService.top3List(freezerService.top3List(Location, seLocation));
    }

    @GetMapping("/pricePage")
    public Result pricePage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String type) {
        return logService.pricePage(pageNum, pageSize, type);
    }

    @PostMapping("/productPage")
    public Result productPage(@RequestParam Integer pageNum,
                              @RequestParam Integer pageSize,
                              @RequestParam(defaultValue = "Desc") String sort,
                              @RequestParam(defaultValue = "value") String sortType,
                              @RequestParam(required = false) String location,
                              @RequestParam(required = false) String device,
                              @RequestParam(defaultValue = "1970-1-1 0:0:0") String startTime,
                              @RequestParam(defaultValue = "2999-12-31 23:59:59") String endTime) {
        return logService.productPage(pageNum, pageSize, sort, sortType, location, device, startTime, endTime);
    }

}
