package com.lcc.blog.utils;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class RetrofitUtil {
    public static final String DOMAIN = "http://115.28.69.91";
    private static Retrofit retrofit;

    private RetrofitUtil() {
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitUtil.class)
            {
                if (retrofit == null)
                {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client =
                            new OkHttpClient
                                    .Builder()
                                    .addNetworkInterceptor(new AutInterceptor())
                                    .addInterceptor(loggingInterceptor)
                                    .build();
                    retrofit = new Retrofit
                            .Builder()
                            .baseUrl(DOMAIN + "/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
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

    private static class AutInterceptor implements Interceptor
    {

        @Override
        public Response intercept(Chain chain) throws IOException
        {
            Request originRequest = chain.request();
            if (!UserManager.isLogin())
            {
                return chain.proceed(originRequest);
            }
            Request authorizationRequest = originRequest
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + UserManager.getToken())
                    .build();
            return chain.proceed(authorizationRequest);
        }
    }
}
