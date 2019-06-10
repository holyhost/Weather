package com.zxyoyo.apk.weather;

import android.util.Log;
import android.webkit.WebView;

/**
 * webview 处理的一些工具
 * 通过js获取dom的方式隐藏界面
 */
public class WebUtils {

    private static int funIndex = 0;
    public static void hideHtmlView(WebView webView,String cssClassName){
        try {
            //定义javaScript方法
            String stringNodeList = "var nodes = document.getElementsByClassName('"+cssClassName+"');";
            String stringCount = "var count = nodes.length;";
            String stringFor = "for(var i=0;i<count;i++){ nodes[i].style.display='none';}";
            String funNanme = "jsFun"+(funIndex++);
            String javascript = "javascript:function "
                    + funNanme
                    +"() { "
                    + "console.log('hide "
                    + funNanme
                    +"');"
                    +stringNodeList
                    +stringCount
                    +stringFor
                    + "}";
            //加载方法
            Log.i("javascript",javascript);
            webView.loadUrl(javascript);
            //执行方法
            webView.loadUrl("javascript:" +
                    funNanme +
                    "();");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeHtmlNode(WebView webView,String nodeName){
        String funNanme = "jsFun"+nodeName+(funIndex++);
        String stringCount = "$('"+nodeName+"').remove();";
        String javascript = "javascript:function "
                + funNanme
                +"() { "
                + "console.log('hide "
                + funNanme
                +"');"
                +stringCount
                + "}";
        //加载方法
        Log.i("javascript",javascript);
        webView.loadUrl(javascript);
        //执行方法
        webView.loadUrl("javascript:" +
                funNanme +
                "();");
    }
}
