package com.example.cash1.myapplication;

/**
 * Created by cash 1 on 12/26/2015.
 */
public class Constants {

    final static String BASE_URL = "https://www.reddit.com/api/v1/";
    final static String OAUTH_URL = "authorize?client_id={client_id}&response_type=code&state=TEST&redirect_uri={redirect_uri}&duration={duration}&scope={scope}";
    final static String CLIENT_ID = "xx";
    final static String REDIRECT_URI = "http://localhost";
    final static String GRANT_TYPE_REFRESH="refresh_token";
    final static String GRANT_TYPE_AUTHORIZE="authorization_code";
    final static String DURATION = "permanent";
    final static String SCOPE = "read";
}
