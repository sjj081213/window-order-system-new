package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class WindowOrder {
    @ExcelProperty("ID")
    private Long id;
    
    @ExcelProperty("订单号")
    private String orderNo;
    
    @ExcelProperty("客户名")
    private String customerName;
    
    @ExcelProperty("电话")
    private String customerPhone;
    
    @ExcelIgnore
    private Long customerId;
    
    @ExcelProperty("安装地址")
    private String address; // Display Address (Full)
    
    @ExcelProperty("品牌")
    private String brand;
    
    @ExcelProperty("窗型")
    private String windowType;

    @ExcelProperty("颜色")
    private String color;

    @ExcelProperty("玻璃规格")
    private String glassSpec;

    @ExcelProperty("宽(mm)")
    private Double width;
    
    @ExcelProperty("高(mm)")
    private Double height;
    
    @ExcelIgnore
    private Boolean isRemeasured;
    
    @ExcelProperty("价格")
    private BigDecimal price;

    @ExcelProperty("已付金额")
    private BigDecimal paidAmount;

    @ExcelProperty("支付状态")
    private String paymentStatus;
    
    @ExcelProperty("下单时间")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;
    
    @ExcelProperty("安装时间")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime installTime;
    
    @ExcelProperty("预约安装日期")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledInstallDate;
    
    @ExcelProperty("实际安装完成日期")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualInstallEndDate;
    
    @ExcelProperty("安装进度")
    private String installProgress;
    
    @ExcelProperty("制作进度")
    private String productionProgress;

    @ExcelProperty("物流状态")
    private String logisticsStatus;
    
    @ExcelProperty("状态")
    private String status; // DRAFT, SUBMITTED
    
    @ExcelIgnore
    private Long salespersonId;
    
    @ExcelIgnore
    private Long installerId;
    
    @ExcelProperty("创建时间")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @ExcelIgnore
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    // Structured Address
    @ExcelIgnore
    private String regionCodes; 
    @ExcelIgnore
    private String province;
    @ExcelIgnore
    private String city;
    @ExcelIgnore
    private String district;
    @ExcelIgnore
    private String detailAddress;

    // Vo fields (joined)
    @ExcelProperty("销售员")
    private String salespersonName;
    
    @ExcelProperty("安装师傅")
    private String installerName;
    
    // Audit fields
    @ExcelIgnore
    private Long createBy;
    @ExcelIgnore
    private Long updateBy;
    @ExcelIgnore
    private Boolean isDeleted;
}
