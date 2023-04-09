package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.Ulogin;
import com.Nirlvy.Newbackend.entity.User;
import com.Nirlvy.Newbackend.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/user")
@Tag(name = "用户接口")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @SecurityRequirement(name = "basicAuth")
    @Operation(summary = "登陆")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登陆成功", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
            }),
            @ApiResponse(responseCode = "1001", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content)
    })
    @PostMapping("/login")
    public Result login(@RequestBody @Parameter(description = "登陆信息") Ulogin ulogin) {
        return userService.login(ulogin);
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public Result register(@RequestBody @Parameter(description = "注册信息") Ulogin ulogin) {
        return userService.register(ulogin);
    }

    @Operation(summary = "更新用户")
    @PostMapping
    public boolean saveUser(@RequestBody @Parameter(description = "用户信息") User user) {
        return userService.sOu(user);
    }

    @Operation(summary = "列出所有用户")
    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }

    @Operation(summary = "删除一个用户")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable @Parameter(description = "用户ID") Integer id) {
        return userService.removeById(id);
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody @Parameter(description = "用户ID列表") List<Integer> ids) {
        return userService.removeBatchByIds(ids);
    }

    @Operation(summary = "分页查询用户")
    @GetMapping("/page")
    public IPage<User> findPage(
            @RequestParam(required = false) @Parameter(description = "用户ID") Integer id,
            @RequestParam(defaultValue = "") @Parameter(description = "用户名称") String userName,
            @RequestParam(defaultValue = "") @Parameter(description = "创建时间") String createTime,
            @RequestParam @Parameter(description = "页码") Integer pageNum,
            @RequestParam @Parameter(description = "每页数量") Integer pageSize) {
        return userService.findPage(id, userName, createTime, pageNum, pageSize);
    }

    @Operation(summary = "导出用户信息")
    @GetMapping("/export")
    public void export(@Parameter(description = "响应对象") HttpServletResponse response) throws Exception {
        userService.export(response);
    }

    @Operation(summary = "导入用户信息")
    @PostMapping("/import")
    public boolean imp(@Parameter(description = "上传的文件") MultipartFile file) throws Exception {
        return userService.imp(file);
    }

}
