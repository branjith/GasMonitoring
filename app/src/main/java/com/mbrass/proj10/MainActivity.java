package com.mbrass.proj10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

    private EditText tokenText, phoneNumber;
    private TextView v1, v2, v3, v4, v5, v6, v7, v8;
    private Button sendSms ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenText = findViewById(R.id.tokenText);
        phoneNumber = findViewById(R.id.phoneText);
        v1 = (TextView) findViewById(R.id.v1View);
        v2 = (TextView) findViewById(R.id.v2View);
        v3 = (TextView) findViewById(R.id.v3View);
        v4 = (TextView) findViewById(R.id.v4View);
        v5 = (TextView) findViewById(R.id.v5View);
        v6 = (TextView) findViewById(R.id.v6View);
        v7 = (TextView) findViewById(R.id.v7View);
        v8 = (TextView) findViewById(R.id.v8View);
        sendSms = (Button) findViewById(R.id.sendButton);


        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                andRestCall();
            }
        };

        timer.schedule (hourlyTask, 0l, 1000*60*60);

        sendSms.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
    }

    public String getValue(){
        StringBuffer sb = new StringBuffer();
        String o2    = "    O2   : "+v7.getText().toString();
        String co2   = "    CO2  : "+v4.getText().toString();
        String temp  = "    Temp : "+v2.getText().toString();
        String co    = "    CO   : "+v3.getText().toString();
        String hum   = "    Hum  : "+v1.getText().toString();
        String h2    = "    H2   : "+v6.getText().toString();
        String ch4   = "    CH4  : "+v5.getText().toString();
        String lora  = "    Lora : "+v8.getText().toString();
        sb.append(o2);
        sb.append("\n");
        sb.append(co2);
        sb.append("\n");
        sb.append(temp);
        sb.append("\n");
        sb.append(co);
        sb.append("\n");
        sb.append(hum);
        sb.append("\n");
        sb.append(h2);
        sb.append("\n");
        sb.append(ch4);
        sb.append("\n");
        sb.append(lora);
        return sb.toString();
    }

    public void sendSms(){
        EditText p1 = (EditText) phoneNumber;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.SEND_SMS}, 1);
        }
        else {
            if (p1.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(p1.getText().toString(), null, getValue(), pi, null);
                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void andRestCall(){
        String token = "P7HT8sOD1ep43FDQ8aEVHzZEX8rhO-4z";
        if(tokenText.getText().toString().trim().isEmpty()==false)
            token = tokenText.getText().toString().trim();
        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
              //  .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.get("http://blynk-cloud.com/{token}/get/{pin}")
                .addPathParameter("token", token)
                .addPathParameter("pin","V1")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        v1.setText("          HUMIDITY: \n" +
                                   "            "+response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        v1.setText("          ERROR");
                    }
                });

        AndroidNetworking.get("http://blynk-cloud.com/{token}/get/{pin}")
                .addPathParameter("token", token)
                .addPathParameter("pin","V2")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        v2.setText("      TEMPERATURE : \n" +
                                   "           "+response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        v2.setText("          ERROR");
                    }
                });
        AndroidNetworking.get("http://blynk-cloud.com/{token}/get/{pin}")
                .addPathParameter("token", token)
                .addPathParameter("pin","V3")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        v3.setText("   CARBON MONO OXIDE : \n" +
                                   "          "+response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        v3.setText("          ERROR");
                    }
                });
        AndroidNetworking.get("http://blynk-cloud.com/{token}/get/{pin}")
                .addPathParameter("token", token)
                .addPathParameter("pin","V4")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        v4.setText("   CARBON DI OXIDE : \n" +
                                   "           "+response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        v4.setText("          ERROR");
                    }
                });
        AndroidNetworking.get("http://blynk-cloud.com/{token}/get/{pin}")
                .addPathParameter("token", token)
                .addPathParameter("pin","V5")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        v5.setText("        METHANE : \n" +
                                   "           "+response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        v5.setText("          ERROR");
                    }
                });
        AndroidNetworking.get("http://blynk-cloud.com/{token}/get/{pin}")
                .addPathParameter("token", token)
                .addPathParameter("pin","V6")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        v6.setText("          HYDROGEN : \n" +
                                   "              "+response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        v6.setText("          ERROR");
                    }
                });
        AndroidNetworking.get("http://blynk-cloud.com/{token}/get/{pin}")
                .addPathParameter("token", token)
                .addPathParameter("pin","V7")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        v7.setText("          OXYGEN : \n" +
                                   "            "+response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        v7.setText("          ERROR");
                    }
                });
        AndroidNetworking.get("http://blynk-cloud.com/{token}/get/{pin}")
                .addPathParameter("token", token)
                .addPathParameter("pin","V8")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        v8.setText("            LORA : \n" +
                                   "           "+response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        v8.setText("          ERROR");
                    }
                });
    }
}
