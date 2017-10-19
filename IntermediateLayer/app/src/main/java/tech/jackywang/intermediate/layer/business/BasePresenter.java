package tech.jackywang.intermediate.layer.business;

/**
 * @author jacky
 * @version v1.0
 * @description
 * @since 2017/10/19
 */

public interface BasePresenter {

    void onCreate();

    void onResume();

    void onPause();

    void onDestroy();
}
