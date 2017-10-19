package tech.jackywang.intermediate.layer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import tech.jackywang.intermediate.layer.business.business1.Business1Contract;
import tech.jackywang.intermediate.layer.business.business1.Business1Model;
import tech.jackywang.intermediate.layer.business.business1.Business1Presenter;
import tech.jackywang.intermediate.layer.business.business1.Business1View;
import tech.jackywang.intermediate.layer.business.business2.Business2Contract;
import tech.jackywang.intermediate.layer.business.business2.Business2Model;
import tech.jackywang.intermediate.layer.business.business2.Business2Presenter;
import tech.jackywang.intermediate.layer.business.business2.Business2View;

/**
 * 委托
 *
 * @author jacky
 * @version v1.0
 * @description 委托，隔离各业务间的耦合
 * @since 2017/9/14
 */

public class BusinessDelegate extends RelativeLayout implements IBusinessDelegate {

    private WeakReference<Context> mContextReference;

    private Business1Contract.Presenter mBusiness1Presenter;
    private Business2Contract.Presenter mBusiness2Presenter;

    public BusinessDelegate(Context context) {
        super(context);
    }

    public BusinessDelegate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BusinessDelegate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public IBusinessDelegate setup() {
        mContextReference = new WeakReference<>(getContext());
        removeAllViews();
        return this;
    }

    // ---- 动态加载挂件 Start ----
    @Override
    public IBusinessDelegate setupBusiness1() {
        Business1View view = new Business1View(mContextReference.get());
        mBusiness1Presenter = new Business1Presenter(view, new Business1Model());
        view.setPresenter(mBusiness1Presenter);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(view, params);
        return this;
    }

    @Override
    public IBusinessDelegate setupBusiness2() {
        Business2View view = new Business2View(mContextReference.get());
        mBusiness2Presenter = new Business2Presenter(view, new Business2Model());
        view.setPresenter(mBusiness2Presenter);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(view, params);
        return this;
    }
    // ---- 动态加载挂件 End ----

    // ---- 接收事件 Start ----
    @Override
    public void event1() {
        if (mBusiness1Presenter != null) {
            mBusiness1Presenter.notify();
        }
    }

    @Override
    public void event2() {
        if (mBusiness2Presenter != null) {
            mBusiness2Presenter.notify();
        }
    }
    // ---- 接收事件 End ----

    // ---- 生命周期 Start ----
    @Override
    public void onCreate() {
        if (mBusiness1Presenter != null) {
            mBusiness1Presenter.onCreate();
        }
        if (mBusiness2Presenter != null) {
            mBusiness2Presenter.onCreate();
        }
    }

    @Override
    public void onResume() {
        if (mBusiness1Presenter != null) {
            mBusiness1Presenter.onResume();
        }
        if (mBusiness2Presenter != null) {
            mBusiness2Presenter.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mBusiness1Presenter != null) {
            mBusiness1Presenter.onPause();
        }
        if (mBusiness2Presenter != null) {
            mBusiness2Presenter.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if (mBusiness1Presenter != null) {
            mBusiness1Presenter.onDestroy();
        }
        if (mBusiness2Presenter != null) {
            mBusiness2Presenter.onDestroy();
        }
    }
    // ---- 生命周期 End ----
}