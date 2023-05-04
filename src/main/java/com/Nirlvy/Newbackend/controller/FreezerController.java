package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.Freezer;
import com.Nirlvy.Newbackend.service.IFreezerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/freezer")
public class FreezerController {

    private final IFreezerService freezerService;

    @Autowired
    public FreezerController(IFreezerService freezerService) {
        this.freezerService = freezerService;
    }

    @Operation(summary = "增加一个冰柜")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                         "code": 200,
                         "msg": "成功",
                         "data": null
                     }"""))
    })
    @PostMapping("/add")
    public Result add(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(
                    name = "全部参数",
                    value = """
                            {
                               "id": "HB165",
                               "temp": 0,
                               "enable": true,
                               "charge": true,
                               "location": "A校",
                               "seLocation": "C区",
                               "coordinates": "1,2",
                               "img": "img",
                               "deployTime": "2023-04-22 07:50:25",
                               "supplyTime": "2023-04-22 07:50:25"
                             }"""
            ),
            @ExampleObject(
                    name = "最少参数",
                    value = """
                            {
                               "id": "HB165"
                             }""")})) Freezer freezer) {
        return freezerService.add(freezer);
    }

    @Operation(summary = "批量删除冰柜")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                         "code": 200,
                         "msg": "成功",
                         "data": null
                     }"""))
    })
    @DeleteMapping("/remove/batch")
    public Result remove(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(
                    value = "HB1,HB2"

            )})) List<Integer> ids) {
        return freezerService.removeBatch(ids);
    }

    @Operation(summary = "根据Id修改冰柜")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "true"))
    })
    @PostMapping("/alter")
    public boolean alter(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(
                    value = """
                            {
                               "id": "HB165",
                               "temp": 0,
                               "enable": true,
                               "charge": true,
                               "location": "A",
                               "seLocation": "B",
                               "coordinates": "string",
                               "img": "string",
                               "deployTime": "2023-04-22 07:50:25",
                               "supplyTime": "2023-04-22 07:50:25"
                             }"""
            )})) Freezer freezer) {
        return freezerService.updateById(freezer);
    }

    @Operation(summary = "列出冰柜信息")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    [
                      {
                        "id": "HB114",
                        "temp": 0,
                        "enable": false,
                        "charge": false,
                        "location": null,
                        "seLocation": null,
                        "coordinates": null,
                        "img": null,
                        "deployTime": null,
                        "supplyTime": null
                      },
                      {
                        "id": "HB115",
                        "temp": 0,
                        "enable": false,
                        "charge": false,
                        "location": "A校",
                        "seLocation": "一教",
                        "coordinates": "100,500",
                        "img": null,
                        "deployTime": "2023-04-07 10:10:10",
                        "supplyTime": "2023-04-07 10:10:10"
                      },
                      {
                        "id": "HB116",
                        "temp": 0,
                        "enable": false,
                        "charge": false,
                        "location": "A校",
                        "seLocation": "二教",
                        "coordinates": "100,500",
                        "img": null,
                        "deployTime": "2023-04-07 10:10:10",
                        "supplyTime": "2023-04-07 10:10:10"
                      },
                      {
                        "id": "HB117",
                        "temp": 0,
                        "enable": false,
                        "charge": false,
                        "location": "A校",
                        "seLocation": "三教",
                        "coordinates": "100,500",
                        "img": null,
                        "deployTime": "2023-04-07 10:10:10",
                        "supplyTime": "2023-04-07 10:10:10"
                      },
                      {
                        "id": "HB118",
                        "temp": 0,
                        "enable": false,
                        "charge": false,
                        "location": "A校",
                        "seLocation": "四教",
                        "coordinates": "100,500",
                        "img": null,
                        "deployTime": "2023-04-07 10:10:10",
                        "supplyTime": "2023-04-07 10:10:10"
                      },
                      {
                        "id": "HB15153",
                        "temp": 0,
                        "enable": false,
                        "charge": false,
                        "location": "B校",
                        "seLocation": "一教",
                        "coordinates": "600,900",
                        "img": null,
                        "deployTime": "2023-04-07 10:10:10",
                        "supplyTime": "2023-04-07 10:10:10"
                      },
                      {
                        "id": "HB15154",
                        "temp": 0,
                        "enable": false,
                        "charge": false,
                        "location": "B校",
                        "seLocation": "二教",
                        "coordinates": "600,900",
                        "img": null,
                        "deployTime": "2023-04-07 10:10:10",
                        "supplyTime": "2023-04-07 10:10:10"
                      },
                      {
                        "id": "HB15155",
                        "temp": 0,
                        "enable": false,
                        "charge": false,
                        "location": "B校",
                        "seLocation": "三教",
                        "coordinates": "600,900",
                        "img": null,
                        "deployTime": "2023-04-07 10:10:10",
                        "supplyTime": "2023-04-07 10:10:10"
                      },
                      {
                        "id": "HB15156",
                        "temp": 0,
                        "enable": false,
                        "charge": false,
                        "location": "B校",
                        "seLocation": "四教",
                        "coordinates": "600,900",
                        "img": null,
                        "deployTime": "2023-04-07 10:10:10",
                        "supplyTime": "2023-04-07 10:10:10"
                      },
                      {
                        "id": "HB165",
                        "temp": 0,
                        "enable": true,
                        "charge": true,
                        "location": "A",
                        "seLocation": "B",
                        "coordinates": "string",
                        "img": "string",
                        "deployTime": "2023-04-22 07:50:25",
                        "supplyTime": "2023-04-22 07:50:25"
                      }
                    ]"""))
    })
    @GetMapping("/list")
    public List<Freezer> list() {
        return freezerService.list();
    }

    @Operation(summary = "列出所有位置")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                    "code": 200,
                    "msg": "成功",
                    "data": {
                    "A校": [
                    "一教",
                    "二教",
                    "三教",
                    "四教"
                    ],
                    "B校": [
                    "一教",
                    "二教",
                    "三教",
                    "四教"
                    ]
                    }
                    }"""))
    })
    @GetMapping("/locationList")
    public Result locationList() {
        return freezerService.locationList();
    }

}
