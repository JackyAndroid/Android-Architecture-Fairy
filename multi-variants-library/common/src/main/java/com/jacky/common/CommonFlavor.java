
package com.jacky.common;

import android.util.Log;

/**
 * @author wangshichang
 * @version v1.0
 * @since 2016/11/22
 */

public class CommonFlavor {
    public CommonFlavor() {
        Log.d("==Common Flavor= flavor", "" + BuildConfig.FLAVOR);
        Log.d("==Common Flavor==Flag", "" + BuildConfig.flavorFlag);
    }
}
