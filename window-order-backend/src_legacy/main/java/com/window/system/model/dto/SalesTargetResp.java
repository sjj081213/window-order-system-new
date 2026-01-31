package com.window.system.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.window.system.model.entity.SalesTarget;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class SalesTargetResp extends SalesTarget {
    @ExcelProperty("销售员")
    private String salespersonName;
    @ExcelProperty("实际完成金额")
    private BigDecimal actualAmount;
    @ExcelProperty("完成率(%)")
    private BigDecimal completionRate; // 0-100
}
