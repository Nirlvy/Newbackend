package com.Nirlvy.Newbackend.controller;

import com.Nirlvy.Newbackend.common.Result;
import com.Nirlvy.Newbackend.entity.Freezer;
import com.Nirlvy.Newbackend.service.IFreezerService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

    @PostMapping("/add")
    public Result add(@RequestBody Freezer freezer) {
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

}
