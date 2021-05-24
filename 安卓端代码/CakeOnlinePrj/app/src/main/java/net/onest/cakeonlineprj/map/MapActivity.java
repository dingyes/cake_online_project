package net.onest.cakeonlineprj.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.Cake;
import net.onest.cakeonlineprj.beans.ShopPosition;
import net.onest.cakeonlineprj.util.ConfigUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locClient;
    private LocationClientOption locOption;
    private String info;
    private List<ShopPosition> positions = new ArrayList<>();
    private Handler handler;

    private void showMarkerOptions() {
        // 定义标记覆盖物对象
        List<OverlayOptions> markerOptionsList = new ArrayList<>();
        for (ShopPosition position : positions) {
            // 创建标注覆盖物对象，并添加到集合中
            OverlayOptions option = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.shop))
                    .position(new LatLng(position.getLatitude(), position.getLongitude()));
            markerOptionsList.add(option);
        }
        // 在地图上显示覆盖物集合
        baiduMap.addOverlays(markerOptionsList);
    }

    private class ConvertToPositions extends Thread {
        @Override
        public void run() {
            positions.clear();
            try {
                JSONObject jObj = new JSONObject(info);
                JSONArray jArray = jObj.getJSONArray("positions");
                for (int i = 0; i < jArray.length(); i++) {
                    // 获取当前的JSONObject对象
                    JSONObject jPosition = jArray.getJSONObject(i);
                    // 获取当前对象的属性和头像地址
                    double latitude = jPosition.getDouble("latitude");
                    double longitude = jPosition.getDouble("longitude");
                    ShopPosition position = new ShopPosition(latitude, longitude);
                    positions.add(position);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map_view);
        HandlerThread thread = new HandlerThread("MyThread");
        thread.start();
        handler = new Handler(thread.getLooper()) {
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        info = (String) msg.obj;
                        ConvertToPositions toPositions = new ConvertToPositions();
                        toPositions.start();
                        try {
                            toPositions.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        showMarkerOptions();
                        break;
                }
            }
        };
        // 显示定位
        showLocationInfo();
        // 显示默认比例尺与比例尺范围
        zoomOperation();
        // 显示商店位置
        showPositions();
    }

    /**
     * 获得所有商店的经纬度坐标
     */
    private void showPositions() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String urlPath = ConfigUtil.SERVER_ADDR + "/" + ConfigUtil.NET_HOME + "/GetShopPositions";
                    Log.e("url", urlPath);
                    URL url = new URL(urlPath);
                    URLConnection conn = url.openConnection();
                    //获取字节输入流
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    Log.e("url", result);
                    reader.close();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 显示普通地图与定位
     */
    public void showLocationInfo() {
        // 获取百度地图控制器
        baiduMap = mapView.getMap();
        // 显示普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 获取定位客户端类的对象
        locClient = new LocationClient(getApplicationContext());
        // 获取定位客户端选项类的对象
        locOption = new LocationClientOption();
        // 1、设置定位参数
        // 打开GPS
        locOption.setOpenGps(true);
        // 设置扫描间隔时间
        locOption.setScanSpan(1000);
        // 设置定位模式(高精度定位模式)
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 设置坐标系
        locOption.setCoorType("wgs84");
        // 设置是否需要地址信息
        locOption.setIsNeedAddress(true);
        // 2、将设置的定位参数应用给客户端
        locClient.setLocOption(locOption);
        // 3、启动定位
        locClient.start();
        // 开启图层定位
        baiduMap.setMyLocationEnabled(true);
        // 4、注册定位监听器（为了将定位的数据显示在地图上）
        locClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // 获取纬度信息
                double lat = bdLocation.getLatitude();
                // 获取经度信息
                double lng = bdLocation.getLongitude();
                LatLng position = new LatLng(lat, lng);
                // 获取当前定位时间
                String locTime = bdLocation.getTime();
                Log.e("dy", locTime);
                // 转换成百度坐标
                LatLng baiduLatLng = GlobalTool.transGpsToBaiduLatLng(position);
                // 在地图上显示当前位置
                showLobPositionOnMap(baiduLatLng);
            }
        });
    }

    /**
     * 将定位的坐标显示在地图上
     *
     * @param baiduLatLng
     */
    private void showLobPositionOnMap(LatLng baiduLatLng) {
        // 1、设置坐标显示的图标信息
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.COMPASS,  // 显示位置罗盘态
                true,  // 是否允许显示方向信息
                BitmapDescriptorFactory.fromResource(R.mipmap.water)
        );
        // 2、设置显示的样式
        baiduMap.setMyLocationConfiguration(config);
        // 设置定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .latitude(baiduLatLng.latitude)
                .longitude(baiduLatLng.longitude)
                .build();
        // 设置定位位置
        baiduMap.setMyLocationData(locData);
        // 3、把地图移动到定位位置
        // 创建MapStatusUpdate对象
        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLng(baiduLatLng);
        // 给地图应用当前状态更新
        baiduMap.animateMapStatus(statusUpdate);
    }

    /**
     * 设置默认比例尺与比例尺范围
     */
    private void zoomOperation() {
        // 设置是否显示比例尺
        mapView.showZoomControls(true);
        // 设置比例尺放大缩小的范围
        baiduMap.setMaxAndMinZoomLevel(19, 13);
        // 设置默认比例尺大小
        // 1、创建地图状态更新对象
        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.zoomTo(16);
        // 2、通过百度地图控制器应用状态更新
        baiduMap.setMapStatus(statusUpdate);
    }
}
