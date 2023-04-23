package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.FeedBack;
import com.Nirlvy.Newbackend.service.IFeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @PostMapping("/add")
    public Result add(@RequestBody @Parameter(description = "feedBack实体") FeedBack feedBack) {
        return feedBackService.add(feedBack);
    }

    @Operation(summary = "列出")
    @GetMapping("/list")
    public Result getList(@RequestParam(required = false) @Parameter(description = "设备号") String device) {
        return feedBackService.getList(device);
    }

    @Operation(summary = "修改")
    @PostMapping("/alter")
    public Result alter(@RequestBody @Parameter(description = "feedBack实体") FeedBack feedBack) {
        return feedBackService.alter(feedBack);
    }

}
