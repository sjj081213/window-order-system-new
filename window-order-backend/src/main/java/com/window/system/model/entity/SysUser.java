package com.window.system.model.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class SysUser {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;
    private LocalDateTime createTime;
    private Long createBy;
    private Long updateBy;
    
    private Boolean isDeleted;
    
    // Joined field
    private String roleName;
}
