package net.onest.cakeonlineprj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.os.Bundle;
import android.widget.TabHost;

import net.onest.cakeonlineprj.seller.CakeManagementFragment;
import net.onest.cakeonlineprj.seller.OrderManagementFragment;

public class SellerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        initSellerFragmentTabHost();
    }

    private void initSellerFragmentTabHost() {
        FragmentTabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1").setIndicator("商品管理");
        tabHost.addTab(tab1, CakeManagementFragment.class, null);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3").setIndicator("订单管理");
        tabHost.addTab(tab3, OrderManagementFragment.class, null);
        //设置默认选中的标签页:参数是下标
        tabHost.setCurrentTab(0);
    }
}
