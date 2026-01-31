package com.window.system.model.req;

import com.window.system.model.req.BasePageReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AfterSalesListReq extends BasePageReq {
    private String orderNo;
    private String ticketNo;
    private String customerName;
    private String customerPhone;
    private String status;
    private Long handlerId;
    
    // Auth
    private Long currentUserId;
    private String currentUserRole;
}
