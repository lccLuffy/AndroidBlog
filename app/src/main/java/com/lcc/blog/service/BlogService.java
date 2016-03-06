package com.lcc.blog.service;

import com.lcc.blog.model.Model;
import com.lcc.blog.model.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public interface BlogService {
    @GET("tag")
    Call<Model<List<Tag>>> getTags();

    @FormUrlEncoded
    @POST("post")
    Call<Model<String>> createPost(@Field("title") String title, @Field("content_markdown") String content_markdown, @Query("token") String token);
}
