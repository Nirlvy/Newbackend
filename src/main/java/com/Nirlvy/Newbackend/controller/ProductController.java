package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.entity.Product;
import com.Nirlvy.Newbackend.service.IProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/alter")
    public boolean alter(@RequestBody Product product) {
        return productService.alter(product);
    }
}
