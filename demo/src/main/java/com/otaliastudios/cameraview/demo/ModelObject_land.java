package com.otaliastudios.cameraview.demo;

/**
 * Created by haide on 12/12/2017.
 */

public enum ModelObject_land {
    ZERO(R.string.zero, R.layout.fragment_one_land),
    ONE(R.string.one, R.layout.fragment_two_land),
    TWO(R.string.two, R.layout.fragment_three_land),
    THREE(R.string.three,R.layout.fragment_four_land),
    FOUR(R.string.four,R.layout.fragment_five_land),
    FIVE(R.string.five,R.layout.fragment_six_land),
    SIX(R.string.six,R.layout.fragment_seven_land);


    private int mTitleResId;
    private int mLayoutResId;

    ModelObject_land(int titleResId, int layoutResId){
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId(){
        return mTitleResId;
    }

    public int getLayoutResId(){
        return mLayoutResId;
    }
}
