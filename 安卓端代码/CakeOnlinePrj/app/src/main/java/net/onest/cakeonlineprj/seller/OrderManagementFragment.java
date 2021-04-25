package net.onest.cakeonlineprj.seller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.onest.cakeonlineprj.Adapter.SellerOrderAdapter;
import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.OrderDetail;
import net.onest.cakeonlineprj.beans.Seller;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementFragment extends Fragment {
    private SellerOrderAdapter sellerOrderAdapter;
    private ListView sellerOrders;
    private List<OrderDetail> orders = new ArrayList<>();
    private View root;
    private String phone = Seller.getCurrentSeller();
    private android.os.Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    String result = (String) msg.obj;
                    convertToOrders(result);
                    initSellerCakeFragment();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_ordermanagement, container, false);
        sellerOrders = root.findViewById(R.id.orders);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initSellerOrders();
    }

    private void initSellerCakeFragment() {
        if (orders.size() != 0) {
            sellerOrderAdapter = new SellerOrderAdapter(getContext(), orders, R.layout.order_management_list_item);
            sellerOrders.setAdapter(sellerOrderAdapter);
        } else {
            orders.clear();
            sellerOrderAdapter = new SellerOrderAdapter(getContext(), orders, R.layout.order_management_list_item);
            sellerOrders.setAdapter(sellerOrderAdapter);
            Toast.makeText(getContext(), "无相关信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void convertToOrders(String result) {
        orders.clear();
        try {
            JSONObject jObj = new JSONObject(result);
            JSONArray jArray = jObj.getJSONArray("orders");
            for (int i = 0; i < jArray.length(); i++) {
                // 获取当前的JSONObject对象
                JSONObject jOrder = jArray.getJSONObject(i);
                int id = jOrder.getInt("id");
                String customerPhone = jOrder.getString("customerPhone");
                String cakeName = jOrder.getString("cakeName");
                int count = jOrder.getInt("count");
                int status = jOrder.getInt("status");
                OrderDetail or = new OrderDetail(id, customerPhone, count, status, cakeName);
                orders.add(or);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示订单
     */
    private void initSellerOrders() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String scUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/GetOrdersDatas?phone=" + phone;
                    URL url = new URL(scUrl);
                    // 通过URL获取网络输入流
                    InputStream in = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    myHandler.sendMessage(message);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
