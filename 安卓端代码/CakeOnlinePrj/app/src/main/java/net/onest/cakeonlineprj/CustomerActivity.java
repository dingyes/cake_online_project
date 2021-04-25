package net.onest.cakeonlineprj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.os.Bundle;
import android.widget.TabHost;

import net.onest.cakeonlineprj.customer.CakeFragment;
import net.onest.cakeonlineprj.customer.MineFragment;
import net.onest.cakeonlineprj.customer.ShoppingCartFragment;

public class CustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        initCustomerFragmentTabHost();
    }

    private void initCustomerFragmentTabHost() {
        FragmentTabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1").setIndicator("商品");
        tabHost.addTab(tab1, CakeFragment.class, null);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2").setIndicator("购物车");
        tabHost.addTab(tab2, ShoppingCartFragment.class, null);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3").setIndicator("我");
        tabHost.addTab(tab3, MineFragment.class, null);
        //设置默认选中的标签页:参数是下标
        tabHost.setCurrentTab(2);
    }
}
