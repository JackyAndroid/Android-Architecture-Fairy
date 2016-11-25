
package com.jacky.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.jacky.common.CommonFlavor;
import com.jacky.library.UIKitFlavor;
import com.jacky.sdk.SdkFlavor;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("===App Flavor=== flavor", "" + BuildConfig.FLAVOR);
        Log.d("===App Flavor=== Flag", "" + BuildConfig.flavorFlag);

        new UIKitFlavor();
        new SdkFlavor();
        new CommonFlavor();
    }
}
