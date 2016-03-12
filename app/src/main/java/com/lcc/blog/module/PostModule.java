package com.lcc.blog.module;

import com.lcc.blog.component.ActivityScope;
import com.lcc.blog.impl.post.PostPresenterImpl;
import com.lcc.blog.presenter.PostPresenter;
import com.lcc.blog.view.PostView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lcc_luffy on 2016/3/12.
 */
@Module
public class PostModule {
    private PostView postView;

    public PostModule(PostView postView) {
        this.postView = postView;
    }

    @Provides
    @ActivityScope
    public PostPresenter providePostPresenter()
    {
        return new PostPresenterImpl(postView);
    }
}
