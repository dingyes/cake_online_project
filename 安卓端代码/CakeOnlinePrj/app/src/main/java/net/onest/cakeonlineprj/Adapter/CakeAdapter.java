package net.onest.cakeonlineprj.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.onest.cakeonlineprj.R;
import net.onest.cakeonlineprj.beans.Cake;
import net.onest.cakeonlineprj.util.ConfigUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CakeAdapter extends BaseAdapter {
    private Context cakeContext;
    private List<Cake> cakes = new ArrayList<>();
    private int itemLayoutRes;

    public CakeAdapter(Context cakeContext, List<Cake> cakes, int itemLayoutRes) {
        this.cakeContext = cakeContext;
        this.cakes = cakes;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if (null != cakes) {
            return cakes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != cakes) {
            return cakes.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(cakeContext);
        convertView = inflater.inflate(itemLayoutRes, null);
        ImageView cakeImg = convertView.findViewById(R.id.cake_img);
        TextView cakeName = convertView.findViewById(R.id.cake_name);
        TextView cakeSize = convertView.findViewById(R.id.cake_size);
        TextView cakePrice = convertView.findViewById(R.id.cake_price);
        cakeName.setText(cakes.get(position).getCakeName());
        cakeSize.setText(cakes.get(position).getSize() + "???");
        cakePrice.setText(cakes.get(position).getPrice() + "");
        Glide.with(cakeContext).load(ConfigUtil.SERVER_ADDR + "/" + cakes.get(position).getCakeImg())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)  // ???????????????????????????
                .placeholder(R.mipmap.loading)
                .error(R.drawable.photo) // ????????????????????????????????????
                .fallback(R.drawable.photo)  // ?????????????????????null??????????????????
                .into(cakeImg);
        return convertView;
    }
}
