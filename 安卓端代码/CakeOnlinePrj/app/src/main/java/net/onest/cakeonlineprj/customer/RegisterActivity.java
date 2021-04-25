package net.onest.cakeonlineprj.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.onest.cakeonlineprj.MainActivity;
import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.SellerActivity;
import net.onest.cakeonlineprj.beans.Customer;
import net.onest.cakeonlineprj.beans.Seller;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerPhone;
    private EditText registerPwd;
    private EditText registerName;
    private Button btnRegister;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String[] customer = ((String) msg.obj).split("&&&");
                    if ("success".equals(customer[0])) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, customer[1], Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerPhone = findViewById(R.id.register_phone);
        registerPwd = findViewById(R.id.register_password);
        btnRegister = findViewById(R.id.btn_register);
        registerName = findViewById(R.id.register_name);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerPhone.length() == 11) {
                    if (registerPwd.length() > 0) {
                        if (registerName.length() > 0) {
                            upRegisterValue(ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/CustomerRegister");
                        } else {
                            Toast.makeText(RegisterActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "手机号码格式不对，应为11位数字", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void upRegisterValue(final String s) {
        new Thread() {
            @Override
            public void run() {
                String phone = registerPhone.getText().toString();
                String password = registerPwd.getText().toString();
                String name = registerName.getText().toString();
                try {
                    JSONObject object = new JSONObject();
                    object.put("phone", phone);
                    object.put("password", password);
                    object.put("name", name);
                    URL url = new URL(s);
                    // 获取URLConnection
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    // 将Json串写入
                    writer.write(object.toString() + "\n");
                    writer.flush();
                    // 获取网络输入流
                    InputStream in = conn.getInputStream();
                    //使用字符流读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    //读取字符信息
                    String str = reader.readLine();
                    Message msg = new Message();
                    msg.obj = str;
                    msg.what = 1;
                    myHandler.sendMessage(msg);
                    in.close();
                    reader.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
