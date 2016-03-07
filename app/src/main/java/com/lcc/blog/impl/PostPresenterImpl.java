package com.lcc.blog.impl;

import com.lcc.blog.model.PostModel;
import com.lcc.blog.presenter.PostPresenter;
import com.lcc.blog.service.PostService;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.view.PostView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/7.
 */
public class PostPresenterImpl implements PostPresenter{
    private PostView postView;
    private PostService postService;
    public PostPresenterImpl(PostView postView) {
        this.postView = postView;
    }

    @Override
    public void getAllPosts(final int page)
    {
        if(postService == null)
        {
            postService = RetrofitUtil.create(PostService.class);
        }
        postService.getAllPosts(page).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                logic(page,response);
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                postView.onFail(t.toString());
            }
        });
    }

    @Override
    public void getPostsByUser(final int page, int user_id)
    {
        if(postService == null)
        {
            postService = RetrofitUtil.create(PostService.class);
        }
        postService.getPostsByUser(page,user_id).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                logic(page,response);
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                postView.onFail(t.toString());
            }
        });
    }

    private void logic(int page,Response<PostModel> response)
    {
        if(response.isSuccess())
        {
            PostModel postModel = response.body();

            if(postModel.results.isEmpty())
            {
                if(page == 1)
                {
                    postView.onEmpty();
                }
                else
                {
                    postView.onLoadMore(postModel,true);
                }
            }
            else
            {
                if(page == 1)
                {
                    postView.onRefresh(postModel);
                }
                else
                {
                    postView.onLoadMore(postModel,false);
                }
            }
        }
        else
        {
            postView.onFail(response.message());
        }
    }
}
