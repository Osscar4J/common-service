package com.zhao.commonservice.utils;

import com.zhao.common.entity.UserInfo;

/**
 * 用户信息工具类
 * @Author: zhaolianqi
 * @Date: 2021/1/28 10:58
 * @Version: v1.0
 */
public class UserInfoUtils {

    private static ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>();

    private UserInfoUtils(){}

    public static UserInfo getCurrentUser(){
        return userInfoThreadLocal.get();
    }

    public static void setCurrentUser(UserInfo user){
        userInfoThreadLocal.set(user);
    }

}
