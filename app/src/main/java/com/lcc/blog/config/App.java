package com.lcc.blog.config;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lcc.blog.component.AppComponent;
import com.lcc.blog.component.DaggerAppComponent;
import com.lcc.blog.module.AppModule;
import com.lcc.blog.utils.UserManager;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.orhanobut.logger.Logger;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class App extends Application{
    private static App app;
    private static AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        UserManager.init();
        Logger.init("main");
        DrawerImageLoader.init(new ImageLoader());
        appComponent = DaggerAppComponent.builder().appModule(new AppModule()).build();

    }
    public static App getInstance()
    {
        return app;
    }
    private class ImageLoader extends AbstractDrawerImageLoader
    {
        @Override
        public void set(ImageView imageView, Uri uri, Drawable placeholder) {
            Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
        }

        @Override
        public void cancel(ImageView imageView) {
            Glide.clear(imageView);
        }
    }
    public static AppComponent getAppComponent()
    {
        return appComponent;
    }
}
