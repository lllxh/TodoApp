package com.jason.mdtally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutMeActivity extends AppCompatActivity {
    private static final String TAG = "MdTally-AboutMeActivity";
    private String url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        Intent intent = getIntent();
        url = intent.getStringExtra("Url");
        Log.i(TAG,"Url:"+url);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        if (url.equals("http://blog.catnipball.xyz/about/")){
//            toolbar.setTitle("关于我");
//
//        } else if (url.equals("https://github.com/lllxh")) {
//            toolbar.setTitle("我的Github");
//        }
//        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.colorPrimary_Dark));
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().getJavaScriptEnabled();
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}