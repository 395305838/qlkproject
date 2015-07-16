/**
 *
 */
package com.xiaocoder.android.fw.general.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xiaocoder.android.fw.general.base.XCBaseFragment;
import com.xiaocoder.android_fw_general.R;


public class XCWebViewFragment extends XCBaseFragment {
    WebView qlk_id_webview;
    String url;
    int progressBarVisible = View.VISIBLE;
    ProgressBar progressBar;
    int scrollMode = -10000;
    String params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return init(inflater, R.layout.xc_l_fragment_webview);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setScrollMode(int scrollMode) {
        this.scrollMode = scrollMode;
    }

    @Override
    public void onClick(View v) {
    }

    @JavascriptInterface
    @Override
    public void initWidgets() {
        qlk_id_webview = getViewById(R.id.xc_id_fragment_webview);
        if(scrollMode != -10000){
            qlk_id_webview.setOverScrollMode(scrollMode);
        }

        progressBar = getViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setVisibility(progressBarVisible);
        WebSettings wSettings = qlk_id_webview.getSettings();
        wSettings.setJavaScriptEnabled(true);
        wSettings.setBuiltInZoomControls(true);
//		wSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        qlk_id_webview.loadUrl(url);

    }

    public void setProgressBarVisible(int visible){

          this.progressBarVisible = visible;

    }

    @Override
    public void listeners() {
        qlk_id_webview.setFocusable(false);
        qlk_id_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                    // 在当前的webview中跳转到新的url
                    view.loadUrl(url);

                // 如果不需要其他对点击链接事件的处理返回true，否则返回false
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setProgress(100);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setProgress(100);
                //drawChart(['4/6','5/6','6/6','7/6','8/6','9/6','10/6'],[],[56, 562, 563, 628, 680, 700, 1500],[50, 550, 568, 652, 670, 699, 1000],'143px',35,20);
//                String params= "['1/6','2/6','3/6','4/6','5/6','6/6','7/6'],[],[56, 262, 363, 428, 580, 700, 1500],[50, 350, 468, 552, 670, 899, 1000],180,35,30";
//                String params= "['07/01','06/30','06/29','06/28','06/27','06/26','06/25'],[],[100,100,100,100,100,100,100],[200,200,200,200,200,200,200],180,35,30";

                if(!TextUtils.isEmpty(params)){
                    view.loadUrl("javascript:drawChart(" + params + ")");
                }


            }
        });
        qlk_id_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

    }

    public void loadJS(String params){
//                  view.loadUrl("javascript:drawChart(" + params + ")");
        qlk_id_webview.loadUrl("javascript:drawChart(" + params + ")");
        this.params = params;
    }


}