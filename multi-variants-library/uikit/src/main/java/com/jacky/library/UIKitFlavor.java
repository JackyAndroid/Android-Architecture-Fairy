
package com.jacky.library;

import android.util.Log;

/**
 * @author wangshichang
 * @version v1.0
 * @since 2016/11/22
 */

public class UIKitFlavor {
    public UIKitFlavor() {
        Log.d("===UIKit Flavor==flavor", "" + BuildConfig.FLAVOR);
        Log.d("===UIKit Flavor=== Flag", "" + BuildConfig.flavorFlag);
    }
}
