package com.lcc.blog.impl.user.authentication;

import com.lcc.blog.model.Authentication;
import com.lcc.blog.model.Model;
import com.lcc.blog.presenter.RegisterPresenter;
import com.lcc.blog.service.user.UserService;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.utils.UserManager;
import com.lcc.blog.ui.user.authentication.RegisterView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class RegisterPresenterImpl implements RegisterPresenter {

    private RegisterView registerView;
    private UserService userService;
    public RegisterPresenterImpl(RegisterView registerView) {
        this.registerView = registerView;
    }

    @Override
    public void register(String username, String email, String password) {
        if(userService == null)
        {
            userService = RetrofitUtil.create(UserService.class);
        }
        Call<Model<Authentication>> authenticationCall = userService.register(
                username,
                email,
                password
        );
        authenticationCall.enqueue(new Callback<Model<Authentication>>() {
            @Override
            public void onResponse(Call<Model<Authentication>> call, Response<Model<Authentication>> response) {
                Model<Authentication> model = response.body();
                if(response.isSuccess() && model != null)
                {
                    if(!model.error)
                    {
                        UserManager.saveAuthentication(model.results);
                        registerView.onSuccess();
                    }
                    else
                    {
                        registerView.onFail("注册失败,"+model.message);
                    }
                }
                else
                {
                    registerView.onFail("注册失败");
                }
            }

            @Override
            public void onFailure(Call<Model<Authentication>> call, Throwable t) {
                registerView.onFail("登录失败,"+t.toString());
            }
        });
    }
}
