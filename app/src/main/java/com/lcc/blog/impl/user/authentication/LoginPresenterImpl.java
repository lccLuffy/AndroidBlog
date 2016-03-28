package com.lcc.blog.impl.user.authentication;

import com.lcc.blog.impl.user.UserProfilePresenterImpl;
import com.lcc.blog.bean.Authentication;
import com.lcc.blog.bean.Model;
import com.lcc.blog.mvp.presenter.LoginPresenter;
import com.lcc.blog.service.user.UserService;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.utils.UserManager;
import com.lcc.blog.mvp.view.LoginView;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class LoginPresenterImpl implements LoginPresenter {

    private LoginView loginView;
    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void login(String email, String password) {
        UserService userService = RetrofitUtil.create(UserService.class);
        Call<Model<Authentication>> tokenCall = userService.login(email,password);
        tokenCall.enqueue(new Callback<Model<Authentication>>() {
            @Override
            public void onResponse(Call<Model<Authentication>> call, Response<Model<Authentication>> response) {
                Model<Authentication> model = response.body();
                if(response.isSuccess() && model != null)
                {
                    if(!model.error)
                    {
                        UserManager.saveAuthentication(model.results);
                        EventBus.getDefault().post(new UserProfilePresenterImpl.Message());
                        loginView.onSuccess();
                    }
                    else
                    {
                        loginView.onFail("登录失败,"+model.message);
                    }
                }
                else
                {
                    loginView.onFail("登录失败");
                }
            }

            @Override
            public void onFailure(Call<Model<Authentication>> call, Throwable t) {
                loginView.onFail("登录失败,"+t.toString());
            }
        });
    }
}
