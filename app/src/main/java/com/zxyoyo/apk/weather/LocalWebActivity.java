package com.zxyoyo.apk.weather;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import io.reactivex.disposables.Disposable;

public class LocalWebActivity extends AppCompatActivity {
    private WebView webView;

    private String curUrl = "file:///android_asset/weather.html";//当前的url
    private Disposable disposable;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = findViewById(R.id.web_view);
        if(getIntent()!=null){
            String extraUrl = getIntent().getStringExtra("url");
            if(!TextUtils.isEmpty(extraUrl)) curUrl = extraUrl;
        }
        webView.getSettings().setJavaScriptEnabled(true);
        // html代码中可调用本地安卓方法
        webView.addJavascriptInterface(new NativeInterface(this), "AndroidNative");
        webView.loadUrl(curUrl);
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
