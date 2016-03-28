package com.lcc.blog.mvp.view;

import com.lcc.blog.bean.PostModel;

/**
 * Created by lcc_luffy on 2016/3/7.
 */
public interface PostView {
    void onRefresh(PostModel postModel);

    void onLoadMore(PostModel postModel, boolean noMoreData);

    void onFail(String msg);

    void onEmpty();
}
