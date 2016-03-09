package com.lcc.blog.impl.user;

import android.support.annotation.NonNull;

import com.lcc.blog.R;
import com.lcc.blog.presenter.UserProfilePresenter;
import com.lcc.blog.ui.user.UserProfileView;
import com.lcc.blog.utils.UserManager;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

/**
 * Created by lcc_luffy on 2016/3/9.
 */
public class UserProfilePresenterImpl implements UserProfilePresenter
{
    private UserProfileView profileView;
    public UserProfilePresenterImpl(UserProfileView profileView) {
        this.profileView = profileView;
    }

    @Override
    public void refreshUserProfile(@NonNull ProfileDrawerItem profileDrawerItem) {
        if(UserManager.isLogin())
        {
            profileDrawerItem
                    .withName(UserManager.getUser().username)
                    .withEmail(UserManager.getUser().email);
            String avatar = UserManager.getUser().avatar;
            if(avatar != null && !avatar.trim().equals(""))
            {
                profileDrawerItem.withIcon(avatar);
            }
            else
            {
                profileDrawerItem.withIcon(R.mipmap.ic_avatar);
            }
        }
        else
        {
            profileDrawerItem = new ProfileDrawerItem()
                    .withName("游客")
                    .withIcon(R.mipmap.ic_avatar);
        }
        profileView.onProfile(profileDrawerItem);
    }
}
