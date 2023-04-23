package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.Product;
import com.Nirlvy.Newbackend.entity.UpdateProductDTO;
import com.Nirlvy.Newbackend.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/product")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "修改一个商品")
    @ApiResponse(responseCode = "200", description = "成功")
    @PostMapping("/update")
    public Result update(@RequestBody UpdateProductDTO updateProductDTO) {
        return productService.updateData(updateProductDTO);
    }

    @Operation(summary = "增加一个商品")
    @ApiResponse(responseCode = "200", description = "成功", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                         "code": 200,
                         "msg": "成功",
                         "data": null
                     }"""))
    })
    @PostMapping("/add")
    public Result add(@RequestBody(content = @Content(examples = {@ExampleObject(
            value = """
                    {
                        "count": 10,
                        "price": 5,
                        "name": "美汁源果粒橙橘子味300ml",
                        "type": "美汁源果粒橙中瓶系列",
                        "seType": "果汁",
                        "device": "HB15153"
                    }"""
    )})) Product product) {
        return productService.add(product);
    }

}
