package com.lcc.blog.config;

import android.app.Application;

import com.lcc.blog.utils.UserManager;
import com.orhanobut.logger.Logger;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class App extends Application{
    private static App app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        UserManager.init();
        Logger.init("main");
    }
    public static App getInstance()
    {
        return app;
    }
}
