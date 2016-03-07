package com.lcc.blog.utils;

import com.lcc.blog.model.Authentication;
import com.lcc.blog.model.User;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class UserManager {
    private static final String KEY = "USER";
    private static Authentication authentication;
    public static void init()
    {
        authentication = Json.fromJson(PrfUtil.get().getString(KEY,null),Authentication.class);
    }

    public static void saveAuthentication(Authentication authentication)
    {
        UserManager.authentication = authentication;
        if(authentication != null)
        {
            PrfUtil.start().putString(KEY,Json.toJson(authentication)).commit();
        }
    }

    public static boolean isLogin()
    {
        return authentication != null && authentication.token != null && getUser() != null;
    }

    public static boolean logout()
    {
        authentication = null;
        return PrfUtil.start().remove(KEY).commit();
    }

    public static User getUser()
    {
        return authentication == null ? null : authentication.user;
    }
    public static String getToken()
    {
        return authentication == null ? null : authentication.token;
    }
}
