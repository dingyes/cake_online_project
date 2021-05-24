package net.onest.cakeonlineprj.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import net.onest.cakeonlineprj.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.wv_view);
        webView.loadUrl("https://www.jiguang.cn/jpush2/#/app/30713e9362c0b0bd8e12abb3/push_form/notification");
    }
}
