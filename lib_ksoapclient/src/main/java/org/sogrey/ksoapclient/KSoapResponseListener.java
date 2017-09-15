package org.sogrey.ksoapclient;

/**
 * Created by Sogrey on 2017/9/15.
 */

public interface KSoapResponseListener {
    /**
     * 接口回调的结果数据
     */
    void post(String result);
}
