package com.window.system.model.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemeasureTaskListReq extends BasePageReq {
    private String orderNo;
    private Long orderId;
    private Long assigneeId;
    private String status;
    
    private Long currentUserId;
    private String currentUserRole;
}
