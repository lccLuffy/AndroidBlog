package com.lcc.blog.presenter;

import android.support.annotation.NonNull;

import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

/**
 * Created by lcc_luffy on 2016/3/9.
 */
public interface UserProfilePresenter {
    void refreshUserProfile(@NonNull ProfileDrawerItem profileDrawerItem);
}
