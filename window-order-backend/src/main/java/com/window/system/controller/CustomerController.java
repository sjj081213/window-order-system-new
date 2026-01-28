package com.window.system.controller;

import com.window.system.common.Result;
import com.window.system.mapper.CustomerMapper;
import com.window.system.model.dto.PageResponse;
import com.window.system.model.entity.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@Tag(name = "Customer Management")
public class CustomerController {

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping("/list")
    @Operation(summary = "List customers")
    public Result<PageResponse<Customer>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        long count = customerMapper.countList(name, phone);
        if (count == 0) {
            return Result.success(PageResponse.of(Collections.emptyList(), 0L));
        }
        
        int startIndex = (pageNo - 1) * pageSize;
        List<Customer> list = customerMapper.selectList(name, phone, startIndex, pageSize);
        return Result.success(PageResponse.of(list, count));
    }
    
    @GetMapping("/detail/{id}")
    @Operation(summary = "Get customer detail")
    public Result<Customer> getDetail(@PathVariable Long id) {
        return Result.success(customerMapper.getById(id));
    }
}
