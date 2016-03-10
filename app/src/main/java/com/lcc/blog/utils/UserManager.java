package com.lcc.blog.utils;

import com.lcc.blog.impl.user.UserProfilePresenterImpl;
import com.lcc.blog.model.Authentication;
import com.lcc.blog.model.User;

import org.greenrobot.eventbus.EventBus;

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
        if(!isLogin())
            return false;
        authentication = null;
        boolean result = PrfUtil.start().remove(KEY).commit();
        EventBus.getDefault().post(new UserProfilePresenterImpl.Message());
        return result;
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
