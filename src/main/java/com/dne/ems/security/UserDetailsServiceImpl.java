package com.dne.ems.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dne.ems.model.UserAccount;
import com.dne.ems.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import java.time.LocalDateTime;

/**
 * Service to load user-specific data.
 * This is used by Spring Security to handle authentication.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    /**
     * Locates the user based on the username. In our case, the username is the email.
     *
     * @param username the username (email) identifying the user whose data is required.
     * @return a fully populated user record (never {@code null})
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("未找到邮箱为 " + username + " 的用户。"));

        if (userAccount.getLockoutEndTime() != null && userAccount.getLockoutEndTime().isAfter(LocalDateTime.now())) {
            throw new LockedException("该账户已被锁定，请稍后再试。");
        }

        return new CustomUserDetails(userAccount);
    }
} 