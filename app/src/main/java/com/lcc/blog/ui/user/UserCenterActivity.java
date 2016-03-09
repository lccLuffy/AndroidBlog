package com.lcc.blog.ui.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.lcc.blog.model.User;
import com.lcc.blog.utils.UserManager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.Bind;

public class UserCenterActivity extends BaseActivity {

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

    @Bind(R.id.userInfoWrapper)
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        actionBar.setDisplayShowTitleEnabled(false);
        setupUserInfo(UserManager.getUser());
        UserFragmentAdapter fragmentAdapter = new UserFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        viewpagerTab.setViewPager(viewPager);
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListenerHelper());
    }


    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX",300);
        intent.putExtra("outputY",300);

        intent.putExtra("return-data", true);

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

    private class OnOffsetChangedListenerHelper implements AppBarLayout.OnOffsetChangedListener
    {
        boolean avatarCanFadeOut = true,avatarCanFadeIn = false;
        boolean hasFadeOut = false,hasFadeIn = true;
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            int totalScrollRange = appBarLayout.getTotalScrollRange();

            float positiveOffset = -verticalOffset;
            float percent = positiveOffset / totalScrollRange;

            avatarCanFadeOut = percent >= 0.65f;

            avatarCanFadeIn = percent < 0.65f;


            if(!hasFadeOut && avatarCanFadeOut)
            {
                hasFadeOut = true;
                animateOut(avatar_iv);
                animateOut(username_tv);

            }
            else if(!hasFadeIn && avatarCanFadeIn)
            {
                hasFadeIn = true;
                animateIn(avatar_iv);
                animateIn(username_tv);
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
