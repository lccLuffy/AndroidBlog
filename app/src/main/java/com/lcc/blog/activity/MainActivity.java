package com.lcc.blog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lcc.blog.R;
import com.lcc.blog.adapter.TestAdapter;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.model.Model;
import com.lcc.blog.model.Tag;
import com.lcc.blog.service.BlogService;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.utils.UserManager;
import com.lcc.state_refresh_recyclerview.Recycler.StateRecyclerView;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    @Bind(R.id.stateRecyclerView)
    StateRecyclerView stateRecyclerView;

    TestAdapter testAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stateRecyclerView.setAdapter(testAdapter = new TestAdapter(this));
        stateRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        testAdapter.showNoMoreView();
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

        if (id == R.id.action_settings) {
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
    private void getData()
    {
        BlogService blogService = RetrofitUtil.create(BlogService.class);
        blogService.getTags().enqueue(new Callback<Model<List<Tag>>>() {
            @Override
            public void onResponse(Call<Model<List<Tag>>> call, Response<Model<List<Tag>>> response) {
                if(response.isSuccess())
                {
                    testAdapter.initData(response.body().results);
                }
                stateRecyclerView.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Model<List<Tag>>> call, Throwable t) {
                stateRecyclerView.setRefreshing(false);
            }
        });
    }
}
