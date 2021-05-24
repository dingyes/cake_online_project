package net.onest.cakeonlineprj.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.OrderDetail;
import net.onest.cakeonlineprj.seller.WebViewActivity;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SellerOrderAdapter extends BaseAdapter {
    private Context orderContext;
    private List<OrderDetail> orders = new ArrayList<>();
    private int itemLayoutRes;
    private final int TOBESENTOUT = 0; // 待发货
    private final int DELIVERED = 1;  // 已发货
    private final int HANDLED = 2;  // 已完成
    private int currentPosition;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    if ("success".equals(result)) {
                        if (orders.get(currentPosition).getStatus() == TOBESENTOUT) {
                            orders.get(currentPosition).setStatus(DELIVERED);
                            sendNotificationToCustomer();
                        } else if (orders.get(currentPosition).getStatus() == DELIVERED) {
                            orders.get(currentPosition).setStatus(HANDLED);
                            sendNotificationToCustomer();
                        }
                        Toast.makeText(orderContext.getApplicationContext(), "订单状态修改成功", Toast.LENGTH_SHORT).show();
                        // 判断状态
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(orderContext.getApplicationContext(), "订单状态修改失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void sendNotificationToCustomer() {
        Intent intent = new Intent(orderContext, WebViewActivity.class);
        orderContext.startActivity(intent);
    }

    public SellerOrderAdapter(Context orderContext, List<OrderDetail> orders, int itemLayoutRes) {
        this.orderContext = orderContext;
        this.orders = orders;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if (null != orders) {
            return orders.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != orders) {
            return orders.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(orderContext);
        convertView = inflater.inflate(itemLayoutRes, null);
        TextView orderId = convertView.findViewById(R.id.order_id);
        final TextView orderCakeName = convertView.findViewById(R.id.order_cake_name);
        TextView orderCount = convertView.findViewById(R.id.order_count);
        TextView orderStatus = convertView.findViewById(R.id.order_status);
        TextView orderCustomerPhone = convertView.findViewById(R.id.customer_phone);
        Button btnUpdate = convertView.findViewById(R.id.btn_update);

        orderCustomerPhone.setText(orders.get(position).getCustomerPhone());
        orderId.setText(orders.get(position).getId() + "");
        orderCakeName.setText(orders.get(position).getCakeName());
        orderCount.setText("数量：" + orders.get(position).getCount());
        if (orders.get(position).getStatus() == TOBESENTOUT) {
            orderStatus.setText("待发货");
            orderStatus.setTextColor(Color.RED);
            btnUpdate.setText("发货");
        } else if (orders.get(position).getStatus() == DELIVERED) {
            orderStatus.setText("已发货");
            orderStatus.setTextColor(Color.BLUE);
            btnUpdate.setText("完成订单");
        } else if (orders.get(position).getStatus() == HANDLED) {
            orderStatus.setText("已完成订单");
            orderStatus.setTextColor(Color.GRAY);
            btnUpdate.setText("已完成订单");
        } else {
            orderStatus.setText("出错啦！");
            orderStatus.setTextColor(Color.RED);
            btnUpdate.setText("删除");
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                if (orders.get(currentPosition).getStatus() == TOBESENTOUT || orders.get(currentPosition).getStatus() == DELIVERED) {
                    updataStatus();
                } else {
                    Toast.makeText(orderContext.getApplicationContext(), "订单已完成，无需修改", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    private void updataStatus() {
        new Thread() {
            @Override
            public void run() {
                JSONObject jObj = new JSONObject();
                try {
                    jObj.put("orderId", orders.get(currentPosition).getId());
                    jObj.put("status", orders.get(currentPosition).getStatus());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String urlPath = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/UpdateOrderStatus";
                    URL url = new URL(urlPath);
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    // 获取数据
                    // 获取输入流与输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    // 将Json串写入
                    writer.write(jObj.toString() + "\n");
                    writer.flush();
                    // 发送消息
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    myHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
