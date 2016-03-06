package com.lcc.blog.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public abstract class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }


    private Toast toast;
    public void toast(CharSequence msg)
    {
        if (toast == null)
        {
            toast = Toast.makeText(this,msg,Toast.LENGTH_LONG);
        }
        toast.setText(msg);
        toast.show();
    }
    protected abstract int getLayoutId();
}
