package com.lcc.blog.impl.user;

import com.lcc.blog.bean.Model;
import com.lcc.blog.bean.User;
import com.lcc.blog.mvp.presenter.ProfilePresenter;
import com.lcc.blog.service.user.UserProfileService;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.mvp.view.ProfileView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lcc_luffy on 2016/3/9.
 */
public class ProfilePresenterImpl implements ProfilePresenter{
    ProfileView profileView;

    public ProfilePresenterImpl(ProfileView profileView) {
        this.profileView = profileView;
    }
    UserProfileService userProfileService;
    private UserProfileService getService()
    {
        if(userProfileService == null)
            userProfileService = RetrofitUtil.create(UserProfileService.class);
        return userProfileService;
    }

    @Override
    public void getUser(final int user_id) {
        getService().getUserProfile(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Model<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        profileView.onUserFail(e.getMessage());
                    }

                    @Override
                    public void onNext(Model<User> userModel) {
                        profileView.onUser(userModel.results);
                    }
                });

    }

    @Override
    public void getPostsCount(int user_id) {
        getService().getPostsCount(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Model<Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        profileView.onUserFail(e.toString());
                        profileView.onPostsCount(0);
                    }

                    @Override
                    public void onNext(Model<Integer> integerModel) {
                        profileView.onPostsCount(integerModel.results);
                    }
                });

    }
}
