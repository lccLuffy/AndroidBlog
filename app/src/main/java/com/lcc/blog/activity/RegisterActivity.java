package com.lcc.blog.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.lcc.blog.R;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.model.Authentication;
import com.lcc.blog.model.Model;
import com.lcc.blog.service.user.UserService;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.utils.UserManager;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BaseActivity{

    @Bind(R.id.email)
    AutoCompleteTextView mEmailView;

    @Bind(R.id.username)
    EditText username_et;

    @Bind(R.id.password)
    EditText mPasswordView;

    @Bind(R.id.password_confirm)
    EditText password_confirm_View;

    @Bind(R.id.login_progress)
    View mProgressView;

    @Bind(R.id.login_form)
    View mLoginFormView;

    @Bind(R.id.email_sign_in_button)
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attampRegister();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attampRegister() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String username = username_et.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            mEmailView.setError("邮箱不能为空");
            mEmailView.requestFocus();
            return;
        }


        if(TextUtils.isEmpty(password))
        {
            mPasswordView.setError("密码不能为空");
            mPasswordView.requestFocus();
            return;
        }


        if(!password.equals(password_confirm_View.getText().toString().trim()))
        {
            mPasswordView.setError("两次密码不一致");
            mPasswordView.requestFocus();
            password_confirm_View.setError("两次密码不一致");
            return;
        }

        if(TextUtils.isEmpty(username))
        {
            username_et.setError("用户名不能为空");
            username_et.requestFocus();
            return;
        }

        if(!isEmailValid(email))
        {
            mEmailView.setError("邮箱不正确");
            mEmailView.requestFocus();
            return;
        }
        if(!isPasswordValid(email))
        {
            mPasswordView.setError("密码太短");
            mPasswordView.requestFocus();
            return;
        }
        if(!isUsernameValid(username))
        {
            username_et.setError("用户名长度为3-16");
            username_et.requestFocus();
            return;
        }
        showProgress(true);
        register(username,email,password);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        int l = password.length();
        return l >= 6;
    }
    private boolean isUsernameValid(String username) {
        int l = username.length();
        return l >= 3 && l<=16;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public void register(String username, String email, String password)
    {
        UserService userService = RetrofitUtil.create(UserService.class);
        Call<Model<Authentication>> authenticationCall = userService.register(
                username,
                email,
                password
        );
        authenticationCall.enqueue(new Callback<Model<Authentication>>() {
            @Override
            public void onResponse(Call<Model<Authentication>> call, Response<Model<Authentication>> response) {
                showProgress(false);
                Model<Authentication> model = response.body();
                if(response.isSuccess() && model != null)
                {
                    if(!model.error)
                    {
                        toast("注册成功");
                        UserManager.saveAuthentication(model.results);
                    }
                    else
                    {
                        toast("注册失败,"+model.message);
                    }
                }
                else
                {
                    toast("注册失败");
                }
            }

            @Override
            public void onFailure(Call<Model<Authentication>> call, Throwable t) {
                toast("注册失败");
                showProgress(false);
            }
        });
    }

}

