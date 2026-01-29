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

    @GetMapping("/list")
    @Operation(summary = "获取导出任务列表")
    public Result<List<SysExportTask>> list() {
        return sysExportTaskService.list();
    }
    
    @PostMapping("/create-test")
    @Operation(summary = "创建测试导出任务")
    public Result<String> createTestTask() {
        sysExportTaskService.createTask("测试导出任务_" + System.currentTimeMillis(), "TEST", null);
        return Result.success("任务已提交，请在导出中心查看进度");
    }
}
