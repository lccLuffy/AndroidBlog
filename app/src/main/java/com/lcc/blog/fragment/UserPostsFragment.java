package com.lcc.blog.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.lcc.blog.R;
import com.lcc.blog.adapter.PostAdapter;
import com.lcc.blog.base.BaseFragment;
import com.lcc.blog.impl.PostPresenterImpl;
import com.lcc.blog.model.PostModel;
import com.lcc.blog.presenter.PostPresenter;
import com.lcc.blog.utils.UserManager;
import com.lcc.blog.view.PostView;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;
import com.lcc.state_refresh_recyclerview.Recycler.StateRecyclerView;

import butterknife.Bind;


public class UserPostsFragment extends BaseFragment implements PostView{

    @Bind(R.id.stateRecyclerView)
    StateRecyclerView stateRecyclerView;

    PostAdapter postAdapter;

    PostPresenter postPresenter;
    private int currentPage = 1;
    private int user_id;
    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        if(UserManager.isLogin())
            user_id = UserManager.getUser().id;
        postPresenter = new PostPresenterImpl(this);
        stateRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        stateRecyclerView.setAdapter(postAdapter = new PostAdapter(context),false);
        stateRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                postAdapter.showLoadMoreView();
                getData();
            }
        });
        postAdapter.setOnLoadMoreListener(new NiceAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                ++currentPage;
                getData();
            }
        });
        getData();
    }

    private void getData() {
        postPresenter.getPostsByUser(user_id, currentPage);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_post;
    }

    @Override
    public void onRefresh(PostModel postModel) {
        postAdapter.initData(postModel.results);
        stateRecyclerView.setRefreshing(false);
    }

    @Override
    public void onLoadMore(PostModel postModel, boolean noMoreData) {
        if(noMoreData)
            postAdapter.showNoMoreView();
        else
            postAdapter.addData(postModel.results);
        stateRecyclerView.setRefreshing(false);
    }

    @Override
    public void onFail(String msg) {
        if(postAdapter.isDataEmpty())
        {
            stateRecyclerView.showErrorView(msg);
        }
        else
        {
            postAdapter.showNoMoreView();
        }
        stateRecyclerView.setRefreshing(false);
    }

    @Override
    public void onEmpty() {
        stateRecyclerView.showEmptyView();
        stateRecyclerView.setRefreshing(false);
    }
}
