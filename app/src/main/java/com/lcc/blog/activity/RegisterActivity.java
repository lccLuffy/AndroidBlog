package com.lcc.blog.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.lcc.blog.R;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.iml.RegisterPresenterImp;
import com.lcc.blog.presenter.RegisterPresenter;
import com.lcc.blog.view.RegisterView;

import butterknife.Bind;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BaseActivity implements RegisterView{

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

    RegisterPresenter registerPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerPresenter = new RegisterPresenterImp(this);

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
        if(!isEmailValid(email))
        {
            mEmailView.setError("邮箱不正确");
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

        if(!isPasswordValid(email))
        {
            mPasswordView.setError("密码太短");
            mPasswordView.requestFocus();
            return;
        }



        if(TextUtils.isEmpty(username))
        {
            username_et.setError("用户名不能为空");
            username_et.requestFocus();
            return;
        }

        if(!isUsernameValid(username))
        {
            username_et.setError("用户名长度为3-16");
            username_et.requestFocus();
            return;
        }
        showProgress(true);
        registerPresenter.register(username,email,password);
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
    @Override
    public void onSuccess() {
        showProgress(false);
        toast("注册成功");
    }

    @Override
    public void onFail(String msg) {
        showProgress(false);
        toast(msg);
    }
}