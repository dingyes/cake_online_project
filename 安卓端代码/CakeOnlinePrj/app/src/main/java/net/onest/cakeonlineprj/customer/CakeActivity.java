package net.onest.cakeonlineprj.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.Cake;
import net.onest.cakeonlineprj.beans.Customer;
import net.onest.cakeonlineprj.beans.Order;
import net.onest.cakeonlineprj.beans.ShoppingCartItem;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
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

public class CakeActivity extends AppCompatActivity {
    private final int TOBESENTOUT = 0; // 待发货
    private ImageView cakeImg;
    private TextView cakeName;
    private TextView cakePrice;
    private TextView cakeSize;
    private TextView cakeNum;
    private TextView cakeSeller;
    private TextView cakeDescription;
    private TextView coLeft;
    private TextView coRight;
    private Button addShoppingCart;
    private Button addOrder;
    private Cake cake;
    private int cakeId;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    if ("success".equals(result)) {
                        Toast.makeText(getApplicationContext(), "添加购物车成功", Toast.LENGTH_SHORT).show();
                        cakeNum.setText("1");
                    } else {
                        Toast.makeText(getApplicationContext(), "添加购物车失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    String orderResult = (String) msg.obj;
                    if ("success".equals(orderResult)) {
                        Toast.makeText(getApplicationContext(), "购买成功", Toast.LENGTH_SHORT).show();
                        cakeNum.setText("1");
                    } else {
                        Toast.makeText(getApplicationContext(), "购买失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    String cakeInfo = (String) msg.obj;
                    convertToCake(cakeInfo);
                    DownloadCakeImage downloadCakeImage = new DownloadCakeImage();
                    downloadCakeImage.start();
                    try {
                        downloadCakeImage.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    showDatas();
                    break;
            }
        }
    };

    private void showDatas() {
        cakeName.setText(cake.getCakeName());
        cakePrice.setText(cake.getPrice() + "");
        cakeSize.setText(cake.getSize() + "寸");
        cakeSeller.setText(cake.getSellerPhone());
        if ("".equals(cake.getDescription()) || null == cake.getDescription()){
            cakeDescription.setText("    卖家很懒，什么描述都没有留下呢~");
        } else {
            cakeDescription.setText("    " + cake.getDescription());
        }
        try {
            InputStream read = new FileInputStream(cake.getCakeImg());
            Bitmap img = BitmapFactory.decodeStream(read);
            Bitmap bitmap = ConfigUtil.zoomBitmap(img, 90, 90);
            cakeImg.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void convertToCake(String cakeInfo) {
        try {
            JSONObject jCake = new JSONObject(cakeInfo);
            int id = jCake.getInt("id");
            String cakeName = jCake.getString("cakeName");
            String sellerPhone = jCake.getString("sellerPhone");
            String description = jCake.getString("description");
            String cakeImg = jCake.getString("cakeImg");
            int size = jCake.getInt("cakeSize");
            int price = jCake.getInt("cakePrice");
            cake = new Cake(id, sellerPhone, cakeName, price, description, size, cakeImg);
            String files = getApplicationContext().getFilesDir().getAbsolutePath();
            String imgs = files + "/images";
            // 获取图片的名称（不包含服务器路径的图片名称）
            String[] strs = cake.getCakeImg().split("/");
            String imgName = strs[strs.length - 1];
            String imgPath = imgs + "/" + imgName;
            // 修改user对象的头像地址
            cake.setCakeImg(imgPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake);
        Intent intent = getIntent();
        cakeId = intent.getIntExtra("cakeId", 0);
        findViews();
        setListener();
        initCakeDatas();
    }

    private void findViews() {
        cakeImg = findViewById(R.id.cake_img);
        cakeName = findViewById(R.id.cake_name);
        cakePrice = findViewById(R.id.cake_price);
        cakeSize = findViewById(R.id.cake_size);
        cakeNum = findViewById(R.id.cake_num);
        cakeSeller = findViewById(R.id.cake_seller);
        cakeDescription = findViewById(R.id.cake_description);
        coLeft = findViewById(R.id.co_left);
        coRight = findViewById(R.id.co_right);
        addShoppingCart = findViewById(R.id.add_shopping_cart);
        addOrder = findViewById(R.id.add_order);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        coLeft.setOnClickListener(myListener);
        coRight.setOnClickListener(myListener);
        addShoppingCart.setOnClickListener(myListener);
        addOrder.setOnClickListener(myListener);
    }

    private void initCakeDatas() {
        new Thread() {
            @Override
            public void run() {
                String getCakeUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/GetSpecificCakeDatas?id=" + cakeId;
                try {
                    URL url = new URL(getCakeUrl);
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

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int number = 1;
            switch (v.getId()) {
                case R.id.co_left:
                    // 商品数量减一
                    number = Integer.parseInt(cakeNum.getText().toString());
                    if (number == 1) {
                        Toast.makeText(getApplicationContext(), "蛋糕数量不能再减少啦", Toast.LENGTH_SHORT).show();
                    } else {
                        number--;
                        cakeNum.setText(number + "");
                    }
                    break;
                case R.id.co_right:
                    // 商品数量加一
                    number = Integer.parseInt(cakeNum.getText().toString());
                    number++;
                    cakeNum.setText(number + "");
                    break;
                case R.id.add_shopping_cart:
                    // 添加到购物车
                    addToShoppingCart();
                    break;
                case R.id.add_order:
                    // 下单成功
                    addToOrder();
                    break;
            }
        }
    }

    /**
     * 下单操作
     */
    private void addToOrder() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String addOrderUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/" + "AddToOrder";
                    URL url = new URL(addOrderUrl);
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    // 获取数据
                    // 获取输入流与输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    String customerPhone = Customer.getCurrentCustomer();
                    int cakeId = cake.getId();
                    int count = Integer.parseInt(cakeNum.getText().toString());
                    Order order = new Order(customerPhone, cakeId, count, TOBESENTOUT);
                    String info = convertOrderJson(order);
                    // 将Json串写入
                    writer.write(info);
                    writer.flush();
                    // 获取客户端信息
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();   // 买家对象的Json串
                    reader.close();
                    out.close();
                    Message message = new Message();
                    message.what = 2;
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

    /**
     * 添加到购物车操作
     */
    private void addToShoppingCart() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String addShoppingCartUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/" + "AddToShppingCart";
                    URL url = new URL(addShoppingCartUrl);
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    // 获取数据
                    // 获取输入流与输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    String customerPhone = Customer.getCurrentCustomer();
                    int cakeId = cake.getId();
                    int count = Integer.parseInt(cakeNum.getText().toString());
                    ShoppingCartItem item = new ShoppingCartItem(cakeId, customerPhone, count);
                    String info = convertShoppingCartJson(item);
                    // 将Json串写入
                    writer.write(info);
                    writer.flush();
                    // 获取客户端信息
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();   // 买家对象的Json串
                    reader.close();
                    out.close();
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

    /**
     * 将购物车商品对象转换为json串
     *
     * @param item 购物车商品对象
     * @return
     */
    private String convertShoppingCartJson(ShoppingCartItem item) {
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("customerPhone", item.getCustomerPhone());
            jObj.put("cakeId", item.getCakeId());
            jObj.put("cakeCount", item.getCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObj.toString();
    }

    /**
     * 将订单对象转化为json串
     *
     * @param order
     * @return
     */
    private String convertOrderJson(Order order) {
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("customerPhone", order.getCustomerPhone());
            jObj.put("cakeId", order.getCakeId());
            jObj.put("cakeCount", order.getCount());
            jObj.put("status", order.getStatus());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObj.toString();
    }

    /**
     * 下载图片
     */
    private class DownloadCakeImage extends Thread {
        @Override
        public void run() {
            try {
                // 拼接图片的服务端资源路径，进行下载
                String cakeImg = cake.getCakeImg();
                // 拼接服务端地址
                String netHeader = ConfigUtil.SERVER_ADDR + cakeImg;
                Log.e("image", netHeader);
                // 通过网络请求下载
                URL imgUrl = new URL(netHeader);
                InputStream imgIn = imgUrl.openStream();
                String files = getFilesDir().getAbsolutePath();
                String imgs = files + "/images";
                // 获取图片的名称（不包含服务器路径的图片名称）
                String[] strs = cake.getCakeImg().split("/");
                String imgName = strs[strs.length - 1];
                String imgPath = imgs + "/" + imgName;
                // 修改user对象的头像地址
                cake.setCakeImg(imgPath);
                // 获取本地文件输出流
                OutputStream out = new FileOutputStream(cake.getCakeImg());
                // 循环读写
                int b = -1;
                while ((b = imgIn.read()) != -1) {
                    out.write(b);
                    out.flush();
                }
                // 关闭流
                imgIn.close();
                out.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
