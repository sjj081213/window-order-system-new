package com.window.system.model.dto;

import com.window.system.model.entity.SalesTarget;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class SalesTargetResp extends SalesTarget {
    private String salespersonName;
    private BigDecimal actualAmount;
    private BigDecimal completionRate; // 0-100
}
