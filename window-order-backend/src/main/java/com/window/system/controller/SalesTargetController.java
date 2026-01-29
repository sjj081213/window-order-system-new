package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.SalesTargetResp;
import com.window.system.model.req.SalesTargetReq;
import com.window.system.service.SalesTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.window.system.model.req.SalesTargetListReq;

@RestController
@RequestMapping("/api/sales-target")
public class SalesTargetController {

    @Autowired
    private SalesTargetService salesTargetService;
    
    @Autowired
    private com.window.system.service.SysExportTaskService sysExportTaskService;

    @PostMapping("/set")
    public Result<String> setTarget(@RequestBody SalesTargetReq req) {
        return salesTargetService.setTarget(req);
    }

    @GetMapping("/list")
    public Result<List<SalesTargetResp>> list(@RequestParam(required = false) String month,
                                              @RequestParam(required = false) Long salespersonId) {
        return salesTargetService.list(month, salespersonId);
    }

    @PostMapping("/export")
    public Result<String> export(@RequestBody SalesTargetListReq req) {
        String params = cn.hutool.json.JSONUtil.toJsonStr(req);
        String timeStr = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(java.time.LocalDateTime.now());
        sysExportTaskService.createTask("导出销售目标_" + timeStr + ".xlsx", "SALES_TARGET", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }
}
