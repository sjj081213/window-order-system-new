package com.window.system.controller;

import com.window.system.annotation.Log;
import com.window.system.common.Result;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.RemeasureTask;
import com.window.system.model.req.RemeasureTaskAssignReq;
import com.window.system.model.req.RemeasureTaskListReq;
import com.window.system.model.req.RemeasureTaskSubmitReq;
import com.window.system.security.AuthUser;
import com.window.system.service.RemeasureTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/remeasure")
@CrossOrigin(origins = "*")
@lombok.extern.slf4j.Slf4j
public class RemeasureTaskController {

    @Autowired
    private RemeasureTaskService remeasureTaskService;

    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<PageResponse<RemeasureTask>> list(@RequestBody RemeasureTaskListReq req, @AuthenticationPrincipal AuthUser user) {
        log.info("Query remeasure task list: {}", req);
        req.setCurrentUserId(user.getId());
        req.setCurrentUserRole(user.getRole());
        return remeasureTaskService.list(req);
    }

    @PostMapping("/assign")
    @Log(module = "复尺任务", operation = "指派复尺")
    @PreAuthorize("hasAnyRole('SALES','ADMIN')")
    public Result<String> assign(@RequestBody @Validated RemeasureTaskAssignReq req, @AuthenticationPrincipal AuthUser user) {
        log.info("Assign remeasure task: {}", req);
        return remeasureTaskService.assign(req, user);
    }

    @PostMapping("/submit")
    @Log(module = "复尺任务", operation = "提交复尺结果")
    @PreAuthorize("hasAnyRole('INSTALLER','ADMIN')")
    public Result<String> submit(@RequestBody @Validated RemeasureTaskSubmitReq req, @AuthenticationPrincipal AuthUser user) {
        log.info("Submit remeasure task: {}", req);
        return remeasureTaskService.submit(req, user);
    }
    
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<RemeasureTask> get(@PathVariable Long id, @AuthenticationPrincipal AuthUser user) {
        log.info("Get remeasure task detail id: {}", id);
        return remeasureTaskService.get(id, user);
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<RemeasureTask> getByOrderId(@PathVariable Long orderId, @AuthenticationPrincipal AuthUser user) {
        log.info("Get remeasure task by order id: {}", orderId);
        return remeasureTaskService.getByOrderId(orderId, user);
    }
}
