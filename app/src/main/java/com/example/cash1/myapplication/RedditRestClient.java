package com.example.cash1.myapplication;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by cash 1 on 12/26/2015.
 */
public class RedditRestClient {

    public interface RedditToken{
        @POST("access_token")
        Observable<AccessToken> obtainAccessToken(
                @Query("grant_type") String grantType,
                @Query("code") String code,
                @Query("redirect_uri") String redirectUri
        );

        @POST("access_token")
        Observable<AccessToken> getObtainAcessToken(@Body PostData postData);
    }

    public static Observable<AccessToken> getAccessToken(Boolean isRefresh, String code){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.e("Oauth", chain.request().headers().toString());
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

        return token.obtainAccessToken(
                isRefresh?Constants.GRANT_TYPE_REFRESH:Constants.GRANT_TYPE_AUTHORIZE,
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
}
