package com.example.a24706.imagetest.PhotoImageView;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.a24706.imagetest.R;
import com.facebook.drawee.drawable.AutoRotateDrawable;

/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author 杨晨 on 2017/4/3 19:22
 * @e-mail 247067345@qq.com
 * @see [相关类/方法](可选)
 */

public class PhotoLoadingView extends RelativeLayout {
    private ImageView loadingImageView;

    public PhotoLoadingView(Context context) {
        this(context,null);
    }

    public PhotoLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PhotoLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.BLACK);
        loadingImageView = new ImageView(getContext());
        AutoRotateDrawable autoRotateDrawable=new AutoRotateDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_loading),1000);
        loadingImageView.setImageDrawable(autoRotateDrawable);
        RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(loadingImageView,lp);
    }

    public void show(){
        setVisibility(VISIBLE);
    }

    public void dismiss(){
        setVisibility(GONE);
    }
}
