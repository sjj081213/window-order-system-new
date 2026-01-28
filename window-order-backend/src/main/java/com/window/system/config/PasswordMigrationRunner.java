package com.window.system.config;

import com.window.system.mapper.SysUserMapper;
import com.window.system.model.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordMigrationRunner implements CommandLineRunner {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            List<SysUser> all = sysUserMapper.selectAll();
            for (SysUser u : all) {
                String pwd = u.getPassword();
                if (pwd != null && !pwd.startsWith("$2")) {
                    u.setPassword(passwordEncoder.encode(pwd));
                    sysUserMapper.update(u);
                }
            }
        } catch (Exception ignored) { }
    }
}
