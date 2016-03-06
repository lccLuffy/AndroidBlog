package com.lcc.blog.service.user;

import com.lcc.blog.model.Authentication;
import com.lcc.blog.model.Model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public interface UserService {
    @FormUrlEncoded
    @POST("user/login")
    Call<Model<Authentication>> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/register")
    Call<Model<Authentication>> register(@Field("username") String username,@Field("email") String email, @Field("password") String password);
}
