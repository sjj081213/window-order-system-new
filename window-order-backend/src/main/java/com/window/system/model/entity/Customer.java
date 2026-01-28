package com.window.system.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Customer {
    private Long id;
    
    private String name;
    
    private String phone;
    
    private String address;
    
    private String remark;
    
    private Long createBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    private Boolean isDeleted;
    
    // VO Fields
    private Long orderCount;
    private java.math.BigDecimal totalSpent;
}
