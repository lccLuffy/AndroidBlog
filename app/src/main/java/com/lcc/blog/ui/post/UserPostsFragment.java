package com.lcc.blog.ui.post;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.lcc.blog.R;
import com.lcc.blog.adapter.PostAdapter;
import com.lcc.blog.base.BaseFragment;
import com.lcc.blog.impl.post.PostPresenterImpl;
import com.lcc.blog.bean.PostModel;
import com.lcc.blog.presenter.PostPresenter;
import com.lcc.blog.view.PostView;
import com.lcc.state_refresh_recyclerview.Recycler.LoadMoreFooter;
import com.lcc.state_refresh_recyclerview.Recycler.StateRecyclerView;

import butterknife.Bind;


public class UserPostsFragment extends BaseFragment implements PostView{

    @Bind(R.id.stateRecyclerView)
    StateRecyclerView stateRecyclerView;

    PostAdapter postAdapter;

    PostPresenter postPresenter;
    private int currentPage = 1;
    private int user_id;
    LoadMoreFooter loadMoreFooter;

    public static UserPostsFragment newInstance(int user_id)
    {
        UserPostsFragment fragment = new UserPostsFragment();
        fragment.user_id = user_id;
        return fragment;
    }
    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        postPresenter = new PostPresenterImpl(this);
        stateRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        stateRecyclerView.setAdapter(postAdapter = new PostAdapter(context),true);
        loadMoreFooter = postAdapter.getLoadMoreFooter();
        stateRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                loadMoreFooter.showLoadMoreView();
                getData();
            }
        });
        loadMoreFooter.setOnLoadMoreListener(new LoadMoreFooter.OnLoadMoreListener() {
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
            loadMoreFooter.showNoMoreView();
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
            loadMoreFooter.showErrorView();
        }
        stateRecyclerView.setRefreshing(false);
    }

    @Override
    public void onEmpty() {
        stateRecyclerView.showEmptyView();
        stateRecyclerView.setRefreshing(false);
    }
}
