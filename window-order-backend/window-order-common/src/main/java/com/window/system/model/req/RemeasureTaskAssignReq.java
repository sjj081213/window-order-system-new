package com.window.system.model.req;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class RemeasureTaskAssignReq {
    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotNull(message = "Assignee ID cannot be null")
    private Long assigneeId;
    
    private String remark;
}
