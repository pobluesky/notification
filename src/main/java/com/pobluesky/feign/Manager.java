package com.pobluesky.feign;

import com.pobluesky.global.entity.Department;
import com.pobluesky.global.security.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Manager {
    private Long userId;
    protected String name;
    protected String email;
    protected String password;
    protected String phone;
    protected Boolean isActivated;
    protected UserRole role;
    private String empNo;
    private Department department;
    private String securityRole;

}
