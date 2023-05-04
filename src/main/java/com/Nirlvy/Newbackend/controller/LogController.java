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
                            @Parameter(description = "设备或者位置", example = "HB15154")
                            @RequestParam(required = false) String deviceOrLocation,
                            @Parameter(description = "起始时间", example = "1970-1-1 0:0:0")
                            @RequestParam(defaultValue = "1970-1-1 0:0:0") String startTime,
                            @Parameter(description = "截止时间", example = "2999-12-31 23:59:59")
                            @RequestParam(defaultValue = "2999-12-31 23:59:59") String endTime) {
        return logService.pointPage(pageNum, pageSize, sort, deviceOrLocation, startTime, endTime);
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
                                  "img": "xxx",
                                  "Device": "HB115"
                                },
                                {
                                  "Cpeople": "admin",
                                  "Type": "康师傅冰红茶中瓶系列",
                                  "Price": 4,
                                  "img": "xxx",
                                  "Device": "HB15154",
                                  "Time": "2023-04-07 22:00:00"
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
                            "Ave": "4.13",
                            "Type": "农夫山泉-茶π系列",
                            "img": "xxx",
                            "Total Price": 111.5,
                            "Price": 4,
                            "SeType": "茶饮料",
                            "deviceCount": 8,
                            "count": 27
                          },
                          {
                            "Ave": "3.19",
                            "Type": "康师傅冰红茶中瓶系列",
                            "img": "xxx",
                            "Total Price": 25.5,
                            "Price": 3,
                            "SeType": "茶饮料",
                            "deviceCount": 8,
                            "count": 8
                          },
                          {
                            "Total Price": 192.5,
                            "Total Count": 57,
                            "Total Ave": "3.38"
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
                              @Parameter(description = "排序类型", example = "Total Price", schema = @Schema(allowableValues = {"Total Price", "DeviceAvg"}))
                              @RequestParam(defaultValue = "price") String sortType,
                              @Parameter(description = "起始时间", example = "1970-1-1 0:0:0")
                              @RequestParam(defaultValue = "1970-1-1 0:0:0") String startTime,
                              @Parameter(description = "截止时间", example = "2999-12-31 23:59:59")
                              @RequestParam(defaultValue = "2999-12-31 23:59:59") String endTime) {
        return logService.outletsPage(pageNum, pageSize, sort, sortType, startTime, endTime);
    }

    @Operation(summary = "修改量或上架量或售出量")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                         "code": 200,
                         "msg": "成功",
                         "data": [
                           0,
                           0,
                           0,
                           1,
                           3,
                           0,
                           0,
                           0,
                           0,
                           0,
                           0,
                           0,
                           4
                         ]
                       }"""))
    })
    @GetMapping("/uploadOrSoldDays")
    public Result uploadOrSoldDays(@Parameter(description = "月或年", example = "month",
            schema = @Schema(allowableValues = {"month", "year"}), required = true)
                                   @RequestParam String choose,
                                   @Parameter(description = "修改或上架或售出", example = "true",
                                           schema = @Schema(allowableValues = {"true", "false"}))
                                   @RequestParam String upOrSold) {
        return logService.uploadOrSoldDays(choose, upOrSold);
    }

}
