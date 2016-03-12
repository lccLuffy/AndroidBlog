package com.lcc.blog.utils;

import android.support.annotation.NonNull;

import com.lcc.blog.impl.user.UserProfilePresenterImpl;
import com.lcc.blog.bean.Authentication;
import com.lcc.blog.bean.User;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class UserManager {
    private static final String KEY = "USER";
    private static Authentication authentication;
    private static final User GUEST;

    static
    {
        GUEST = new User();
        GUEST.avatar=null;
        GUEST.username = "Guest";
        GUEST.email="email@example.com";
        GUEST.id=0;
    }
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

    @NonNull
    public static User getUser()
    {
        return authentication == null ? GUEST : authentication.user;
    }
    public static String getToken()
    {
        return authentication == null ? null : authentication.token;
    }
}
