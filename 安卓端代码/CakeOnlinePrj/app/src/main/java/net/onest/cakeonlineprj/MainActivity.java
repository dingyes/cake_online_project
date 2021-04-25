package net.onest.cakeonlineprj;

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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import net.onest.cakeonlineprj.beans.Customer;
import net.onest.cakeonlineprj.beans.Seller;
import net.onest.cakeonlineprj.customer.RegisterActivity;
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

public class MainActivity extends AppCompatActivity {
    private final int SELLER = 0;
    private final int CUSTOMER = 1;
    private EditText loginPhone;
    private EditText loginPassword;
    private RadioButton loginCustomer;
    private RadioButton loginSeller;
    private Button btnLogin;
    private TextView customerRegister;
    private int role = CUSTOMER;  // 0代表买家,1代表卖家
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:  // 验证卖家登录是否成功
                    String seller = (String) msg.obj;
                    if ("success".equals(seller)) {
                        Seller.setCurrentSeller(loginPhone.getText().toString());
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, SellerActivity.class);
                        startActivity(intent1);
                    } else {
                        Customer.setCurrentCustomer(loginPhone.getText().toString());
                        Toast.makeText(MainActivity.this, "号码或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:  // 验证买家登录是否成功
                    String customer = (String) msg.obj;
                    if ("success".equals(customer)) {
                        Log.e("customer",customer);
                        Customer.setCurrentCustomer(loginPhone.getText().toString());
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, CustomerActivity.class);
                        startActivity(intent1);
                    } else {
                        Toast.makeText(MainActivity.this, "号码或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPhone.setText("");
        loginPassword.setText("");
        loginCustomer.setChecked(true);
        role = CUSTOMER;
    }

    private void findViews() {
        loginPhone = findViewById(R.id.login_phone);
        loginPassword = findViewById(R.id.login_password);
        loginCustomer = findViewById(R.id.login_customer);
        loginSeller = findViewById(R.id.login_seller);
        btnLogin = findViewById(R.id.btn_login);
        customerRegister = findViewById(R.id.customer_register);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        loginCustomer.setOnClickListener(myListener);
        loginSeller.setOnClickListener(myListener);
        btnLogin.setOnClickListener(myListener);
        customerRegister.setOnClickListener(myListener);
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_customer:
                    // 买家
                    loginCustomer.setChecked(true);
                    loginSeller.setChecked(false);
                    role = CUSTOMER;
                    break;
                case R.id.login_seller:
                    // 卖家
                    loginSeller.setChecked(true);
                    loginCustomer.setChecked(false);
                    role = SELLER;
                    break;
                case R.id.btn_login:
                    if (role == CUSTOMER) {
                        // 买家身份验证
                        customerLoginVerification();
                    } else {
                        // 卖家身份验证
                        sellerLoginVerification();
                    }
                    break;
                case R.id.customer_register:
                    Intent intent1 = new Intent();
                    intent1.setClass(MainActivity.this, RegisterActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    }

    /**
     * 买家身份验证
     */
    private void customerLoginVerification() {
        if (loginPhone.length() == 11) {
            if (loginPassword.length() > 0) {
                Customer customer = new Customer();
                customer.setCustomerPhone(loginPhone.getText().toString());
                customer.setCustomerPassword(loginPassword.getText().toString());
                String customJson = customerConvertToJson(customer);
                translateCustomerDataToServer(customJson);
            } else {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "手机号码格式不对，应为11位数字", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 卖家身份验证
     */
    private void sellerLoginVerification() {
        if (loginPhone.length() == 11) {
            if (loginPassword.length() > 0) {
                Seller seller = new Seller();
                seller.setSellerPhone(loginPhone.getText().toString());
                seller.setSellerPassword(loginPassword.getText().toString());
                String sellerJson = sellerConvertToJson(seller);
                translateSellerDataToServer(sellerJson);
            } else {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "手机号码格式不对，应为11位数字", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将Seller对象转换成Json串
     *
     * @param seller
     * @return
     */
    private String sellerConvertToJson(Seller seller) {
        String json = null;
        try {
            // 创建JSONObject对象
            JSONObject jObj = new JSONObject();
            // 向JSONObject中添加数据
            jObj.put("sellerPhone", seller.getSellerPhone());
            jObj.put("sellerPwd", seller.getSellerPassword());
            // 由JSONObject生成Json字符串
            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 将Customer对象转换成Json串
     *
     * @param customer
     * @return
     */
    private String customerConvertToJson(Customer customer) {
        String json = null;
        try {
            // 创建JSONObject对象
            JSONObject jObj = new JSONObject();
            // 向JSONObject中添加数据
            jObj.put("customerPhone", customer.getCustomerPhone());
            jObj.put("customerPwd", customer.getCustomerPassword());
            // 由JSONObject生成Json字符串
            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 将seller对象的json串传递给服务端进行验证
     *
     * @param sellerJson
     */
    private void translateSellerDataToServer(final String sellerJson) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String sellerUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/" + "SellerLoginVerification";
                    URL url = new URL(sellerUrl);
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    // 获取输入流与输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    // 将Json串写入
                    writer.write(sellerJson + "\n");
                    writer.flush();
                    // 获取客户端信息
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 将customer对象Json串传送给服务端进行验证
     *
     * @param customJson
     */
    private void translateCustomerDataToServer(final String customJson) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String customerUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/CustomerLoginVerification";
                    URL url = new URL(customerUrl);
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    // 获取输入流与输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    // 将Json串写入
                    writer.write(customJson + "\n");
                    writer.flush();
                    // 获取客户端信息
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = result;
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
