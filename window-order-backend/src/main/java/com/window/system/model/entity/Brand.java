package com.window.system.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Brand {
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private Long createBy;
    private Long updateBy;
    private Boolean isDeleted;
}
