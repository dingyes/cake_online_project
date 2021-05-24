package net.onest.cakeonlineprj.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.OrderDetail;
import net.onest.cakeonlineprj.util.ConfigUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HistoryOrderAdapter extends BaseAdapter {
    private Context hOrderContext;
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
                        orders.remove(orders.get(currentPosition));
                        Toast.makeText(hOrderContext.getApplicationContext(), "订单删除成功", Toast.LENGTH_SHORT).show();
                        HistoryOrderAdapter.this.notifyDataSetChanged();
                    } else {
                        Toast.makeText(hOrderContext.getApplicationContext(), "订单删除失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public HistoryOrderAdapter(Context hOrderContext, List<OrderDetail> orders, int itemLayoutRes) {
        this.hOrderContext = hOrderContext;
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
        LayoutInflater inflater = LayoutInflater.from(hOrderContext);
        convertView = inflater.inflate(itemLayoutRes, null);
        TextView orderId = convertView.findViewById(R.id.order_id);
        TextView orderCakeName = convertView.findViewById(R.id.order_cake_name);
        TextView orderCount = convertView.findViewById(R.id.order_count);
        TextView orderStatus = convertView.findViewById(R.id.order_status);
        Button btnDelete = convertView.findViewById(R.id.btn_delete);
        if (orders.get(position).getId() == 0) {
            orderId.setText("流水号");
            orderCakeName.setText("蛋糕名称");
            orderCount.setText("数量");
            orderStatus.setText("订单状态");
            btnDelete.setVisibility(View.INVISIBLE);
        } else {
            orderId.setText(orders.get(position).getId() + "");
            orderCakeName.setText(orders.get(position).getCakeName());
            orderCount.setText(orders.get(position).getCount() + "");
            Log.e("tag", orders.get(position).getId() + ":" + orders.get(position).getStatus());
            if (orders.get(position).getStatus() == TOBESENTOUT) {
                orderStatus.setText("待发货");
                orderStatus.setTextColor(Color.RED);
            } else if (orders.get(position).getStatus() == DELIVERED) {
                orderStatus.setText("已发货");
                orderStatus.setTextColor(Color.BLUE);
            } else if (orders.get(position).getStatus() == HANDLED) {
                orderStatus.setText("已完成订单");
                orderStatus.setTextColor(Color.GRAY);
            } else {
                orderStatus.setText("出错啦！");
            }
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                // 删除订单
                deleteOrder(orders.get(position).getId());
            }
        });
        return convertView;
    }

    private void deleteOrder(final int id) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/DeleteOrder?id=" + id);
                    InputStream in = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    //读取字符信息
                    String str = reader.readLine();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = str;
                    myHandler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
