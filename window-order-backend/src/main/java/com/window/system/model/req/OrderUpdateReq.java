package com.window.system.model.req;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderUpdateReq {
    private Long id;
    private String customerName;
    private String customerPhone;
    private String address;
    private String brand;
    private String windowType;
    private String color;
    private String glassSpec;
    private Double width;
    private Double height;
    private Boolean isRemeasured;
    private BigDecimal price;
    private LocalDateTime orderTime;
    private LocalDateTime installTime;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledInstallDate;
    
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualInstallEndDate;
    private String installProgress;
    private String productionProgress;
    private String status;
    private Long salespersonId;
    private Long installerId;
    
    // Auth fields
    private Long currentUserId;
    private String currentUserRole;
    
    // Address structured fields
    private String regionCodes;
    private String province;
    private String city;
    private String district;
    private String detailAddress;


    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
