package com.lcc.blog.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class RetrofitUtil {
    public static final String DOMAIN = "http://115.28.69.91";
    private static Retrofit retrofit;
    private RetrofitUtil(){}
    public static Retrofit getRetrofit()
    {
        if (retrofit == null)
        {
            synchronized (RetrofitUtil.class)
            {
                if(retrofit == null)
                {
                    retrofit =  new Retrofit.Builder()
                            .baseUrl(DOMAIN+"/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
    public static <T> T create(final Class<T> service)
    {
        return getRetrofit().create(service);
    }
}
