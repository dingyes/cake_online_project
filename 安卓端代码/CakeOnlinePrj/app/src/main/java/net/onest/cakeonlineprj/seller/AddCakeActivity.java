package net.onest.cakeonlineprj.seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.Seller;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class AddCakeActivity extends AppCompatActivity {
    private final int PHOTO_REQUEST = 1;
    private ImageView addCakeImage;
    private Button btnAddImage;
    private TextView addCakeName;
    private TextView addCakePrice;
    private TextView addCakeDeacription;
    private Button btnCakeSubmit;
    private RadioButton size6;
    private RadioButton size8;
    private RadioButton size10;
    private RadioButton size12;
    private RadioButton size14;
    private int cakeSize = 6;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    if ("success".equals(result.trim())) {
                        Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    String result1 = (String) msg.obj;
                    if ("success".equals(result1.trim())) {
                        Toast.makeText(getApplicationContext(), "图片上传成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cake);
        findViews();
        setListener();
    }

    private void findViews() {
        addCakeImage = findViewById(R.id.add_cake_image);
        btnAddImage = findViewById(R.id.btn_add_img);
        addCakeName = findViewById(R.id.add_cake_name);
        addCakePrice = findViewById(R.id.add_cake_price);
        addCakeDeacription = findViewById(R.id.add_cake_description);
        btnCakeSubmit = findViewById(R.id.add_cake_submit);
        size6 = findViewById(R.id.size6);
        size8 = findViewById(R.id.size8);
        size10 = findViewById(R.id.size10);
        size12 = findViewById(R.id.size12);
        size14 = findViewById(R.id.size14);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        size6.setOnClickListener(myListener);
        size8.setOnClickListener(myListener);
        size10.setOnClickListener(myListener);
        size12.setOnClickListener(myListener);
        size14.setOnClickListener(myListener);
        btnAddImage.setOnClickListener(myListener);
        btnCakeSubmit.setOnClickListener(myListener);
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_cake_submit:
                    // 对输入数据进行判断
                    if ("".equals(addCakeName.getText().toString().trim())) {
                        Toast.makeText(getApplicationContext(), "蛋糕名称不能为空哦~", Toast.LENGTH_SHORT).show();
                    } else {
                        if ("".equals(addCakePrice.getText().toString().trim())) {
                            Toast.makeText(getApplicationContext(), "请输入价格呢~", Toast.LENGTH_SHORT).show();
                        } else {
                            AddCake addCake = new AddCake();
                            addCake.start();
                            try {
                                addCake.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case R.id.size6:
                    cakeSize = 6;
                    size6.setChecked(true);
                    break;
                case R.id.size8:
                    cakeSize = 8;
                    size8.setChecked(true);
                    break;
                case R.id.size10:
                    cakeSize = 10;
                    size10.setChecked(true);
                    break;
                case R.id.size12:
                    cakeSize = 12;
                    size12.setChecked(true);
                    break;
                case R.id.size14:
                    cakeSize = 14;
                    size14.setChecked(true);
                    break;
                case R.id.btn_add_img:
                    Intent intent = new Intent();
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, PHOTO_REQUEST);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PHOTO_REQUEST) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    Bitmap raw = BitmapFactory.decodeStream(in);
                    Bitmap bitmap = ConfigUtil.zoomBitmap(raw, 160, 160);
                    addCakeImage.setImageBitmap(bitmap);
                    AddCakeImg img = new AddCakeImg(uri);
                    img.start();
                    img.join();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class AddCakeImg extends Thread {
        private Uri uri;

        public AddCakeImg(Uri uri) {
            this.uri = uri;
        }

        @Override
        public void run() {
            try {
                InputStream in = getContentResolver().openInputStream(uri);
                URL url = new URL(ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/AddCakeImg");
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                int b = -1;
                while ((b = in.read()) != -1) {
                    out.write(b);
                    out.flush();
                }
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8"));
                String info = reader.readLine();
                out.close();
                in.close();
                reader.close();
                Message message = new Message();
                message.what = 2;
                message.obj = info;
                myHandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class AddCake extends Thread {
        @Override
        public void run() {
            JSONObject object = new JSONObject();
            try {
                object.put("sellerPhone", Seller.getCurrentSeller());
                object.put("cakeName", addCakeName.getText().toString());
                object.put("cakePrice", Integer.parseInt(addCakePrice.getText().toString()));
                object.put("cakeSize", cakeSize);
                object.put("cakeDescription", addCakeDeacription.getText().toString());
                String path = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/AddCake";
                URL url = new URL(path);
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // 获取数据
                // 获取输入流与输出流
                OutputStream out = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                // 将Json串写入
                writer.write(object.toString() + "\n");
                writer.flush();
                // 发送消息
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String result = reader.readLine();
                reader.close();
                in.close();
                Message message = new Message();
                message.what = 1;
                message.obj = result;
                myHandler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
