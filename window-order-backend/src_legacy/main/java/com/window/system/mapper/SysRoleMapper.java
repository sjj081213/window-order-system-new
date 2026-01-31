package com.window.system.mapper;

import com.window.system.model.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    
    @Select("SELECT * FROM sys_role ORDER BY create_time ASC")
    List<SysRole> selectAll();
    
    @Select("SELECT * FROM sys_role WHERE role_code = #{roleCode}")
    SysRole findByCode(String roleCode);
}
