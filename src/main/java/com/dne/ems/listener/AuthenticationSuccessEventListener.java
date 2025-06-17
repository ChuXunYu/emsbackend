package com.dne.ems.listener;

import com.dne.ems.model.UserAccount;
import com.dne.ems.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            UserAccount user = userAccountRepository.findByEmail(username).orElse(null);

            if (user != null && user.getFailedLoginAttempts() > 0) {
                user.setFailedLoginAttempts(0);
                user.setLockoutEndTime(null);
                userAccountRepository.save(user);
            }
        }
    }
} 