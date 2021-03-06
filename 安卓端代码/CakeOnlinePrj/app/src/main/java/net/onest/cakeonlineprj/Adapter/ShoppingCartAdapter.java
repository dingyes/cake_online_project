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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
                    // ????????????
                    String result = (String) msg.obj;
                    if ("success".equals(result.trim())) {
                        items.remove(items.get(itemPosition));
                        Toast.makeText(shoppingCartContext.getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(shoppingCartContext.getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
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
        cakeSize.setText(items.get(position).getCakeSize() + "???");
        cakePrice.setText(items.get(position).getCakePrice() + "");
        cakeCount.setText(items.get(position).getCakeCount() + "");
        // ????????????
        coLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ??????????????????
                if (items.get(position).getCakeCount() > 1) {
                    int num = items.get(position).getCakeCount() - 1;
                    items.get(position).setCakeCount(num);
                    cakeCount.setText(items.get(position).getCakeCount() + "");
                } else {
                    Toast.makeText(shoppingCartContext.getApplicationContext(), "???????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        coRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ??????????????????
                int num = items.get(position).getCakeCount() + 1;
                items.get(position).setCakeCount(num);
                cakeCount.setText(items.get(position).getCakeCount() + "");
            }
        });

        Glide.with(shoppingCartContext).load(ConfigUtil.SERVER_ADDR + "/" + items.get(position).getCakeImg())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)  // ???????????????????????????
                .placeholder(R.mipmap.loading)
                .error(R.drawable.photo) // ????????????????????????????????????
                .fallback(R.drawable.photo)  // ?????????????????????null??????????????????
                .into(cakeImg);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPosition = position;
                // ??????item????????????id??????
                // ??????id?????????????????????????????????????????????
                // ??????????????????items???????????????????????????
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            // ?????????????????????
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
                            // ??????URL?????????????????????
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
