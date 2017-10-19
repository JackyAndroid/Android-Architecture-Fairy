package tech.jackywang.intermediate.layer;

/**
 * 委托协议
 *
 * @author jacky
 * @version v1.0
 * @description 对外暴露的协议
 * @since 2017/9/14
 */

public interface IBusinessDelegate {

    IBusinessDelegate setup();

    IBusinessDelegate setupBusiness1();

    IBusinessDelegate setupBusiness2();

    void event1();

    void event2();

    void onCreate();

    void onResume();

    void onPause();

    void onDestroy();
}