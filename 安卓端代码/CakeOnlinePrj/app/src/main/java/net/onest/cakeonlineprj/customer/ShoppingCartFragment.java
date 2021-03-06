package net.onest.cakeonlineprj.customer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.onest.cakeonlineprj.Adapter.CakeAdapter;
import net.onest.cakeonlineprj.Adapter.ShoppingCartAdapter;
import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.Cake;
import net.onest.cakeonlineprj.beans.Customer;
import net.onest.cakeonlineprj.beans.ShoppingCartItem;
import net.onest.cakeonlineprj.beans.ShoppingCartItemDetail;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class ShoppingCartFragment extends Fragment {
    private ShoppingCartAdapter scartAdapter;
    private ListView shoppingCartItems;
    private List<ShoppingCartItemDetail> items = new ArrayList<>();
    private View root;
    private Button btnAddToOrder;
    private String resultDetail;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    resultDetail = result;
                    ConvertToDetailList detail = new ConvertToDetailList();
                    detail.start();
                    try {
                        detail.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    initShoppingFragment();
                    break;
                case 2:
                    String result1 = (String) msg.obj;
                    if ("success".equals(result1)) {
                        items.clear();
                        scartAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * ??????adapter
     */
    private void initShoppingFragment() {
        if (items.size() == 0) {
            Toast.makeText(getContext(), "???????????????????????????????????????????????????~", Toast.LENGTH_SHORT).show();
        } else {
            scartAdapter = new ShoppingCartAdapter(getContext(), items, R.layout.shoppingcart_list_item);
            shoppingCartItems.setAdapter(scartAdapter);
        }
    }

    private class ConvertToDetailList extends Thread {
        @Override
        public void run() {
            try {
                JSONObject jObj = new JSONObject(resultDetail);
                JSONArray jArray = jObj.getJSONArray("self");
                for (int i = 0; i < jArray.length(); i++) {
                    // ???????????????JSONObject??????
                    JSONObject jCake = jArray.getJSONObject(i);
                    // ?????????????????????
                    int id = jCake.getInt("id");
                    int cakeId = jCake.getInt("cakeId");
                    String cakeName = jCake.getString("cakeName");
                    String sellerName = jCake.getString("sellerName");
                    String cakeImg = jCake.getString("cakeImg");
                    int size = jCake.getInt("cakeSize");
                    int price = jCake.getInt("cakePrice");
                    int count = jCake.getInt("count");
                    ShoppingCartItemDetail detail = new ShoppingCartItemDetail(id, cakeId, cakeImg, cakeName, sellerName, size, price, count);
                    // ????????????User????????????????????????
                    items.add(detail);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_shoppingcart, container, false);
        shoppingCartItems = root.findViewById(R.id.shopping_cart_items);
        btnAddToOrder = root.findViewById(R.id.sum_add_to_order);
        btnAddToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAllToOreder(ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/AddAllToOrder");
            }
        });
        return root;
    }

    /**
     * ??????????????????????????????
     *
     * @param s
     */
    private void addAllToOreder(final String s) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    JSONArray array = new JSONArray();
                    for (ShoppingCartItemDetail detail : items) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", detail.getId());
                        jsonObject.put("cakeId", detail.getCakeId());
                        jsonObject.put("count", detail.getCakeCount());
                        array.put(jsonObject);
                    }
                    object.put("cakeCount", array);
                    object.put("phone", Customer.getCurrentCustomer());
                    URL url = new URL(s);
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    // ???Json?????????
                    writer.write(object.toString() + "\n");
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    myHandler.sendMessage(message);
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

    @Override
    public void onResume() {
        super.onResume();
        items.clear();
        initShoppingCartDatas();
    }

    /**
     * ?????????????????????
     */
    private void initShoppingCartDatas() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String scUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/" + "GetSelfShoppingCartItems?phone=" + Customer.getCurrentCustomer();
                    URL url = new URL(scUrl);
                    // ??????URL?????????????????????
                    InputStream in = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
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
                }
            }
        }.start();
    }
}
