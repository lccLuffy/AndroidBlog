package com.lcc.blog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lcc.blog.adapter.PostAdapter;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.bean.PostModel;
import com.lcc.blog.component.AppComponent;
import com.lcc.blog.component.DaggerMainActivityComponent;
import com.lcc.blog.impl.user.UserProfilePresenterImpl;
import com.lcc.blog.module.PostModule;
import com.lcc.blog.mvp.presenter.PostPresenter;
import com.lcc.blog.mvp.presenter.UserProfilePresenter;
import com.lcc.blog.ui.post.CreatePostActivity;
import com.lcc.blog.ui.setting.SettingActivity;
import com.lcc.blog.ui.user.UserCenterActivity;
import com.lcc.blog.ui.user.authentication.LoginActivity;
import com.lcc.blog.ui.user.authentication.RegisterActivity;
import com.lcc.blog.utils.UserManager;
import com.lcc.blog.mvp.view.PostView;
import com.lcc.blog.mvp.view.UserProfileView;
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

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements PostView,UserProfileView{
    @Bind(R.id.stateRecyclerView)
    StateRecyclerView stateRecyclerView;

    PostAdapter postAdapter;
    int currentPage = 1;

    @Inject
    PostPresenter postPresenter;

    UserProfilePresenter userProfilePresenter;
    LoadMoreFooter loadMoreFooter;


    Drawer result;
    AccountHeader accountHeader;
    ProfileDrawerItem profileDrawerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfilePresenter = new UserProfilePresenterImpl(this);
        init();
        getData();
        setupDrawer();
    }

    @Override
    protected void component(AppComponent appComponent) {
        DaggerMainActivityComponent.builder()
                .appComponent(appComponent)
                .postModule(new PostModule(this))
                .build()
                .inject(this);
    }

    private void setupDrawer() {
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(getProfile())
                .withHeaderBackground(R.mipmap.user_info_bg)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if(UserManager.isLogin())
                        {
                            startActivity(UserCenterActivity.newIntent(MainActivity.this, UserManager.getUser().id));
                            return true;
                        }
                        toast("请登录");
                        return false;

                    }
                })
                .build();

        final PrimaryDrawerItem homeItem = new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home);
        final PrimaryDrawerItem settingItem = new PrimaryDrawerItem().withName("Setting").withIcon(FontAwesome.Icon.faw_cog);

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        homeItem,
                        new DividerDrawerItem()
                        )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(drawerItem == settingItem)
                        {
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                            return true;
                        }
                        return false;
                    }
                })
                .build();
        result.addStickyFooterItem(settingItem);
        userProfilePresenter.refreshUserProfile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userProfilePresenter.onDestroy();
    }

    private void init() {
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
            startActivity(new Intent(this,CreatePostActivity.class));
            return true;
        }
        if (id == R.id.action_register) {
            startActivity(new Intent(this,RegisterActivity.class));
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

    @Override
    public void onProfile(ProfileDrawerItem profile) {
        accountHeader.updateProfile(profile);
    }

    @NonNull
    @Override
    public ProfileDrawerItem getProfile() {
        if(profileDrawerItem == null)
            profileDrawerItem = new ProfileDrawerItem();
        return profileDrawerItem;
    }
}
