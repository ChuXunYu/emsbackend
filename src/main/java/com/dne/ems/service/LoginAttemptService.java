package com.dne.ems.service;

public interface LoginAttemptService {

    /**
     * 当用户登录成功时调用，清除失败尝试记录
     * @param key 通常是用户名或IP地址
     */
    void loginSucceeded(String key);

    /**
     * 当用户登录失败时调用，增加失败尝试次数
     * @param key 通常是用户名或IP地址
     */
    void loginFailed(String key);

    /**
     * 检查用户是否因为失败次数过多而被锁定
     * @param key 通常是用户名或IP地址
     * @return 如果被锁定，则返回true
     */
    boolean isBlocked(String key);
} 