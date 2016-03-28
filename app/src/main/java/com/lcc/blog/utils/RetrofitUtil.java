package com.lcc.blog.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class RetrofitUtil {
    public static final String DOMAIN = "http://115.28.69.91:10086";
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

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yy-MM-dd HH:mm:ss")
                            .create();

                    OkHttpClient client =
                            new OkHttpClient
                                    .Builder()
                                    .addNetworkInterceptor(new AutInterceptor())
                                    .addInterceptor(loggingInterceptor)
                                    .build();

                    retrofit = new Retrofit
                            .Builder()
                            .baseUrl(DOMAIN + "/api/")
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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
                    .addHeader("Accept","application/vnd.blog.v1+json")
                    .addHeader("Authorization", "Bearer " + UserManager.getToken())
                    .build();
            return chain.proceed(authorizationRequest);
        }
    }
}
