package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.model.dto.LoginResp;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.SysUser;
import com.window.system.model.req.user.UserListReq;
import com.window.system.model.req.user.UserSaveReq;
import com.window.system.service.SysUserService;
import com.window.system.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result<LoginResp> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return sysUserService.login(username, password);
    }

    @GetMapping("/migrate-passwords")
    public Result<String> migratePasswords() {
        return sysUserService.migratePasswords();
    }

    @GetMapping("/me")
    public Result<Map<String, Object>> me() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> info = new java.util.HashMap<>();
        if (auth != null) {
            info.put("principal", auth.getPrincipal());
            info.put("authorities", auth.getAuthorities());
        }
        return Result.success(info);
    }

    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<PageResponse<SysUser>> list(@RequestBody UserListReq req) {
        return sysUserService.list(req);
    }
    
    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('SALES','ADMIN','INSTALLER')")
    public Result<List<SysUser>> listByRole(@PathVariable String role) {
        return sysUserService.listByRole(role);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Log(module = "账号", operation = "保存账号")
    public Result<String> save(@RequestBody @Validated UserSaveReq req) {
        return sysUserService.save(req);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Log(module = "账号", operation = "删除账号")
    public Result<String> delete(@PathVariable Long id) {
        return sysUserService.delete(id);
    }
}
