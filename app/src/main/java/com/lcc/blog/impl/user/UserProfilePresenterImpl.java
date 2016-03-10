package com.lcc.blog.impl.user;

import com.lcc.blog.R;
import com.lcc.blog.presenter.UserProfilePresenter;
import com.lcc.blog.view.UserProfileView;
import com.lcc.blog.utils.UserManager;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by lcc_luffy on 2016/3/9.
 */
public class UserProfilePresenterImpl implements UserProfilePresenter
{
    private UserProfileView profileView;
    public UserProfilePresenterImpl(UserProfileView profileView) {
        this.profileView = profileView;
        EventBus.getDefault().register(this);
    }

    @Override
    public void refreshUserProfile()
    {
        ProfileDrawerItem profileDrawerItem = profileView.getProfile();
        if(UserManager.isLogin())
        {
            profileDrawerItem
                    .withName(UserManager.getUser().username)
                    .withEmail(UserManager.getUser().email);
            String avatar = "https://assets-cdn.github.com/favicon.ico";
            profileDrawerItem.withIcon(avatar);
        }
        else
        {
            profileDrawerItem
                    .withName("游客")
                    .withEmail(null)
                    .withIcon(R.mipmap.ic_avatar);
        }
        profileView.onProfile(profileDrawerItem);
    }

    public static class Message
    {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(Message message)
    {
        refreshUserProfile();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
