package net.onest.cakeonlineprj.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.Cake;
import net.onest.cakeonlineprj.beans.Customer;
import net.onest.cakeonlineprj.beans.ShoppingCartItemDetail;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class ShoppingCartAdapter extends BaseAdapter {
    private Context shoppingCartContext;
    private List<ShoppingCartItemDetail> items = new ArrayList<>();
    private int itemLayoutRes;
    private ImageView cakeImg;
    private TextView sellerName;
    private TextView cakeName;
    private TextView cakeSize;
    private TextView cakePrice;
    private TextView coLeft;
    private TextView coRight;
    private Button btnOrder;
    private int itemPosition;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    // 获得信息
                    String result = (String) msg.obj;
                    if ("success".equals(result.trim())) {
                        items.remove(items.get(itemPosition));
                        Toast.makeText(shoppingCartContext.getApplicationContext(), "结算成功", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(shoppingCartContext.getApplicationContext(), "结算失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public ShoppingCartAdapter(Context shoppingCartContext, List<ShoppingCartItemDetail> items, int itemLayoutRes) {
        this.shoppingCartContext = shoppingCartContext;
        this.items = items;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if (null != items) {
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != items) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(shoppingCartContext);
        convertView = inflater.inflate(itemLayoutRes, null);
        sellerName = convertView.findViewById(R.id.seller_name);
        cakeImg = convertView.findViewById(R.id.cake_img);
        cakeName = convertView.findViewById(R.id.cake_name);
        cakeSize = convertView.findViewById(R.id.cake_size);
        cakePrice = convertView.findViewById(R.id.cake_price);
        final TextView cakeCount = convertView.findViewById(R.id.cake_count);
        coLeft = convertView.findViewById(R.id.co_left);
        coRight = convertView.findViewById(R.id.co_right);
        btnOrder = convertView.findViewById(R.id.btn_order);
        sellerName.setText(items.get(position).getSellerName());
        cakeName.setText(items.get(position).getCakeName());
        cakeSize.setText(items.get(position).getCakeSize() + "寸");
        cakePrice.setText(items.get(position).getCakePrice() + "");
        cakeCount.setText(items.get(position).getCakeCount() + "");
        // 数量控制
        coLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 商品数量减一
                if (items.get(position).getCakeCount() > 1) {
                    int num = items.get(position).getCakeCount() - 1;
                    items.get(position).setCakeCount(num);
                    cakeCount.setText(items.get(position).getCakeCount() + "");
                } else {
                    Toast.makeText(shoppingCartContext.getApplicationContext(), "宝贝不能再减少啦！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        coRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 商品数量加一
                int num = items.get(position).getCakeCount() + 1;
                items.get(position).setCakeCount(num);
                cakeCount.setText(items.get(position).getCakeCount() + "");
            }
        });

        try {
            InputStream read = new FileInputStream(items.get(position).getCakeImg());
            Bitmap img = BitmapFactory.decodeStream(read);
            Bitmap bitmap = ConfigUtil.zoomBitmap(img, 100, 100);
            cakeImg.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPosition = position;
                // 获得item的购物车id删除
                // 蛋糕id、用户电话、数量、状态构成订单
                // 若成功，进行items的删除，并进行更新
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            // 将数据进行传输
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("itemId", items.get(position).getId());
                            jsonObject.put("cakeId", items.get(position).getCakeId());
                            jsonObject.put("customerPhone", Customer.getCurrentCustomer());
                            jsonObject.put("count", items.get(position).getCakeCount());
                            String json = jsonObject.toString();
                            String urlPath = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/ConvertItemToOrder";
                            URL url = new URL(urlPath);
                            URLConnection conn = url.openConnection();
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            OutputStream out = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                            writer.write(json + "\n");
                            writer.flush();
                            // 通过URL获取网络输入流
                            InputStream in = conn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                            String result = reader.readLine();
                            writer.close();
                            reader.close();
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            myHandler.sendMessage(message);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        return convertView;
    }
}
