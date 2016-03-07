package com.lcc.blog.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcc.blog.R;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.model.User;
import com.lcc.blog.utils.UserManager;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setupUserInfo(UserManager.getUser());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_center;
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
}
