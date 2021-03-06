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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CakeManagementActivity extends AppCompatActivity {
    private ImageView cakeImg;
    private TextView cakeName;
    private TextView cakePrice;
    private TextView cakeSize;
    private TextView cakeDescription;
    private Button cakeUpdate;
    private Button cakeDelete;
    private int cakeId;
    private Cake cake;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String cakeInfo = (String) msg.obj;
                    convertToCake(cakeInfo);
                    DownloadCakeImage downloadCakeImage = new DownloadCakeImage();
                    downloadCakeImage.start();
                    try {
                        downloadCakeImage.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String files = getApplicationContext().getFilesDir().getAbsolutePath();
                    String imgs = files + "/images";
                    // ??????????????????????????????????????????????????????????????????
                    String[] strs = cake.getCakeImg().split("/");
                    String imgName = strs[strs.length - 1];
                    String imgPath = imgs + "/" + imgName;
                    // ??????user?????????????????????
                    cake.setCakeImg(imgPath);
                    showDatas();
                    break;
                case 2:
                    String result = (String) msg.obj;
                    if ("success".equals(result)) {
                        Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_management);
        Intent intent = getIntent();
        cakeId = intent.getIntExtra("cakeId", 0);
        findViews();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCakeDatas();
    }

    private void showDatas() {
        cakeName.setText(cake.getCakeName());
        cakePrice.setText(cake.getPrice() + "???");
        cakeSize.setText(cake.getSize() + "???");
        cakeDescription.setText(cake.getDescription());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findViews() {
        cakeImg = findViewById(R.id.update_cake_image);
        cakeName = findViewById(R.id.cake_name);
        cakePrice = findViewById(R.id.cake_price);
        cakeSize = findViewById(R.id.cake_size);
        cakeDescription = findViewById(R.id.cake_description);
        cakeUpdate = findViewById(R.id.cake_update);
        cakeDelete = findViewById(R.id.cake_delete);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        cakeUpdate.setOnClickListener(myListener);
        cakeDelete.setOnClickListener(myListener);
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cake_update:
                    // ???????????????????????????
                    Intent intent = new Intent(getApplicationContext(), CakeUpdateActivity.class);
                    intent.putExtra("cake", cake);
                    startActivityForResult(intent, 100);
                    break;
                case R.id.cake_delete:
                    // ?????????????????????
                    delateCake();
                    break;
            }
        }
    }


    private void delateCake() {
        new Thread() {
            @Override
            public void run() {
                String delete = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/DeleteCake?cakeId=" + cakeId;
                try {
                    URL url = new URL(delete);
                    URLConnection conn = url.openConnection();
                    // ?????????????????????
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();   // ?????????????????????
                    reader.close();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            Cake cake = (Cake) data.getSerializableExtra("upCake");
            cakeName.setText(cake.getCakeName());
            cakePrice.setText(cake.getPrice() + "???");
            cakeSize.setText(cake.getSize() + "???");
            if ("".equals(cake.getDescription().trim())) {
                cakeDescription.setText("???????????????????????????????????????????????????~");
            } else {
                cakeDescription.setText(cake.getDescription());
            }
        }
    }

    private void initCakeDatas() {
        new Thread() {
            @Override
            public void run() {
                String getCakeUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/GetSpecificCakeDatas?id=" + cakeId;
                try {
                    URL url = new URL(getCakeUrl);
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

    /**
     * ????????????
     */
    private class DownloadCakeImage extends Thread {
        @Override
        public void run() {
            try {
                // ?????????????????????
                String netHeader = ConfigUtil.SERVER_ADDR + cake.getCakeImg();
                // ????????????????????????
                URL imgUrl = new URL(netHeader);
                InputStream imgIn = imgUrl.openStream();
                String files = getApplicationContext().getFilesDir().getAbsolutePath();
                String imgs = files + "/images";
                // ??????????????????????????????????????????????????????????????????
                String[] strs = cake.getCakeImg().split("/");
                String imgName = strs[strs.length - 1];
                String imgPath = imgs + "/" + imgName;
                // ??????user?????????????????????
                cake.setCakeImg(imgPath);
                // ???????????????????????????
                OutputStream out = new FileOutputStream(cake.getCakeImg());
                // ????????????
                int b = -1;
                while ((b = imgIn.read()) != -1) {
                    out.write(b);
                    out.flush();
                }
                // ?????????
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
