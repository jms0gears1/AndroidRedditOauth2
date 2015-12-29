package com.example.cash1.myapplication;

import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by jmsGears1 on 12/26/2015.
 */
public class RedditRestClient {

    public static Observable<AccessToken> getAccessToken(Boolean isRefresh, String code) {
        OkHttpClient okHttpClient = new OkHttpClient();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(
                        chain.request()
                                .newBuilder()
                                .addHeader("Authorization", "Basic " + Base64.encodeToString(Constants.CREDENTIALS.getBytes(), Base64.NO_WRAP))
                                .build());
            }
        });


        okHttpClient.interceptors().add(interceptor);

        Retrofit client = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        RedditToken token = client.create(RedditToken.class);

        return token.obtainAccessToken(
                isRefresh ? Constants.GRANT_TYPE_REFRESH : Constants.GRANT_TYPE_AUTHORIZE,
                code,
                Constants.REDIRECT_URI
        );
    }

    public static Observable<AccessToken> getAccessToken2(Boolean isRefresh, String code){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.e("Oauth", chain.request().toString());
                return chain.proceed(chain.request());
            }
        });
        Retrofit client = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        RedditToken token = client.create(RedditToken.class);

        return token.getObtainAcessToken(new PostData(
                Constants.GRANT_TYPE_AUTHORIZE,
                code,
                Constants.REDIRECT_URI
        ));
    }

    public interface RedditToken {
        @FormUrlEncoded
        @POST("access_token")
        Observable<AccessToken> obtainAccessToken(
                @Field("grant_type") String grantType,
                @Field("code") String code,
                @Field("redirect_uri") String redirectUri
        );

        @POST("access_token")
        Observable<AccessToken> getObtainAcessToken(@Body PostData postData);
    }
}
