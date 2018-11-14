package com.crack.lcz.myglance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Fragment1 extends Fragment {

    private WebView webView;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment1, container, false);
        webView = (WebView)view.findViewById(R.id.mywebView);
        //return LayoutInflater.from(getActivity()).inflate(R.layout.fragment1, container, false);
        return view;
    }

    @Override
    public void onStart() {

        super.onStart();

        setWebView();
    }


    private void setWebView(){
        webView.loadUrl("http://www.badmintoncn.com/");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候是控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器打开
                view.loadUrl(url);
                return true;
            }
            //WebViewClient帮助WebView去处理一些页面控制和请求通知
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载



    }

}
