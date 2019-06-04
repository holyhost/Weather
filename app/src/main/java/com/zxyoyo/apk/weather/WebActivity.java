package com.zxyoyo.apk.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    private String curUrl = "file:///android_asset/test.html";//当前的url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.web_view);
        if(getIntent()!=null){
            String extraUrl = getIntent().getStringExtra("url");
            if(!TextUtils.isEmpty(extraUrl)) curUrl = extraUrl;
        }
        initWebView();
        // WebView 注入即可
        webView.addJavascriptInterface(new NativeInterface(this), "AndroidNative");
        webView.loadUrl(curUrl);
    }

    private void initWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    // 创建要注入的 Java 类
    public class NativeInterface {

        private Context mContext;

        public NativeInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void hello() {
            Toast.makeText(mContext, "hello", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void hello(String params) {
            Toast.makeText(mContext, params, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public String getAndroid() {
            Toast.makeText(mContext, "getAndroid", Toast.LENGTH_SHORT).show();
            return "Android data";
        }

    }
}
