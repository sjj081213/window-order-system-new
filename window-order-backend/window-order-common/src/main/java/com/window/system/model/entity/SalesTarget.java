package com.window.system.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class SalesTarget {
    @ExcelProperty("ID")
    private Long id;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long salespersonId;
    
    @ExcelProperty("月份")
    private String targetMonth; // YYYY-MM
    
    @ExcelProperty("目标金额")
    private BigDecimal targetAmount;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long createBy;
    
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
    
    @com.alibaba.excel.annotation.ExcelIgnore
    private Long updateBy;
    @com.alibaba.excel.annotation.ExcelIgnore
    private LocalDateTime updateTime;
}
