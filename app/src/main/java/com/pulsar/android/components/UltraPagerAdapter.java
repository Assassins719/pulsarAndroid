package com.pulsar.android.components;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pulsar.android.R;

public class UltraPagerAdapter extends PagerAdapter {
    private boolean isMultiScr;
    private Context mContext;
    public UltraPagerAdapter(boolean isMultiScr, Context mContext) {
        this.isMultiScr = isMultiScr;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RelativeLayout linearLayout = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.card_slider_child, null);
        ImageView img = linearLayout.findViewById(R.id.img_card);
        linearLayout.setId(R.id.item_id);
        switch (position) {
            case 0:
                img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_card_1));
                break;
            case 1:
                img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_card_2));
                break;
            case 2:
                img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_card_3));
                break;
        }
        container.addView(linearLayout);
        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view = (LinearLayout) object;
        container.removeView(view);
    }
}
