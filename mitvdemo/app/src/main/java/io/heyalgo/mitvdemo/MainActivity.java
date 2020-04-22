package io.heyalgo.mitvdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient.CustomViewCallback;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";

    private WebView webView;

    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    private String webUrl = "file:///android_asset/index.html";//"https://www.6vdy.org/dianshiju/oumeiju/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        webView = (WebView) findViewById(R.id.mainweb);
        webView = (WebView) findViewById(R.id.mainweb);
//        webView.setWebContentsDebuggingEnabled(true);

        initWebView();

//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//        webView.loadUrl("https://www.6vdy.org/dianshiju/oumeiju/");
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.reload();
    }

    public void initWebView() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        WebChromeClient wvcc = new WebChromeClient();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 关键点
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        webView.setWebChromeClient(wvcc);
        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "onPageFinished");
                loadScript("plugins/6vdy/main.js");
//                webView.loadUrl("javascript:" + "window.alert('Js injection success')" );
                super.onPageFinished(view, url);
            }

//            @Override
//            public void onProgressChanged (WebView view,
//                                           int newProgress) {
//                loadScript("plugins/6vdy/main.js");
////                super.onProgressChanged(view, newProgress);
//            }

        };
        webView.setWebViewClient(wvc);
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view,
//                                           int newProgress) {
//                loadScript("plugins/6vdy/main.js");
////                super.onProgressChanged(view, newProgress);
//            }
//        });

        webView.setWebChromeClient(new WebChromeClient() {
//            /*** 视频播放相关的方法 **/
//            @Override
//            public View getVideoLoadingProgressView() {
//                FrameLayout frameLayout = new FrameLayout(MainActivity.this);
//                frameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//                return frameLayout;
//            }
//            @Override
//            public void onShowCustomView(View view, CustomViewCallback callback) {
////                showCustomView(view, callback);
//            }
//            @Override
//            public void onHideCustomView() {
////                hideCustomView();
//            }

            @Override
            public void onProgressChanged(WebView view,
                                          int newProgress) {
                Log.i(TAG, "onProgressChanged");
                loadScript("plugins/6vdy/main.js");
//                super.onProgressChanged(view, newProgress);
            }
        });
        // 加载Web地址
        webView.loadUrl(webUrl);
//        loadScript("plugins/6vdy/main.js");
    }

    private void showCustomView(View view, CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        MainActivity.this.getWindow().getDecorView();
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(MainActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
    }

    static class FullscreenHolder extends FrameLayout {
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }
        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }
    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
                if (customView != null) {
                    hideCustomView();
                } else if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    private void loadScript(String fn) {
        try {
            InputStream is = this.getAssets().open(fn);
            int lenght = is.available();
            byte[]  buffer = new byte[lenght];
            is.read(buffer);
            String result = new String(buffer, "utf8");
            webView.loadUrl("javascript:" + result);
        } catch (Exception e) {
            Log.e(TAG, "loadScript: error!");
        }
    }
}
