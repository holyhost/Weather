package com.zxyoyo.apk.weather;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * 通过js获取到dom，然后隐藏不需要展示的内容
 */

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    private String curUrl = "file:///android_asset/test.html";//当前的url
    private Disposable disposable;

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
        // html代码中可调用本地安卓方法
        webView.addJavascriptInterface(new NativeInterface(this), "AndroidNative");
        webView.loadUrl(curUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // 复制当前的url
            ClipboardManager manager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            manager.setPrimaryClip(ClipData.newPlainText("",curUrl));
            Toast.makeText(this, curUrl, Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideBottom();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.contains("baidu.com")) return;
                super.onLoadResource(view, url);
                Log.e("onLoadResource",url);
            }


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

//                if (url.contains("baidu.com")) return new WebResourceResponse("text/html","utf-8",null);

                Log.e("shouldInterceptRequest",url);

                return super.shouldInterceptRequest(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.i("web-log", "js console log: " + cm.message()); // cm.message()为js日志
                return true;

            }


        });

        // 每隔五秒去执行一下隐藏的操作
        disposable = Observable.interval(5, TimeUnit.SECONDS)
                .subscribe(next -> {
                    Log.i("web-log", "interval-start");
                    hideBottom();
                });


    }

    // 隐藏sudu方法
    private void hideBottom() {
        try {
            //定义javaScript方法
//            String javascript = "javascript:function hideBottom() { "
//                    + "document.getElementsByClassName('sudu')[0].style.display='none'"
//                    + "}";

            String javascript = "javascript:function hideBottom() { "
                    + "console.log('hide bottom');"
                    + "console.log(document.getElementsByClassName('sudu').length);" // 打印数组的长度
                    + "document.getElementsByClassName('ggModel sixd-box-ad')[0].style.display='none';"
                    + "document.getElementsByClassName('sixd-navbar')[0].style.display='none';"
                    + "document.getElementsByClassName('xuan')[0].style.display='none';"
                    + "document.getElementsByClassName('pic_container')[0].style.display='none';"
                    + "document.getElementsByClassName('pic_row')[0].style.display='none';"
                    + "}";
            //加载方法
            webView.loadUrl(javascript);
            //执行方法
            webView.loadUrl("javascript:hideBottom();");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null&&!disposable.isDisposed()){
            //避免页面关闭了，还在发隐藏指令
            disposable.dispose();
        }
    }
}
