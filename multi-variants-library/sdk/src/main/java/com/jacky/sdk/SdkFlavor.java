
package com.jacky.sdk;

import android.util.Log;

/**
 * @author wangshichang
 * @version v1.0
 * @since 2016/11/22
 */

public class SdkFlavor {
    public SdkFlavor() {
        Log.d("===Sdk Flavor=== flavor", "" + BuildConfig.FLAVOR);
        Log.d("===Sdk Flavor=== Flag", "" + BuildConfig.flavorFlag);
    }
}
