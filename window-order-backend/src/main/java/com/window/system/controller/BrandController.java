package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.Brand;
import com.window.system.model.req.brand.BrandListReq;
import com.window.system.model.req.brand.BrandSaveReq;
import com.window.system.service.BrandService;
import com.window.system.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
@CrossOrigin(origins = "*")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<PageResponse<Brand>> list(@RequestBody BrandListReq req) {
        return brandService.list(req);
    }
    
    @GetMapping("/all")
    public Result<List<Brand>> listAll() {
        return brandService.listAll();
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Log(module = "品牌", operation = "保存品牌")
    public Result<String> save(@RequestBody @Validated BrandSaveReq req) {
        return brandService.save(req);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Log(module = "品牌", operation = "删除品牌")
    public Result<String> delete(@PathVariable Long id) {
        return brandService.delete(id);
    }
}
