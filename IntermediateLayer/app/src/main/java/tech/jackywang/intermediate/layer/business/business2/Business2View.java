package tech.jackywang.intermediate.layer.business.business2;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author jacky
 * @version v1.0
 * @description
 * @since 2017/10/19
 */

public class Business2View extends FrameLayout implements Business2Contract.View {

    public Business2View(@NonNull Context context) {
        super(context);
    }

    public Business2View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Business2View(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPresenter(Business2Contract.Presenter presenter) {

    }
}
