package org.sogrey.ksoapclient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * http://www.cnblogs.com/wangq0126/p/3980205.html
 * <p>
 * Created by Sogrey on 2017/9/15.
 */

public class KSoapClient {
    private static final String TAG = "KSoapClient";

    Context context;

    private String NAMESPACE = "http://tempuri.org/";
    private String URL = "http://192.168.1.189:8079/WebService1.asmx";
    private String SOAP_ACTION = "http://tempuri.org/_GetTableValue";
    private String METHOD_NAME = "_GetTableValue";
    private String mSoapHeaderName = "";
    private Map<String, String> mSoapHeaderMap = new HashMap<>();
    private Map<String, String> mParamsMap = new HashMap<>();
    private Object mTag = null;
    private boolean mIsDebug = false;
    /**
     * 单例模式 对象
     */
    private static KSoapClient sInstance;

    /**
     * 单例模式 <br>
     * 一个类最多只能有一个实例 <br>
     * 1、有一个私有静态成员 <br>
     * 2、有一个公开静态方法getInstance得到这个私有静态成员 <br>
     * 3、有一个私有的构造方法（不允许被实例化） <br>
     */

    public static KSoapClient getInstance(Context context) {
        if (sInstance == null) {
            synchronized (KSoapClient.class) {
                if (sInstance == null) {
                    sInstance = new KSoapClient(context);
                }
            }
        }
        return sInstance;
    }

    private KSoapClient(Context context) {
        this.context = context;
    }

    /**
     * 是否开启调试
     *
     * @param debug true 开启调试输出日志
     */
    public KSoapClient debug(boolean debug) {
        this.mIsDebug = debug;
        return this;
    }

    /**
     * 初始化--一般设置一次，最先初始化，放在公共部分
     *
     * @param namespace 命名空间，xmlns值，默认为xmlns="http://tempuri.org/"
     * @param url       接口url，例如：http://192.168.1.189:8079/WebService1.asmx?op=_GetTableValue 则url=http://192.168.1.189:8079/WebService1.asmx
     */
    public KSoapClient init(String namespace, String url) {
        this.NAMESPACE = namespace;
        this.URL = url;
        return this;
    }

    /**
     * 设置请求方法名和SOAPAction
     *
     * @param SOAPAction 可在接口发布页面查看到
     * @param methodName 方法名称
     */
    public KSoapClient setMethodNAME(String SOAPAction, String methodName) {
        this.SOAP_ACTION = SOAPAction;
        this.METHOD_NAME = methodName;
        return this;
    }

    /**
     * 设置tag
     *
     * @param tag tag区分请求
     */
    public KSoapClient setTag(Object tag) {
        this.mTag = tag;
        return this;
    }

    /**
     * 添加SOAPHeader认证信息
     *
     * @param soapHeaderMap SOAPHeader头认证，map对象 key-value
     */
    public KSoapClient addSoapHeader(String soapHeaderName, Map<String, String> soapHeaderMap) {
        this.mSoapHeaderName = soapHeaderName;
        this.mSoapHeaderMap = soapHeaderMap;
        return this;
    }

    /**
     * 添加请求参数
     *
     * @param paramsMap 请求参数，map对象 key-value
     */
    public KSoapClient addParams(Map<String, String> paramsMap) {
        this.mParamsMap = paramsMap;
        return this;
    }

    public void execute(KSoapResponseListener l) {
        KSoapAsyncTask task = new KSoapAsyncTask(this.NAMESPACE, this.URL, this.SOAP_ACTION, this.METHOD_NAME, this.mSoapHeaderName, this.mSoapHeaderMap, this.mParamsMap, l);
        task.execute();
    }

    private class KSoapAsyncTask extends AsyncTask<Void, Void, String> {

        private String NAMESPACE = "";
        private String URL = "";
        private String SOAP_ACTION = "";
        private String METHOD_NAME = "";
        private String SOAPHeaderName = "";
        private Map<String, String> mSoapHeaderMap = new HashMap<>();
        private Map<String, String> mParamsMap = new HashMap<>();
        private KSoapResponseListener listener;

        public KSoapAsyncTask(String nameSpace, String url, String soapAction, String methodName, String soapHeaderName, Map<String, String> soapHeaderMap, Map<String, String> paramsMap, KSoapResponseListener l) {
            this.NAMESPACE = nameSpace;
            this.URL = url;
            this.SOAP_ACTION = soapAction;
            this.METHOD_NAME = methodName;
            this.SOAPHeaderName = soapHeaderName;
            this.mSoapHeaderMap = soapHeaderMap;
            this.mParamsMap = paramsMap;
            this.listener = l;
        }

        @Override
        protected String doInBackground(Void... voids) {
            // Create request
            SoapObject request = new SoapObject(this.NAMESPACE, this.METHOD_NAME);
            // Create envelope
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //在这里加入了SoapHeader
            Element[] header = new Element[1];
            if (this.mSoapHeaderMap != null && !this.mSoapHeaderMap.isEmpty()) {
                //MySoapHeader与设置的soapheader名称保持一致
                header[0] = new Element().createElement(this.NAMESPACE, this.SOAPHeaderName);
                for (Map.Entry entry : this.mSoapHeaderMap.entrySet()) {
//                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    //key与webservice服务端soapheader的key保持一致
                    Element ele = new Element().createElement(this.NAMESPACE, key);
                    ele.addChild(Node.TEXT, value);
                    header[0].addChild(Node.ELEMENT, ele);
                }
                //设置SOAPHeader
                envelope.headerOut = header;
            }

            /**请求参数*/
            if (this.mParamsMap != null && !this.mParamsMap.isEmpty()) {
                for (Map.Entry entry : this.mParamsMap.entrySet()) {
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    // Property which holds input parameters
                    PropertyInfo celsiusPI = new PropertyInfo();
                    // Set Name
                    celsiusPI.setName(key);
                    // Set Value
                    celsiusPI.setValue(value);
                    // Set dataType
                    celsiusPI.setType(String.class);
                    // Add the property to request object
                    request.addProperty(celsiusPI);
                }
            }


            envelope.dotNet = true;
            envelope.bodyOut = request;

            // Set output SOAP object
            envelope.setOutputSoapObject(request);
            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(this.URL);
            androidHttpTransport.debug = mIsDebug;

            try {
                // Invole web service
                androidHttpTransport.call(this.SOAP_ACTION, envelope);
                // Get the response
//            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
//            Object response = (Object) envelope.getResponse();
////            // Assign it to fahren static variable
//            fahren = response.toString();
//
                //请求
                String requestDump = androidHttpTransport.requestDump;
                //响应
                String responseDump = androidHttpTransport.responseDump;

                if (mIsDebug) {
                    Log.i(TAG, "---> Request: " + requestDump);
                    Log.i(TAG, "<--- Response: " + responseDump);
                }

                SoapObject soapObject = (SoapObject) envelope.bodyIn;

                if (soapObject != null) {
                    String result = soapObject.toString();
                    if (mIsDebug) {
                        Log.i(TAG, ">>>> Result: " + result);
                    }
                    return result;
                } else {
                    System.out.println("soapObject is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.listener != null) {
                this.listener.post(result);
            }
            cancel(true);
        }
    }
}
