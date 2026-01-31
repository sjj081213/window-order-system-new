package com.window.system.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class SysRole {
    private Long id;
    private String roleCode;
    private String roleName;
    private LocalDateTime createTime;
}
