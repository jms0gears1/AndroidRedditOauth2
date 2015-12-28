package com.example.cash1.myapplication;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.net.URI;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tvSuccess) TextView success;

    Dialog authDialog;
    WebView webView;
    String authCode;
    String DEVICE_ID = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSignIn)
    public void signIn(){
        authDialog = new Dialog(MainActivity.this);
        authDialog.setContentView(R.layout.auth_dialog);

        String url =Constants.BASE_URL + Constants.OAUTH_URL
                .replace("{client_id}", Constants.CLIENT_ID)
                .replace("{redirect_uri}", Constants.REDIRECT_URI)
                .replace("{duration}",Constants.DURATION)
                .replace("{scope}", Constants.SCOPE);

        webView = (WebView) authDialog.findViewById(R.id.wvOauth);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if(url.contains("?code=") || url.contains("&code=")){
                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    RedditRestClient.getAccessToken(false, authCode)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(new Subscriber<AccessToken>() {
                                @Override
                                public void onCompleted() {
                                    if(!isUnsubscribed())
                                        unsubscribe();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("getAccessToken", e.toString());
                                }

                                @Override
                                public void onNext(AccessToken accessToken) {
                                    success.setText(
                                            "access_token: " + accessToken.access_token
                                                    +"\n token_type: " + accessToken.token_type

                                    );
                                }
                            });
                    success.setText(authCode);
                    authDialog.dismiss();
                }
            }
        });

        authDialog.show();
        authDialog.setTitle("Authorize");
        authDialog.setCancelable(true);
    }

    public void makeSnackbar(String message){
        Snackbar.make(findViewById(R.id.main_activity), message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

/*Snackbar.make(findViewById(R.id.main_activity), "tryina sign in huh?", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/