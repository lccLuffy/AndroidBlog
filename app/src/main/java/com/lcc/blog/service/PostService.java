package com.lcc.blog.service;

import com.lcc.blog.bean.Model;
import com.lcc.blog.bean.PostModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public interface PostService {
    @FormUrlEncoded
    @POST("post")
    Call<Model<String>> createPost(@Field("title") String title, @Field("content_markdown") String content_markdown);

    @GET("post")
    Call<PostModel> getAllPosts(@Query("page") int page);

    @GET("{user_id}/posts")
    Call<PostModel> getPostsByUser(@Path("user_id") int user_id, @Query("page") int page);
}
