package com.example.cash1.myapplication;

/**
 * Created by cash 1 on 12/27/2015.
 */
public class PostData {
    String grantType;
    String code;
    String redirectUri;

    public PostData(String grantType, String code, String redirectUri){
        this.grantType = grantType;
        this.code = code;
        this.redirectUri = redirectUri;
    }
}
