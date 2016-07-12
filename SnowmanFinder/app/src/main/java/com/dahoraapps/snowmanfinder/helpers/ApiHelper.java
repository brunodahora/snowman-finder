package com.dahoraapps.snowmanfinder.helpers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {

    public static final int READ_TIMEOUT = 300;
    public static final int CONNECT_TIMEOUT = 120;

    private static Retrofit retrofit;
    private static ApiInterface api;
    private static Gson gson;

    public static ApiInterface getApi() {
        if (api == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.newbies.aplicativo.info/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
            api = retrofit.create(ApiInterface.class);
        }

        return api;
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
        }
        return gson;
    }

}

