package com.example.rssreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class NewsDetails extends AppCompatActivity {
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        webview = (WebView) findViewById(R.id.webview);
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_UP;
            }
        });
        Bundle bundle = getIntent().getExtras();
        webview.loadUrl(bundle.getString("Link"));

    }
}
