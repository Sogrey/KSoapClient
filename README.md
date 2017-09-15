# KSoapClient
KSoapClient:使用ksoap2进行Webservice请求

Webservice ：[http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx](http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx)

[demo apk下载](https://github.com/Sogrey/KSoapClient/blob/master/app-debug.apk?raw=true)

![demo](https://github.com/Sogrey/KSoapClient/blob/master/demo.gif?raw=true)

## 用法

	    String telephone = edt_telephone.getText().toString().trim();
        if (TextUtils.isEmpty(telephone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }
        //请求参数
        Map<String, String> params = new HashMap<>();
        params.put("mobileCode", telephone);
        params.put("userID", "");
        KSoapClient.getInstance(this)
				   .init(【nameSpace】, 【url】)
				   .debug(true)//是否开启调试-默认不开启，开启则会输出请求日志
                   .setTag(this)//没用。还在规划
                   .setMethodNAME(【SOAPAction】, 【MethodName】)
                 //.addSoapHeader() //此接口没有SOAPHeader认证
                   .addParams(params).execute(new KSoapResponseListener() {
			            @Override
			            public void post(String result) {
			                txt_result.setText(result);
			            }
        });

以上【】中各变量说明

- nameSpace 命名空间
- url 请求url地址
- SOAPAction 
- MethodName 方法名称

以上各变量去哪儿找，看下面：

![](https://github.com/Sogrey/KSoapClient/blob/master/pics/TIM20170915165608.jpg?raw=true)

示例接口没有SoapHeader验证，拿另一个有SoapHeader验证的说明：

![](https://github.com/Sogrey/KSoapClient/blob/master/pics/TIM20170915170002.jpg?raw=true)

图中：MySoapHeader 为SoapHeader的名称，他有两个字段UserName和PassWord为字符串型。

另以上信息也可在WSDL找到： `url+?WSDL`

![](https://github.com/Sogrey/KSoapClient/blob/master/pics/TIM20170915170517.jpg?raw=true)




## 参考文章

- [http://www.cnblogs.com/wangq0126/p/3980205.html](http://www.cnblogs.com/wangq0126/p/3980205.html)