package org.sogrey.ksoapclient.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.sogrey.ksoapclient.KSoapClient;
import org.sogrey.ksoapclient.KSoapResponseListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView txt_result;
    EditText edt_telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_result = findViewById(R.id.txt_result);
        edt_telephone = findViewById(R.id.edt_telephone);
    }

    public void getDatabaseInfo(View v) {
        //http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx

        KSoapClient.getInstance(this).init("http://WebXml.com.cn/", "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx").debug(true).setTag(this)//没用。还在规划
                .setMethodNAME("http://WebXml.com.cn/getDatabaseInfo", "getDatabaseInfo")
//              .addSoapHeader()
//              .addParams()
                .execute(new KSoapResponseListener() {
                    @Override
                    public void post(String result) {
                        txt_result.setText(result);
                    }
                });
    }

    public void getMobileCodeInfo(View v) {
        String telephone = edt_telephone.getText().toString().trim();
        if (TextUtils.isEmpty(telephone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("mobileCode", telephone);
        params.put("userID", "");
        KSoapClient.getInstance(this).init("http://WebXml.com.cn/", "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx").debug(true).setTag(this)//没用。还在规划
                .setMethodNAME("http://WebXml.com.cn/getMobileCodeInfo", "getMobileCodeInfo")
//              .addSoapHeader()
                .addParams(params).execute(new KSoapResponseListener() {
            @Override
            public void post(String result) {
                txt_result.setText(result);
            }
        });
    }
}
