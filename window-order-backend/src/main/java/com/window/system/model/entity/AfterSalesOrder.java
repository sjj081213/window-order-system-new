package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AfterSalesOrder {
    @ExcelProperty("ID")
    private Long id;
    
    @ExcelProperty("工单号")
    private String ticketNo;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long orderId;
    
    @ExcelProperty("客户姓名")
    private String customerName;
    
    @ExcelProperty("电话")
    private String customerPhone;
    
    @ExcelProperty("地址")
    private String address;
    
    @ExcelProperty("问题描述")
    private String issueDescription;
    
    @ExcelProperty("状态")
    private String status; // PENDING, ASSIGNED, PROCESSING, COMPLETED, CANCELLED
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long handlerId;
    
    @ExcelProperty("预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;
    
    @ExcelProperty("完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completionTime;
    
    @ExcelProperty("解决方案")
    private String solution;
    
    @ExcelProperty("费用")
    private BigDecimal fee;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long createBy;
    
    @ExcelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Boolean isDeleted;
    
    // VO Fields
    @ExcelProperty("处理人")
    private String handlerName;
    @ExcelProperty("关联订单号")
    private String orderNo; // from WindowOrder
}
