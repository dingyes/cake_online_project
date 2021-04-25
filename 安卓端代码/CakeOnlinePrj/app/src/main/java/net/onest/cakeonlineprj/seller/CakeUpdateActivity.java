package net.onest.cakeonlineprj.seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import net.onest.cakeonlineprj.beans.Cake;
import net.onest.cakeonlineprj.beans.Seller;
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

public class CakeUpdateActivity extends AppCompatActivity {
    private TextView cakeName;
    private TextView cakePrice;
    private TextView cakeDescription;
    private Button updateSubmit;
    private Cake cake;
    private Cake upCake;
    private int cakeSize;
    private Button btnUpdateImg;
    private ImageView ivUpdateImg;
    private final int PHOTO_REQUEST = 1;
    private RadioButton size6;
    private RadioButton size8;
    private RadioButton size10;
    private RadioButton size12;
    private RadioButton size14;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    if ("success".equals(result)) {
                        Toast.makeText(getApplicationContext(), "更新信息成功", Toast.LENGTH_SHORT).show();
                        Intent response = new Intent();
                        response.putExtra("upCake", upCake);
                        setResult(200, response);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "更新信息失败", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_cake_update);
        Intent intent = getIntent();
        cake = (Cake) intent.getSerializableExtra("cake");
        findViews();
        setListener();
        showDatas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    Bitmap raw = BitmapFactory.decodeStream(in);
                    Bitmap bitmap = ConfigUtil.zoomBitmap(raw, 160, 160);
                    ivUpdateImg.setImageBitmap(bitmap);
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
                URL url = new URL(ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/UpdateImg");
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

    private void showDatas() {
        cakeName.setText(cake.getCakeName());
        cakePrice.setText(cake.getPrice() + "");
        cakeDescription.setText(cake.getDescription());
        int size = cake.getSize();
        if (size == 6) {
            cakeSize = 6;
            size6.setChecked(true);
        } else if (size == 8) {
            cakeSize = 8;
            size8.setChecked(true);
        } else if (size == 10) {
            cakeSize = 10;
            size10.setChecked(true);
        } else if (size == 12) {
            cakeSize = 12;
            size12.setChecked(true);
        } else if (size == 14) {
            cakeSize = 14;
            size14.setChecked(true);
        }
        try {
            InputStream read = new FileInputStream(cake.getCakeImg());
            Bitmap img = BitmapFactory.decodeStream(read);
            Bitmap bitmap = ConfigUtil.zoomBitmap(img, 150, 150);
            ivUpdateImg.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void findViews() {
        cakeName = findViewById(R.id.update_cake_name);
        cakePrice = findViewById(R.id.update_cake_price);
        cakeDescription = findViewById(R.id.update_cake_description);
        updateSubmit = findViewById(R.id.update_submit);
        size6 = findViewById(R.id.size6);
        size8 = findViewById(R.id.size8);
        size10 = findViewById(R.id.size10);
        size12 = findViewById(R.id.size12);
        size14 = findViewById(R.id.size14);
        btnUpdateImg = findViewById(R.id.btn_add_img);
        ivUpdateImg = findViewById(R.id.add_cake_image);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        updateSubmit.setOnClickListener(myListener);
        size6.setOnClickListener(myListener);
        size8.setOnClickListener(myListener);
        size10.setOnClickListener(myListener);
        size12.setOnClickListener(myListener);
        size14.setOnClickListener(myListener);
        btnUpdateImg.setOnClickListener(myListener);
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.update_submit:
                    // 对输入数据进行判断
                    if ("".equals(cakeName.getText().toString().trim())) {
                        Toast.makeText(getApplicationContext(), "蛋糕名称不能为空哦~", Toast.LENGTH_SHORT).show();
                    } else {
                        if ("".equals(cakePrice.getText().toString().trim())) {
                            Toast.makeText(getApplicationContext(), "请输入价格呢~", Toast.LENGTH_SHORT).show();
                        } else {
                            upCake = new Cake(cake.getId(), Seller.getCurrentSeller(), cakeName.getText().toString(), Integer.parseInt(cakePrice.getText().toString()), cakeDescription.getText().toString(), cakeSize, cake.getCakeImg());
                            updateCake(upCake);
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
                    // 更新图片
                    Intent intent1 = new Intent();
                    intent1.addCategory(Intent.CATEGORY_OPENABLE);
                    intent1.setType("image/*");
                    startActivityForResult(intent1, PHOTO_REQUEST);
                    break;
            }
        }
    }

    private void updateCake(final Cake uCake) {
        new Thread() {
            @Override
            public void run() {
                try {
                    // 获取数据
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cakeId", cake.getId());
                    jsonObject.put("cakeName", uCake.getCakeName());
                    jsonObject.put("cakePrice", uCake.getPrice());
                    jsonObject.put("cakeSize", uCake.getSize());
                    jsonObject.put("cakeDescription", uCake.getDescription());
                    String updateDatas = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/UpdateCakeDatas";
                    URL url = new URL(updateDatas);
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    // 获取数据
                    // 获取输入流与输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(jsonObject.toString() + "\n");
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    myHandler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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
