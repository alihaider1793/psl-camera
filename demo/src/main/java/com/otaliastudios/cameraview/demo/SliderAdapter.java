package com.otaliastudios.cameraview.demo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Nabeel on 1/31/2018.
 */
public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;


    SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slides_images = {

            R.drawable.ic_edit,
            R.drawable.ic_save_icon,
            R.drawable.share_icon

    };


    public String[] slides_headings = {

            "Message",
            "Camera",
            "Mobile Phone"

    };

    public String[] slides_desc = {

            "1",
            "2",
            "3"
    };
    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return slides_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater =  (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.slidelayout, container, false);

        TextView heading_tv = (TextView) v.findViewById(R.id.heading);
        TextView content_tv = (TextView) v.findViewById(R.id.content);

        ImageView img = (ImageView) v.findViewById(R.id.imgview);

        img.setImageResource(slides_images[position]);

        heading_tv.setText(slides_headings[position]);
        content_tv.setText(slides_desc[position]);

        container.addView(v);

        return v;
    }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position  The page position to be removed.
     * @param object    The same object that was returned by
     *                  {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}