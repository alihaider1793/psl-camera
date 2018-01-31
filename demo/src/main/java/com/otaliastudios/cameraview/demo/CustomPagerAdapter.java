package com.otaliastudios.cameraview.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by haide on 12/12/2017.
 */

public class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    String mUri;
    ImageView save;
    ImageView imageView;
    ViewGroup layout;

    public CustomPagerAdapter(Context context, String uri){
        mContext = context;
        mUri = uri;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position){
        ModelObject modelObject = ModelObject.values()[position];
        ModelObject_land modelObject_land = ModelObject_land.values()[position];

        LayoutInflater inflater = LayoutInflater.from(mContext);

        SharedPreferences sh = mContext.getSharedPreferences("orient", MODE_PRIVATE);
        boolean b = sh.getBoolean("orient", false);

        if(!b) {
            layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        } else if(b){
            layout = (ViewGroup) inflater.inflate(modelObject_land.getLayoutResId(), collection, false);

        }

        collection.addView(layout);
        return layout;

    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view){
        collection.removeView((View)view);
    }

    @Override
    public int getCount(){
        return ModelObject.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position){
        ModelObject customPagerEnum = ModelObject.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());

    }
}
