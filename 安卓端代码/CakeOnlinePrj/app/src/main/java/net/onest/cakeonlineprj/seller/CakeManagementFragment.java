package net.onest.cakeonlineprj.seller;

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
import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.Cake;
import net.onest.cakeonlineprj.beans.Seller;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CakeManagementFragment extends Fragment {
    private ListView sellerCakes;
    private List<Cake> cakes = new ArrayList<>();
    private View root;
    private CakeAdapter sellerCakeAdapter;
    private Button addCake;
    private String resultList;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    resultList = result;
                    ConvertToCakeList info = new ConvertToCakeList();
                    info.start();
                    try {
                        info.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    initSellerCakeFragment();
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cakemanagement, container, false);
        addCake = root.findViewById(R.id.add_cake);
        sellerCakes = root.findViewById(R.id.seller_cakes);
        addCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCakeActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private void downloadSellerCakeDatas() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String downloadImgsUrl = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/" + "DownloadSellerCakeDatas?sellerPhone='" + Seller.getCurrentSeller() + "'";
                    URL url = new URL(downloadImgsUrl);
                    // 通过URL获取网络输入流
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

    @Override
    public void onResume() {
        super.onResume();
        cakes.clear();
        downloadSellerCakeDatas();
    }

    private void initSellerCakeFragment() {
        sellerCakeAdapter = new CakeAdapter(getContext(), cakes, R.layout.cake_managerment_list_item);
        sellerCakes.setAdapter(sellerCakeAdapter);
        sellerCakes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getContext(), CakeManagementActivity.class);
                intent.putExtra("cakeId", cakes.get(position).getId());
                startActivity(intent);
            }
        });
        if (cakes.size() == 0) {
            cakes.clear();
            Toast.makeText(getContext(), "您还没有添加宝贝，快来添加吧~", Toast.LENGTH_SHORT).show();
        }
    }

    private class ConvertToCakeList extends Thread {
            @Override
            public void run() {
                try {
                    JSONObject jObj = new JSONObject(resultList);
                    JSONArray jArray = jObj.getJSONArray("cakes");
                    for (int i = 0; i < jArray.length(); i++) {
                        // 获取当前的JSONObject对象
                        JSONObject jCake = jArray.getJSONObject(i);
                        // 获取当前对象的属性和头像地址
                        int id = jCake.getInt("id");
                        String cakeName = jCake.getString("cakeName");
                        String sellerPhone = jCake.getString("sellerPhone");
                        String description = jCake.getString("description");
                        String cakeImg = jCake.getString("cakeImg");
                        int size = jCake.getInt("cakeSize");
                        int price = jCake.getInt("cakePrice");
                        Cake cake = new Cake(id, sellerPhone, cakeName, price, description, size, cakeImg);
                        // 把当前的User对象添加到集合中
                        cakes.add(cake);
                    }
                    // 拼接图片的服务端资源路径，进行下载
                    for (int j = 0; j < cakes.size(); j++) {
                        Cake cake = cakes.get(j);
                        String cakeImg = cake.getCakeImg();
                        // 拼接服务端地址
                        String netHeader = ConfigUtil.SERVER_ADDR + cakeImg;
                        // 通过网络请求下载
                        URL imgUrl = new URL(netHeader);
                        InputStream imgIn = imgUrl.openStream();
                        String files = getContext().getFilesDir().getAbsolutePath();
                        String imgs = files + "/images";
                        // 判断目录是否存在
                        File dirImgs = new File(imgs);
                        if (!dirImgs.exists()) {
                            // 如果目录不存在，则创建
                            dirImgs.mkdir();
                        }
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
