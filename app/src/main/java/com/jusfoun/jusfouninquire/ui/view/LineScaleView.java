package com.jusfoun.jusfouninquire.ui.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.jusfoun.jusfouninquire.R;

/**
 * Created by xiaoma on 2018/1/12/012.
 */

public class LineScaleView extends LinearLayout {

    private View vScale,vDefault;

    private ValueAnimator scaleBig, scaleSmall;

    public LineScaleView(Context context) {
        super(context);
        init();
    }

    public LineScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LinearLayout.inflate(getContext(), R.layout.view_line_scale, this);
        vScale = findViewById(R.id.vScale);
        vDefault = findViewById(R.id.vDefault);

        scaleBig = ObjectAnimator.ofFloat(vScale, "scaleX", 0, 1);
        scaleSmall = ObjectAnimator.ofFloat(vScale, "scaleX", 1, 0);

        scaleBig.setInterpolator(new LinearInterpolator());
        scaleSmall.setInterpolator(new LinearInterpolator());
        setSelected(true);
    }

    public void setLoginBackgroundColor() {
        vScale.setBackgroundColor(Color.parseColor("#86B7FF"));
        vDefault.setBackgroundColor(Color.parseColor("#C5DDFF"));
    }

    public void scaleBig(){
        scaleBig.setDuration(300);
        scaleBig.start();
    }

    public void scaleSmall(){
        scaleSmall.setDuration(300);
        scaleSmall.start();
    }

    public void scaleInit(){
        scaleSmall.setDuration(1);
        scaleSmall.start();
    }
}
