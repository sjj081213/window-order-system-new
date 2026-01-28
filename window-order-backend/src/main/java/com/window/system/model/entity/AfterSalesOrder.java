package com.window.system.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AfterSalesOrder {
    private Long id;
    
    private String ticketNo;
    
    private Long orderId;
    
    private String customerName;
    
    private String customerPhone;
    
    private String address;
    
    private String issueDescription;
    
    private String status; // PENDING, ASSIGNED, PROCESSING, COMPLETED, CANCELLED
    
    private Long handlerId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completionTime;
    
    private String solution;
    
    private BigDecimal fee;
    
    private Long createBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    private Boolean isDeleted;
    
    // VO Fields
    private String handlerName;
    private String orderNo; // from WindowOrder
}
