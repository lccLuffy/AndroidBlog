package com.lcc.blog.presenter;

/**
 * Created by lcc_luffy on 2016/3/7.
 */
public interface PostPresenter {
    void getAllPosts(int page);

    void getPostsByUser(int user_id, int page);
}
