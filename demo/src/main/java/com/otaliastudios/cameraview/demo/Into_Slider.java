package com.otaliastudios.cameraview.demo;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Into_Slider extends AppCompatActivity {

    LinearLayout layout;
    ViewPager viewPager;

    TextView[] dots;

    SliderAdapter sliderAdapter;

    Button next, Previous;

    int current_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into__slider);

        layout = findViewById(R.id.dots);
        viewPager = findViewById(R.id.viewpager);

        sliderAdapter =  new SliderAdapter(this);

        next = findViewById(R.id.next);
        Previous = findViewById(R.id.prev);

        viewPager.setAdapter(sliderAdapter);

        adDots(0);

        viewPager.addOnPageChangeListener(viewlistener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(viewPager.getCurrentItem() == 2){

                    startActivity(new Intent(Into_Slider.this, CameraActivity.class));

                }else {
                    viewPager.setCurrentItem(current_page + 1);
                }
            }
        });

        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewPager.getCurrentItem() == 0){

                    Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();

                }else {
                    viewPager.setCurrentItem(current_page - 1);

                }
            }
        });
    }

    private void adDots(int position) {

        dots = new TextView[3];
        layout.removeAllViews();

        for(int i = 0; i<dots.length; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(android.R.color.black));

            layout.addView(dots[i]);

        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            adDots(position);
            current_page = position;

            if(position == 0){

                next.setEnabled(true);
                Previous.setEnabled(false);
                Previous.setVisibility(View.GONE);

                next.setText("Next");
                Previous.setText("");

            } else if( position == dots.length -1){

                next.setEnabled(true);
                Previous.setEnabled(true);
                Previous.setVisibility(View.VISIBLE);

                next.setText("Finish");
                Previous.setText("Back");

            } else {

                next.setEnabled(true);
                Previous.setEnabled(true);
                Previous.setVisibility(View.VISIBLE);

                next.setText("Next");
                Previous.setText("Back");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
