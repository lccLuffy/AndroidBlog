package com.lcc.blog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcc.blog.adapter.PostAdapter;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.impl.post.PostPresenterImpl;
import com.lcc.blog.model.PostModel;
import com.lcc.blog.model.User;
import com.lcc.blog.presenter.PostPresenter;
import com.lcc.blog.ui.post.PostActivity;
import com.lcc.blog.ui.setting.SettingActivity;
import com.lcc.blog.ui.user.UserCenterActivity;
import com.lcc.blog.ui.user.authentication.LoginActivity;
import com.lcc.blog.utils.L;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.utils.UserManager;
import com.lcc.blog.view.PostView;
import com.lcc.state_refresh_recyclerview.Recycler.LoadMoreFooter;
import com.lcc.state_refresh_recyclerview.Recycler.StateRecyclerView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements PostView{
    @Bind(R.id.stateRecyclerView)
    StateRecyclerView stateRecyclerView;

    TextView username_tv;
    TextView email_tv;
    ImageView avatar_iv;

    PostAdapter postAdapter;
    int currentPage = 1;
    PostPresenter postPresenter;
    LoadMoreFooter loadMoreFooter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postPresenter = new PostPresenterImpl(this);
        init();
        getData();
        setupDrawer();
    }
    Drawer result;
    private void setupDrawer() {
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.user_info_bg)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("lcc_luffy")
                                .withEmail("528360256@qq.com")
                                .withIcon("https://github.com/fluidicon.png")
                                /*.withIcon(R.mipmap.ic_avatar)*/
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        startActivity(new Intent(MainActivity.this, UserCenterActivity.class));
                        return true;
                    }
                })
                .build();
        final PrimaryDrawerItem homeItem = new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home);
        PrimaryDrawerItem settingItem = new PrimaryDrawerItem().withName("Setting").withIcon(FontAwesome.Icon.faw_cog);

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        homeItem,
                        new DividerDrawerItem(),
                        settingItem)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(position == 3)
                        {
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                            return true;
                        }
                        return false;
                    }
                })
                .build();
    }

    private void init() {
        /*View header = null;
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
*/
        stateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stateRecyclerView.setAdapter(postAdapter = new PostAdapter(this),false);
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
            loadMoreFooter.showNoMoreView();
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
