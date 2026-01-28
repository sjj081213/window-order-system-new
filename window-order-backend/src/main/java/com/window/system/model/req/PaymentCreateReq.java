package com.window.system.model.req;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PaymentCreateReq {
    private Long orderId;
    private BigDecimal amount;
    private String payMethod;
    private String remark;
    private LocalDateTime payTime;
    private List<String> attachments;
    
    // Auth fields
    private Long currentUserId;
    private String currentUserRole;
    
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
