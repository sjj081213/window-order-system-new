package com.window.system.controller;

import com.alibaba.excel.EasyExcel;
import com.window.system.common.Result;
import com.window.system.model.dto.DashboardStats;
import com.window.system.model.entity.WindowOrder;
import com.window.system.mapper.WindowOrderMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.window.system.model.req.OrderListReq;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import com.window.system.security.AuthUser;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private WindowOrderMapper windowOrderMapper;

    @GetMapping("/stats")
    public Result<DashboardStats> getStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser user = (AuthUser) authentication.getPrincipal();
        Long userId = user.getId();
        String role = user.getRole();

        DashboardStats stats = new DashboardStats();
        stats.setPendingOrders(windowOrderMapper.countPendingOrders(userId, role));
        stats.setFinishedOrders(windowOrderMapper.countFinishedOrders(userId, role));
        stats.setTotalOrders(windowOrderMapper.countTotalOrders(userId, role));
        stats.setMonthlySales(windowOrderMapper.sumMonthlySales(userId, role).toString());
        stats.setMonthlyPaidAmount(windowOrderMapper.sumMonthlyPaidAmount(userId, role).toString());
        
        // Charts
        stats.setOrderTrend(windowOrderMapper.getOrderTrend(userId, role));
        stats.setBrandDistribution(windowOrderMapper.getBrandDistribution(userId, role));
        stats.setSalesPerformance(windowOrderMapper.getMonthlySalesPerformance(userId, role));
        
        return Result.success(stats);
    }

    @PostMapping("/export")
    public void export(@RequestBody OrderListReq req, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("订单列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        
        List<WindowOrder> list = windowOrderMapper.exportList(req);
        EasyExcel.write(response.getOutputStream(), WindowOrder.class).sheet("订单").doWrite(list);
    }
}
