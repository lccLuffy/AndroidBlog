package com.lcc.blog.mvp.view;

import com.lcc.blog.bean.User;

/**
 * Created by lcc_luffy on 2016/3/9.
 */
public interface ProfileView {
    void onPostsCount(int count);
    void onUser(User user);
    void onUserFail(String msg);
}
