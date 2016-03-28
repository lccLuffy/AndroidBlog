package com.lcc.blog.mvp.presenter;

/**
 * Created by lcc_luffy on 2016/3/9.
 */
public interface ProfilePresenter {
    void getUser(int user_id);
    void getPostsCount(int user_id);
}
