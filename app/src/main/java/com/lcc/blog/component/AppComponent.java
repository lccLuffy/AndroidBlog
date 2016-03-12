package com.lcc.blog.component;

import com.lcc.blog.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lcc_luffy on 2016/3/12.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
}
