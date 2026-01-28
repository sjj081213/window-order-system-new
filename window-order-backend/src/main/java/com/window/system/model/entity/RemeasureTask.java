package com.window.system.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RemeasureTask {
    private Long id;

    private Long orderId;

    private Long assigneeId;

    private String status; // PENDING, COMPLETED

    private Double preciseWidth;

    private Double preciseHeight;

    private String sketchUrl;

    private String sitePhotos; // JSON array

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // VO fields
    private String orderNo;
    private String customerName;
    private String address;
    private String assigneeName;
}
