package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.FeedBack;
import com.Nirlvy.Newbackend.service.IFeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-08
 */
@RestController
@RequestMapping("/feedBack")
public class FeedBackController {

    private final IFeedBackService feedBackService;

    @Autowired
    public FeedBackController(IFeedBackService feedBackService) {
        this.feedBackService = feedBackService;
    }

    @Operation(summary = "增加")
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
                                "content": "测试",
                                "device": "HB15156",
                                "img": "imgBase64",
                                "contact": "A先生",
                                "phoneNum": 123456789,
                                "time": "2023-04-29 12:12:12"
                              }"""
            ),
            @ExampleObject(
                    name = "最少参数",
                    value = """
                            {
                                "content": "测试",
                                "device": "HB15156",
                                "time": "2023-04-29 12:12:12"
                            }"""
            )})) FeedBack feedBack) {
        return feedBackService.add(feedBack);
    }

    @Operation(summary = "列出")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "code": 200,
                      "msg": "成功",
                      "data": [
                        {
                          "id": 1,
                          "content": "测试",
                          "device": "HB15156",
                          "img": null,
                          "contact": null,
                          "phoneNum": null,
                          "time": "2023-04-29 12:12:12",
                          "isRead": false
                        }
                      ]
                    }"""))
    })
    @GetMapping("/list")
    public Result getList(@RequestParam(required = false) @Parameter(description = "设备号") String device) {
        return feedBackService.getList(device);
    }

    @Operation(summary = "修改")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                         "code": 200,
                         "msg": "成功",
                         "data": null
                     }"""))
    })
    @PostMapping("/alter")
    public Result alter(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(
                    value = """
                            {
                                "content": "测试",
                                "device": "HB15156",
                                "img": "imgBase64",
                                "contact": "A先生",
                                "phoneNum": 123456789,
                                "time": "2023-04-29 12:12:12"
                              }"""
            )})) FeedBack feedBack) {
        return feedBackService.alter(feedBack);
    }

}
