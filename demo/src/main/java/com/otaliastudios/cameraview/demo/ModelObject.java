package com.otaliastudios.cameraview.demo;

/**
 * Created by haide on 12/12/2017.
 */

public enum ModelObject {
    ZERO(R.string.zero, R.layout.fragment_one),
    ONE(R.string.one, R.layout.fragment_two),
    TWO(R.string.two, R.layout.fragment_three),
    THREE(R.string.three,R.layout.fragment_four),
    FOUR(R.string.four,R.layout.fragment_five),
    FIVE(R.string.five,R.layout.fragment_six),
    SIX(R.string.six,R.layout.fragment_seven);


    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId){
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
