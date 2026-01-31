package com.window.system.api;

import com.window.system.common.Result;
import com.window.system.model.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "window-order-auth", path = "/api/auth")
public interface UserClient {

    @GetMapping("/{id}")
    Result<SysUser> getUserById(@PathVariable("id") Long id);

    @GetMapping("/role/{role}")
    Result<List<SysUser>> listByRole(@PathVariable("role") String role);
    
    @GetMapping("/ids")
    Result<Map<Long, SysUser>> getUsersByIds(@RequestParam("ids") List<Long> ids);
}
