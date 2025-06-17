package com.dne.ems.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.UserStatus;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UserAccount userAccount;

    public CustomUserDetails(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Long getId() {
        return userAccount.getId();
    }
    
    // ========== 新增的 Getters，暴露 UserAccount 的完整信息 ==========

    public String getName() {
        return userAccount.getName();
    }

    public Role getRole() {
        return userAccount.getRole();
    }

    public String getPhone() {
        return userAccount.getPhone();
    }

    public UserStatus getStatus() {
        return userAccount.getStatus();
    }

    public String getRegion() {
        return userAccount.getRegion();
    }
    
    public List<String> getSkills() {
        return userAccount.getSkills();
    }

    // ========== Spring Security 标准接口实现 ==========

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + getRole().name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return userAccount.getPassword();
    }

    @Override
    public String getUsername() {
        // 我们使用邮箱作为认证用户名
        return userAccount.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 可以根据 lockoutEndTime 字段实现更复杂的锁定逻辑
        return userAccount.getLockoutEndTime() == null;
    }



    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userAccount.isEnabled() && userAccount.getStatus() == UserStatus.ACTIVE;
    }
}