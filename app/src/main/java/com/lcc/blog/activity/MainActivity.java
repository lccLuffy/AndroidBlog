package com.lcc.blog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcc.blog.R;
import com.lcc.blog.adapter.PostAdapter;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.impl.PostPresenterImpl;
import com.lcc.blog.model.PostModel;
import com.lcc.blog.model.User;
import com.lcc.blog.presenter.PostPresenter;
import com.lcc.blog.utils.L;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.utils.UserManager;
import com.lcc.blog.view.PostView;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;
import com.lcc.state_refresh_recyclerview.Recycler.StateRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements PostView{
    @Bind(R.id.stateRecyclerView)
    StateRecyclerView stateRecyclerView;

    @Bind(R.id.drawLayout)
    DrawerLayout drawerLayout;

    @Bind(R.id.navigationView)
    NavigationView navigationView;

    TextView username_tv;
    TextView email_tv;
    ImageView avatar_iv;

    PostAdapter postAdapter;
    int currentPage = 1;
    PostPresenter postPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postPresenter = new PostPresenterImpl(this);
        init();
        getData();

    }

    private void init() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        View header = navigationView.getHeaderView(0);
        username_tv = (TextView) header.findViewById(R.id.username);
        email_tv = (TextView) header.findViewById(R.id.email);
        avatar_iv = (ImageView) header.findViewById(R.id.avatar);
        avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UserCenterActivity.class));
            }
        });

        setupUserInfo(UserManager.getUser());

        stateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stateRecyclerView.setAdapter(postAdapter = new PostAdapter(this),false);
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

    private void check()
    {
        OkHttpUtils.get()
                .addHeader("Authorization", "Bearer " + UserManager.getToken())
                .url(RetrofitUtil.DOMAIN+"/api/user/checkToken")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        L.i(e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        L.json(response);
                    }
                });
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
            /*startActivity(new Intent(this,RegisterActivity.class));*/
            toast("check");
            check();
            return true;
        }
        if (id == R.id.action_logout) {
            if(UserManager.logout()) {
                toast("已经退出登录");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getData()
    {
        postPresenter.getAllPosts(currentPage);
    }

    private void setupUserInfo(User user)
    {
        if(user != null)
        {
            username_tv.setText(user.username);
            email_tv.setText(user.email);
            if(user.avatar != null)
                user.avatar = user.avatar.trim();
            if(!TextUtils.isEmpty(user.avatar))
            {
                Glide.with(this).load(user.avatar).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar_iv);
            }
        }

    }

    @Override
    public void onRefresh(PostModel postModel)
    {
        postAdapter.initData(postModel.results);
        stateRecyclerView.setRefreshing(false);
    }

    @Override
    public void onLoadMore(PostModel postModel, boolean noMoreData) {
        if(noMoreData)
            postAdapter.showNoMoreView();
        else
        {
            postAdapter.addData(postModel.results);
        }
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
