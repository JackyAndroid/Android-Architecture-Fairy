package tech.jackywang.intermediate.layer.business.business1;

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

public class Business1View extends FrameLayout implements Business1Contract.View {

    public Business1View(@NonNull Context context) {
        super(context);
    }

    public Business1View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Business1View(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPresenter(Business1Contract.Presenter presenter) {

    }
}
