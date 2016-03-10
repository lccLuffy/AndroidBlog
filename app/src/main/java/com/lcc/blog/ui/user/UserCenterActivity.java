package com.lcc.blog.ui.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcc.blog.R;
import com.lcc.blog.adapter.UserFragmentAdapter;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.impl.user.ProfilePresenterImpl;
import com.lcc.blog.model.User;
import com.lcc.blog.presenter.ProfilePresenter;
import com.lcc.blog.utils.UserManager;
import com.lcc.blog.view.ProfileView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.Bind;

public class UserCenterActivity extends BaseActivity implements ProfileView {

    @Bind(R.id.username)
    TextView username_tv;

    @Bind(R.id.email)
    TextView email_tv;

    @Bind(R.id.avatar)
    ImageView avatar_iv;

    @Bind(R.id.viewPage)
    ViewPager viewPager;

    @Bind(R.id.viewpagerTab)
    SmartTabLayout viewpagerTab;

    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @Bind(R.id.postsCount)
    TextView postsCount;

    @Bind(R.id.userInfoWrapper)
    LinearLayout linearLayout;

    ProfilePresenter profilePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilePresenter = new ProfilePresenterImpl(this);
        init();

    }

    private void init() {
        actionBar.setDisplayShowTitleEnabled(false);
        int user_id = getIntent().getIntExtra("user_id",0);
        UserFragmentAdapter fragmentAdapter = new UserFragmentAdapter(getSupportFragmentManager(),user_id);
        viewPager.setAdapter(fragmentAdapter);
        viewpagerTab.setViewPager(viewPager);
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListenerHelper());
        profilePresenter.getPostsCount(user_id);
        if(UserManager.isLogin() && UserManager.getUser().id == user_id)
        {
            setupUserInfo(UserManager.getUser());
        }
        else
        {
            profilePresenter.getUser(user_id);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_center;
    }

    private void setupUserInfo(User user) {
        if (user != null) {
            username_tv.setText(user.username);
            email_tv.setText(user.email);
            if (user.avatar != null)
                user.avatar = user.avatar.trim();
            if (!TextUtils.isEmpty(user.avatar)) {
                Glide.with(this).load(user.avatar).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar_iv);
            }
        }

    }

    @NonNull
    public static Intent newIntent(Context context,int user_id)
    {
        Intent intent = new Intent(context,UserCenterActivity.class);
        intent.putExtra("user_id",user_id);
        return intent;
    }

    @Override
    public void onPostsCount(int count) {
        postsCount.setText("文章\n"+count);
    }

    @Override
    public void onUser(User user) {
        setupUserInfo(user);
    }

    @Override
    public void onUserFail(String msg) {
        toast(msg);
    }

    private class OnOffsetChangedListenerHelper implements AppBarLayout.OnOffsetChangedListener
    {
        boolean avatarCanFadeOut = true,avatarCanFadeIn = false;
        boolean hasFadeOut = false,hasFadeIn = true;
        int totalScrollRange;
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            totalScrollRange = appBarLayout.getTotalScrollRange();

            float positiveOffset = -verticalOffset;
            float percent = positiveOffset / totalScrollRange;

            avatarCanFadeOut = percent >= 0.65f;

            avatarCanFadeIn = percent < 0.65f;

            if(!hasFadeOut && avatarCanFadeOut)
            {
                hasFadeOut = true;
                animateOut(linearLayout);
                /*animateOut(username_tv);*/

            }
            else if(!hasFadeIn && avatarCanFadeIn)
            {
                hasFadeIn = true;
                animateIn(linearLayout);
                /*animateIn(username_tv);*/
            }
        }



        private void animateOut(View target)
        {
            target.animate()
                    .scaleX(0)
                    .scaleY(0)
                    .alpha(0)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hasFadeIn = false;
                        }
                    })
                    .start();
        }
        private void animateIn(View target)
        {
            target.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .alpha(1)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hasFadeOut = false;
                        }
                    })
                    .start();
        }
    }
}
