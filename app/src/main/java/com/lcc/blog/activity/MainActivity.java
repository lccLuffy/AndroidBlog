package com.lcc.blog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.lcc.blog.R;
import com.lcc.blog.adapter.PostAdapter;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.model.PostModel;
import com.lcc.blog.service.user.PostService;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.utils.UserManager;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;
import com.lcc.state_refresh_recyclerview.Recycler.StateRecyclerView;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    @Bind(R.id.stateRecyclerView)
    StateRecyclerView stateRecyclerView;

    PostAdapter postAdapter;
    int currentPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stateRecyclerView.setAdapter(postAdapter = new PostAdapter(this));
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_login) {
            if(UserManager.isLogin())
            {
                toast(UserManager.getUser().username+",您已经登陆过了");
                return  true;
            }
            startActivity(new Intent(this,LoginActivity.class));
            return true;
        }
        if (id == R.id.action_post) {
            startActivity(new Intent(this,PostActivity.class));
            return true;
        }
        if (id == R.id.action_register) {
            startActivity(new Intent(this,RegisterActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    PostService postService;
    private void getData()
    {
        if(postService == null)
        {
            postService = RetrofitUtil.create(PostService.class);
        }
        postService.getPosts(currentPage).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.isSuccess())
                {
                    if(currentPage == 1)
                    {
                        postAdapter.initData(response.body().results);
                    }
                    else
                    {
                        if(response.body().results.isEmpty())
                        {
                            postAdapter.showNoMoreView();
                        }
                        else
                        {
                            postAdapter.addData(response.body().results);
                        }
                    }
                }
                stateRecyclerView.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                stateRecyclerView.setRefreshing(false);
            }
        });
    }
}
