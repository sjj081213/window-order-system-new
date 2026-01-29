package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.SysOperationLog;
import com.window.system.model.req.LogListReq;
import com.window.system.mapper.SysOperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
@lombok.extern.slf4j.Slf4j
public class SysOperationLogController {

    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;

    @PostMapping("/list")
    public Result<PageResponse<SysOperationLog>> list(@RequestBody LogListReq req) {
        log.info("Query sys operation logs: {}", req);
        long total = sysOperationLogMapper.countList(req);
        List<SysOperationLog> list = sysOperationLogMapper.selectList(req);
        return Result.success(PageResponse.of(list, total));
    }

    @Autowired
    private com.window.system.service.SysExportTaskService sysExportTaskService;

    @PostMapping("/export")
    public Result<String> export(@RequestBody LogListReq req) {
        String params = cn.hutool.json.JSONUtil.toJsonStr(req);
        String timeStr = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(java.time.LocalDateTime.now());
        sysExportTaskService.createTask("导出日志_" + timeStr + ".xlsx", "LOG", params);
        
        return Result.success("导出任务已创建，请前往【导出中心】查看进度");
    }
}
