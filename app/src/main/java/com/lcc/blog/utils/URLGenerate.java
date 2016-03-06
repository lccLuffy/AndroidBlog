package com.lcc.blog.utils;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class URLGenerate {
    public static String postId2Url(int post_id)
    {
        return RetrofitUtil.DOMAIN+"/post/"+post_id;
    }
}
