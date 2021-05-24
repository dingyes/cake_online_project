package net.onest.cakeonlineprj.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import net.onest.cakeonlineprj.R;

public class OrderDetailActivity extends AppCompatActivity {
    private ImageView img;
    private TextView orderName;
    private TextView orderPrice;
    private TextView orderSize;
    private TextView orderNum;
    private TextView orderStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        findViews();
        showDatas();
    }

    private void showDatas() {
        img.setImageDrawable(getResources().getDrawable(R.drawable.g8));
        orderName.setText("巧克力蛋糕");
        orderNum.setText("2");
        orderPrice.setText("111");
        orderSize.setText("10寸");
        orderStatus.setText("已发货");
        orderStatus.setTextColor(Color.BLUE);
    }

    private void findViews() {
        img = findViewById(R.id.order_img);
        orderName = findViewById(R.id.order_name);
        orderSize = findViewById(R.id.order_size);
        orderPrice = findViewById(R.id.order_price);
        orderNum = findViewById(R.id.order_num);
        orderStatus = findViewById(R.id.order_status);
    }
}
