package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.SalesTargetResp;
import com.window.system.model.req.SalesTargetReq;
import com.window.system.service.SalesTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-target")
public class SalesTargetController {

    @Autowired
    private SalesTargetService salesTargetService;

    @PostMapping("/set")
    public Result<String> setTarget(@RequestBody SalesTargetReq req) {
        return salesTargetService.setTarget(req);
    }

    @GetMapping("/list")
    public Result<List<SalesTargetResp>> list(@RequestParam(required = false) String month,
                                              @RequestParam(required = false) Long salespersonId) {
        return salesTargetService.list(month, salespersonId);
    }
}
