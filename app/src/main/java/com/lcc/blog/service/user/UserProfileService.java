package com.lcc.blog.service.user;

import com.lcc.blog.model.Model;
import com.lcc.blog.model.User;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lcc_luffy on 2016/3/9.
 */
public interface UserProfileService {
    @GET("user/{user_id}/postsCount")
    Observable<Model<Integer>> getPostsCount(@Path("user_id") int user_id);

    @GET("user/{user_id}")
    Observable<Model<User>> getUserProfile(@Path("user_id") int user_id);
}
