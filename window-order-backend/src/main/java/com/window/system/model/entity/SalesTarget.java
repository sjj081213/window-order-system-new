package com.window.system.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class SalesTarget {
    private Long id;
    private Long salespersonId;
    private String targetMonth; // YYYY-MM
    private BigDecimal targetAmount;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
}
