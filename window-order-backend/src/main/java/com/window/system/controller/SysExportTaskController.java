package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.entity.SysExportTask;
import com.window.system.service.SysExportTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

@RestController
@RequestMapping("/api/export-task")
@Tag(name = "导出任务管理")
public class SysExportTaskController {

    @Autowired
    private SysExportTaskService sysExportTaskService;

    @PostMapping("/list")
    @Operation(summary = "获取导出任务列表")
    public Result<com.window.system.model.dto.PageResponse<SysExportTask>> list(@RequestBody com.window.system.model.req.ExportTaskListReq req) {
        return Result.success(sysExportTaskService.list(req));
    }
}
