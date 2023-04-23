package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.Freezer;
import com.Nirlvy.Newbackend.service.IFreezerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    public Result add(@RequestBody(content = @Content(examples = {
            @ExampleObject(
                    name = "全部参数",
                    value = """
                            {
                               "id": "HB165",
                               "temp": 0,
                               "enable": true,
                               "charge": true,
                               "location": "string",
                               "seLocation": "string",
                               "coordinates": "string",
                               "img": "string",
                               "deployTime": "2023-04-22T07:50:25.667Z",
                               "supplyTime": "2023-04-22T07:50:25.667Z"
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

    @DeleteMapping("/remove/batch")
    public boolean remove(@RequestBody List<Integer> ids) {
        return freezerService.removeBatchByIds(ids);
    }

    @PostMapping("/alter")
    public boolean alter(@RequestBody Freezer freezer) {
        return freezerService.updateById(freezer);
    }

    @GetMapping("/list")
    public List<Freezer> list() {
        return freezerService.list();
    }

    @GetMapping("/locationList")
    public Result locationList() {
        return freezerService.locationList();
    }

}
