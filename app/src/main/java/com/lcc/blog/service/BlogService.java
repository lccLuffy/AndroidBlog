package com.lcc.blog.service;

import com.lcc.blog.bean.Model;
import com.lcc.blog.bean.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public interface BlogService {
    @GET("tag")
    Call<Model<List<Tag>>> getTags();
}
