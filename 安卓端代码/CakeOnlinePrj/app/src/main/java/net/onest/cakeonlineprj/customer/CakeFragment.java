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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.onest.cakeonlineprj.Adapter.CakeAdapter;
import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.Cake;
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

public class CakeFragment extends Fragment {
    private EditText keyResearch;
    private Button btnResearch;
    private RadioButton size6;
    private RadioButton size8;
    private RadioButton size10;
    private RadioButton size12;
    private RadioButton size14;
    private RadioButton price100;
    private RadioButton price150;
    private RadioButton price200;
    private RadioButton price300;
    private CakeAdapter cakeAdapter;
    private GridView customerCakes;
    private List<Cake> cakes = new ArrayList<>();
    private int cakeSize = 0;
    private int cakePrice = 0;
    private Button btnFresh;
    private View root;
    private String cakesInfo;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    cakesInfo = (String) msg.obj;
                    ConvertToCakeList toCakeList = new ConvertToCakeList();
                    toCakeList.start();
                    try {
                        toCakeList.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    initCakeFragment();
                    break;
                case 2: // ??????????????????????????????
                    cakes.clear();
                    cakesInfo = (String) msg.obj;
                    ConvertToCakeList toList = new ConvertToCakeList();
                    toList.start();
                    try {
                        toList.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    initCakeFragment();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cake, container, false);
        findViews();
        setListener();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // ????????????
        initCakeDatas();
    }

    /**
     * ??????adapter
     */
    private void initCakeFragment() {
        cakeAdapter = new CakeAdapter(getContext(), cakes, R.layout.cake_list_item);
        customerCakes = root.findViewById(R.id.customer_cakes);
        customerCakes.setAdapter(cakeAdapter);
        customerCakes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getContext(), CakeActivity.class);
                intent.putExtra("cakeId", cakes.get(position).getId());
                startActivity(intent);
            }
        });
        if (cakes.size() == 0) {
            cakes.clear();
            cakeAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "??????????????????~", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ??????????????????
     */
    private void downloadCakeDatas() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String downloadImgsUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/" + "DownloadCakeDatas";
                    URL url = new URL(downloadImgsUrl);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void findViews() {
        keyResearch = root.findViewById(R.id.key_research);
        btnResearch = root.findViewById(R.id.btn_research);
        size6 = root.findViewById(R.id.size6);
        size8 = root.findViewById(R.id.size8);
        size10 = root.findViewById(R.id.size10);
        size12 = root.findViewById(R.id.size12);
        size14 = root.findViewById(R.id.size14);
        price100 = root.findViewById(R.id.price100);
        price150 = root.findViewById(R.id.price150);
        price200 = root.findViewById(R.id.price200);
        price300 = root.findViewById(R.id.price300);
        btnFresh = root.findViewById(R.id.btn_fresh);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        btnResearch.setOnClickListener(myListener);
        size6.setOnClickListener(myListener);
        size8.setOnClickListener(myListener);
        size10.setOnClickListener(myListener);
        size12.setOnClickListener(myListener);
        size14.setOnClickListener(myListener);
        price100.setOnClickListener(myListener);
        price150.setOnClickListener(myListener);
        price200.setOnClickListener(myListener);
        price300.setOnClickListener(myListener);
        btnFresh.setOnClickListener(myListener);
    }

    private class ConvertToCakeList extends Thread {
        @Override
        public void run() {
            try {
                JSONObject jObj = new JSONObject(cakesInfo);
                JSONArray jArray = jObj.getJSONArray("cakes");
                for (int i = 0; i < jArray.length(); i++) {
                    // ???????????????JSONObject??????
                    JSONObject jCake = jArray.getJSONObject(i);
                    // ??????????????????????????????????????????
                    int id = jCake.getInt("id");
                    String cakeName = jCake.getString("cakeName");
                    String sellerPhone = jCake.getString("sellerPhone");
                    String description = jCake.getString("description");
                    String cakeImg = jCake.getString("cakeImg");
                    int size = jCake.getInt("cakeSize");
                    int price = jCake.getInt("cakePrice");
                    Cake cake = new Cake(id, sellerPhone, cakeName, price, description, size, cakeImg);
                    // ????????????User????????????????????????
                    cakes.add(cake);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
                case R.id.price100:
                    cakePrice = 100;
                    price100.setChecked(true);
                    break;
                case R.id.price150:
                    cakePrice = 150;
                    price150.setChecked(true);
                    break;
                case R.id.price200:
                    cakePrice = 200;
                    price200.setChecked(true);
                    break;
                case R.id.price300:
                    cakePrice = 300;
                    price300.setChecked(true);
                    break;
                case R.id.btn_research:
                    if (((keyResearch.length() == 0) || "".equals(keyResearch.getText().toString().trim())) && (cakeSize == 0) && (cakePrice == 0)) {
                        Toast.makeText(getContext(), "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        KeywordSearchCake searchCake = new KeywordSearchCake();
                        searchCake.start();
                        try {
                            searchCake.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.btn_fresh:
                    // ????????????????????????
                    initCakeDatas();
                    break;
            }
        }
    }

    /**
     * ??????????????????
     */
    private void initCakeDatas() {
        if (keyResearch.length() > 0) {
            keyResearch.setText("");
        }
        cakes.clear();
        size6.setChecked(false);
        size8.setChecked(false);
        size10.setChecked(false);
        size12.setChecked(false);
        size14.setChecked(false);
        price100.setChecked(false);
        price150.setChecked(false);
        price200.setChecked(false);
        price300.setChecked(false);
        cakeSize = 0;
        cakePrice = 0;
        cakes.clear();
        downloadCakeDatas();
    }

    /**
     * ?????????????????????
     */
    class KeywordSearchCake extends Thread {
        @Override
        public void run() {
            try {
                String keyUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/KeywordSearchCake";
                URL url = new URL(keyUrl);
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // ????????????
                // ???????????????????????????
                OutputStream out = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                // ???Json?????????
                String json = convertToJson();
                writer.write(json);
                writer.flush();
                // ?????????????????????
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String result = reader.readLine();   // ?????????????????????
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
    }

    /**
     * ??????????????????Json???
     *
     * @return
     */
    private String convertToJson() {
        String json = null;
        try {
            JSONObject jObj = new JSONObject();
            jObj.put("key", keyResearch.getText().toString());
            jObj.put("size", cakeSize);
            jObj.put("price", cakePrice);
            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
