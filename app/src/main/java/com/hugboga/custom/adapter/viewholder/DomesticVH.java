package com.hugboga.custom.adapter.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 国内信用卡ViewHolder
 * Created by HONGBO on 2017/10/23 15:31.
 */

public class DomesticVH extends RecyclerView.ViewHolder {

    @Bind(R.id.domestic_item_img)
    ImageView imageView; //银行卡icon

    public DomesticVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * 显示数据
     */
    public void init() {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(MyApplication.getAppContext().getAssets().open("CMB.png"));
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
