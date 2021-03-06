package com.lcc.blog.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.lcc.blog.R;
import com.lcc.blog.MainActivity;
import com.lcc.blog.component.AppComponent;
import com.lcc.blog.config.App;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    protected ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        if(!(this instanceof MainActivity) && actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        component(App.getAppComponent());
    }

    protected void component(AppComponent appComponent){};


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getLayoutId();
}
