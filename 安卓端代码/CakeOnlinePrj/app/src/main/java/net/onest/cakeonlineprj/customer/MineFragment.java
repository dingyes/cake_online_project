package net.onest.cakeonlineprj.customer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.onest.cakeonlineprj.Adapter.HistoryOrderAdapter;
import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.Customer;
import net.onest.cakeonlineprj.beans.OrderDetail;
import net.onest.cakeonlineprj.map.MapActivity;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MineFragment extends Fragment {
    private HistoryOrderAdapter historyOrderAdapter;
    private ListView historyOrders;
    private List<OrderDetail> orders = new ArrayList<>();
    private View root;
    private String phone = Customer.getCurrentCustomer();
    private ImageView customerPhoto;
    private TextView nickname;
    private TextView customerPhone;
    private Button btnMap;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    // 用户Json串信息
                    String cusJson = (String) msg.obj;
                    Customer customer = convertToObj(cusJson);
                    nickname.setText(customer.getNickname() + "的购买记录");
                    customerPhone.setText(phone);
                    break;
                case 2:
                    // 用户图片
                    Bitmap rawBitmap = (Bitmap) msg.obj;
                    //把图片显示在图片控件中
                    Bitmap bitmap = ConfigUtil.zoomBitmap(rawBitmap, 90, 90);
                    customerPhoto.setImageBitmap(bitmap);
                    break;
                case 3:
                    // 用户历史订单
                    OrderDetail tip = new OrderDetail(0, 0, 0, null);
                    orders.add(tip);
                    String hisOrders = (String) msg.obj;
                    convertToOrders(hisOrders);
                    historyOrderAdapter = new HistoryOrderAdapter(getContext(), orders, R.layout.history_order_list_item);
                    historyOrders.setAdapter(historyOrderAdapter);
                    break;
            }
        }
    };

    private void convertToOrders(String historyOrders) {
        try {
            JSONObject jObj = new JSONObject(historyOrders);
            JSONArray jArray = jObj.getJSONArray("history");
            for (int i = 0; i < jArray.length(); i++) {
                // 获取当前的JSONObject对象
                JSONObject jOrder = jArray.getJSONObject(i);
                int id = jOrder.getInt("id");
                String cakeName = jOrder.getString("cakeName");
                int count = jOrder.getInt("count");
                int status = jOrder.getInt("status");
                OrderDetail or = new OrderDetail(id, count, status, cakeName);
                orders.add(or);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_mine, container, false);
        findViews();
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        orders.clear();
        initData();
        initPhoto();
        initHistoryOrders();
    }

    /**
     * 显示历史订单
     */
    private void initHistoryOrders() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String scUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/" + "GetSelfHistoryOrders?phone=" + Customer.getCurrentCustomer();
                    URL url = new URL(scUrl);
                    // 通过URL获取网络输入流
                    InputStream in = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    Message message = new Message();
                    message.what = 3;
                    message.obj = result;
                    myHandler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initPhoto() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String customerUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/" + "CustomerMineImg?phone=" + phone;
                    URL url = new URL(customerUrl);
                    URLConnection conn = url.openConnection();
                    //获取字节输入流
                    InputStream in = conn.getInputStream();
                    //把输入流解析成一个Bitmap对象
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    //借助于Message，将图片显示在界面上
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = bitmap;
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String sellerUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/" + "CustomerMineData";
                    URL url = new URL(sellerUrl);
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    // 获取数据
                    // 获取输入流与输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    // 将Json串写入
                    writer.write(phone + "\n");
                    writer.flush();
                    // 获取客户端信息
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();   // 买家对象的Json串
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

    private void findViews() {
        customerPhoto = root.findViewById(R.id.customer_photo);
        customerPhone = root.findViewById(R.id.customer_phone);
        nickname = root.findViewById(R.id.nickname);
        historyOrders = root.findViewById(R.id.history_order);
        btnMap = root.findViewById(R.id.btn_map);
    }

    private Customer convertToObj(String cusJson) {
        Customer customer = null;
        try {
            // 根据指定的json串创建JSONObject对象
            JSONObject jObj = null;
            jObj = new JSONObject(cusJson);
            // 构造出对象
            customer = new Customer();
            // 获取JSONObject中元素
            String customerPhone = jObj.getString("customerPhone");
            String nickname = jObj.getString("nickname");
            customer.setNickname(nickname);
            customer.setCustomerPhone(customerPhone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
