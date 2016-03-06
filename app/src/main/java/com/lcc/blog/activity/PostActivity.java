package com.lcc.blog.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lcc.blog.R;
import com.lcc.blog.base.BaseActivity;
import com.lcc.blog.model.Model;
import com.lcc.blog.service.BlogService;
import com.lcc.blog.utils.RetrofitUtil;
import com.lcc.blog.utils.UserManager;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends BaseActivity {

    @Bind(R.id.title)
    EditText tv_title;

    @Bind(R.id.content_markdown)
    EditText tv_content_markdown;

    @Bind(R.id.submit)
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post;
    }

    private void submit() {
        if(!UserManager.isLogin())
        {
            toast("未登陆");
            return;
        }
        String title = tv_title.getText().toString().trim();
        String content_markdown = tv_content_markdown.getText().toString().trim();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content_markdown))
        {
            toast("不能为空");
            return;
        }
        BlogService blogService = RetrofitUtil.create(BlogService.class);
        blogService.createPost(title,content_markdown,UserManager.getToken()).enqueue(new Callback<Model<String>>() {
            @Override
            public void onResponse(Call<Model<String>> call, Response<Model<String>> response) {
                if(response.isSuccess())
                {
                    Model<String> model = response.body();
                    if(model.error)
                    {
                        toast("发布失败,"+model.message);
                    }
                    else
                    {
                        toast("发布成功");
                    }
                }
                else
                {
                    toast("发布失败,"+response.message());
                }
            }

            @Override
            public void onFailure(Call<Model<String>> call, Throwable t) {
                toast("发布失败");
            }
        });
    }

}
