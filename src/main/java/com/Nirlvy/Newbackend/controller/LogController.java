package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.service.IFreezerService;
import com.Nirlvy.Newbackend.service.ILogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "点位统计")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                                "code": 200,
                                                "msg": "成功",
                                                "data": [
                                                    {
                                                        "Total Price": 46.0,
                                                        "Device": "HB15154",
                                                        "Count": 12,
                                                        "SeLocation": "二教",
                                                        "Location": "B校"
                                                    },
                                                    {
                                                        "Total Price": 1.0,
                                                        "Device": "HB15153",
                                                        "Count": 1,
                                                        "SeLocation": "一教",
                                                        "Location": "B校"
                                                    }
                                                ]
                                            }"""))
            })
    })
    @GetMapping("/pointPage")
    public Result pointPage(@Parameter(description = "页码", example = "1", required = true)
                            @RequestParam Integer pageNum,
                            @Parameter(description = "条目个数", example = "10", required = true)
                            @RequestParam Integer pageSize,
                            @Parameter(description = "排序", example = "Desc", schema = @Schema(allowableValues = {"Asc", "Desc"}))
                            @RequestParam(defaultValue = "Desc") String sort,
                            @Parameter(description = "位置", example = "B校")
                            @RequestParam(required = false) String location,
                            @Parameter(description = "设备", example = "HB15154")
                            @RequestParam(required = false) String device,
                            @Parameter(description = "起始时间", example = "1970-1-1 0:0:0")
                            @RequestParam(defaultValue = "1970-1-1 0:0:0") String startTime,
                            @Parameter(description = "截止时间", example = "2999-12-31 23:59:59")
                            @RequestParam(defaultValue = "2999-12-31 23:59:59") String endTime) {
        return logService.pointPage(pageNum, pageSize, sort, location, device, startTime, endTime);
    }

    @Operation(summary = "销售量前三")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "code": 200,
                      "msg": "成功",
                      "data": [
                        "康师傅冰红茶绿茶500ml",
                        "康师傅冰红茶红茶300ml"
                      ]
                    }"""))
    })
    @GetMapping("/top3List")
    public Result top3List(@Parameter(description = "位置", example = "B校", required = true)
                           @RequestParam String Location,
                           @Parameter(description = "二级位置", example = "二教", required = true)
                           @RequestParam String seLocation) {
        return logService.top3List(freezerService.top3List(Location, seLocation));
    }

    @Operation(summary = "价格页")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = """
                            {
                              "code": 200,
                              "msg": "成功",
                              "data": [
                                {
                                  "Type": "康师傅冰红茶中瓶系列",
                                  "Price": 2,
                                  "Device": "HB115"
                                },
                                {
                                  "Type": "康师傅冰红茶小瓶系列",
                                  "Price": 3,
                                  "Device": "HB115"
                                },
                                {
                                  "Type": "农夫山泉-茶π系列",
                                  "Price": 4,
                                  "Device": "HB115"
                                },
                                {
                                  "Type": "美汁源果粒橙小瓶系列",
                                  "Price": 3.5,
                                  "Device": "HB115"
                                },
                                {
                                  "Type": "美汁源果粒橙中瓶系列",
                                  "Price": 5,
                                  "Device": "HB115"
                                },
                                {
                                  "Cpeople": "admin",
                                  "Type": "康师傅冰红茶中瓶系列",
                                  "Price": 4,
                                  "Device": "HB15154",
                                  "Time": "2023-04-07T22:00:00"
                                },
                                {
                                  "Type": "康师傅冰红茶小瓶系列",
                                  "Price": 3.5,
                                  "Device": "HB15154"
                                },
                                {
                                  "Type": "农夫山泉-茶π系列",
                                  "Price": 4,
                                  "Device": "HB15154"
                                },
                                {
                                  "Type": "美汁源果粒橙小瓶系列",
                                  "Price": 2.5,
                                  "Device": "HB15154"
                                },
                                {
                                  "Type": "美汁源果粒橙中瓶系列",
                                  "Price": 3,
                                  "Device": "HB15154"
                                }
                              ]
                            }"""))
            }),
    })
    @GetMapping("/pricePage")
    public Result pricePage(
            @Parameter(description = "页码", example = "1", required = true)
            @RequestParam Integer pageNum,
            @Parameter(description = "条目个数", example = "10", required = true)
            @RequestParam Integer pageSize,
            @Parameter(description = "商品类型", example = "康师傅冰红茶")
            @RequestParam(required = false) String type) {
        return logService.pricePage(pageNum, pageSize, type);
    }

    @Operation(summary = "产品页")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                         "code": 200,
                         "msg": "成功",
                         "data": [
                             {
                                 "Type": "康师傅冰红茶小瓶系列",
                                 "Total Price": 15.0,
                                 "SeType": "茶饮料",
                                 "count": 5.0
                             },
                             {
                                 "Type": "康师傅冰红茶中瓶系列",
                                 "Total Price": 32.0,
                                 "SeType": "茶饮料",
                                 "count": 8
                             }
                         ]
                     }"""))
    })
    @GetMapping("/productPage")
    public Result productPage(@Parameter(description = "页码", example = "1", required = true)
                              @RequestParam Integer pageNum,
                              @Parameter(description = "条目个数", example = "10", required = true)
                              @RequestParam Integer pageSize,
                              @Parameter(description = "排序", example = "Desc", schema = @Schema(allowableValues = {"Asc", "Desc"}))
                              @RequestParam(defaultValue = "Desc") String sort,
                              @Parameter(description = "排序类型", example = "price", schema = @Schema(allowableValues = {"price", "count"}))
                              @RequestParam(defaultValue = "price") String sortType,
                              @Parameter(description = "位置", example = "B校")
                              @RequestParam(required = false) String location,
                              @Parameter(description = "设备", example = "HB15154")
                              @RequestParam(required = false) String device,
                              @Parameter(description = "起始时间", example = "1970-1-1 0:0:0")
                              @RequestParam(defaultValue = "1970-1-1 0:0:0") String startTime,
                              @Parameter(description = "截止时间", example = "2999-12-31 23:59:59")
                              @RequestParam(defaultValue = "2999-12-31 23:59:59") String endTime) {
        return logService.productPage(pageNum, pageSize, sort, sortType, location, device, startTime, endTime);
    }

    @Operation(summary = "网点页")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                          "code": 200,
                          "msg": "成功",
                          "data": [
                              {
                                  "Total Price": 48.0,
                                  "deviceCount": 6,
                                  "count": 14,
                                  "Location": "B校"
                              }
                          ]
                      }"""))
    })
    @GetMapping("/outletsPage")
    public Result outletsPage(@Parameter(description = "页码", example = "1", required = true)
                              @RequestParam Integer pageNum,
                              @Parameter(description = "条目个数", example = "10", required = true)
                              @RequestParam Integer pageSize,
                              @Parameter(description = "排序", example = "Desc", schema = @Schema(allowableValues = {"Asc", "Desc"}))
                              @RequestParam(defaultValue = "Desc") String sort,
                              @Parameter(description = "排序类型", example = "price", schema = @Schema(allowableValues = {"price", "avg"}))
                              @RequestParam(defaultValue = "price") String sortType,
                              @Parameter(description = "起始时间", example = "1970-1-1 0:0:0")
                              @RequestParam(defaultValue = "1970-1-1 0:0:0") String startTime,
                              @Parameter(description = "截止时间", example = "2999-12-31 23:59:59")
                              @RequestParam(defaultValue = "2999-12-31 23:59:59") String endTime) {
        return logService.outletsPage(pageNum, pageSize, sort, sortType, startTime, endTime);
    }

}
