
package com.jacky.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.jacky.common.CommonFlavor;
import com.jacky.library.UIKitFlavor;
import com.jacky.sdk.SdkFlavor;

public class MainActivity extends Activity {

    private WebView webView;
    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        container = (ViewGroup) findViewById(R.id.container);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.setWebChromeClient(new ChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.youku.com/");

        Log.d("===App Flavor=== flavor", "" + BuildConfig.FLAVOR);
        Log.d("===App Flavor=== Flag", "" + BuildConfig.flavorFlag);

        new UIKitFlavor();
        new SdkFlavor();
        new CommonFlavor();
    }

    public class ChromeClient extends WebChromeClient {

        /**
         * Notify the host application that the current page has entered full screen mode.
         *
         * @param view is the View object to be shown.
         * @param callback invoke this callback to request the page to exit full screen mode.
         */
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            Activity activity = getActivity(webView);
            onShowCustomView(view, activity.getRequestedOrientation(), callback);
        }

        /**
         * Notify the host application that the current page would like to show a custom View in a particular orientation.
         *
         * @param view is the View object to be shown.
         * @param callback invoke this callback to request the page to exit full screen mode.
         */
        @Override
        public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
            view.setBackgroundColor(getResources().getColor(android.R.color.black));
            container.addView(view, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        /**
         * By view's context gain parent context as activity
         *
         * @param view child view
         * @return return parent context as activity
         */
        private Activity getActivity(View view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
            return null;
        }
    }

}
