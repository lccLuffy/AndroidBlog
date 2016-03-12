package com.lcc.blog.component;

import com.lcc.blog.module.PostModule;

import dagger.Component;

/**
 * Created by lcc_luffy on 2016/3/12.
 */
@ActivityScope
@Component(dependencies = AppComponent.class,modules = PostModule.class)
public interface MainActivityComponent {
}
