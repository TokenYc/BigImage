package com.example.a24706.imagetest;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a24706.imagetest.photodraweeview.PhotoDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import me.kareluo.intensify.image.IntensifyImage;
import me.kareluo.intensify.image.IntensifyImageView;

/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author 杨晨 on 2017/4/2 18:42
 * @e-mail 247067345@qq.com
 * @see [相关类/方法](可选)
 */

public class PhotoImageView extends RelativeLayout{
    private Utils utils;

    public PhotoImageView(Context context) {
        this(context,null);
    }

    public PhotoImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PhotoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void loadImage(final String url){
        final PhotoDraweeView photoDraweeView = new PhotoDraweeView(getContext());
//
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels))
                .build();
        photoDraweeView.setController(Fresco.newDraweeControllerBuilder()
                .setOldController(photoDraweeView.getController())
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        Log.d("image", "width====>" + imageInfo.getWidth() + "height====>" + imageInfo.getHeight());
                            photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                        if (imageInfo.getHeight()/imageInfo.getWidth()>4){
                            removeView(photoDraweeView);
                            addLongImageView(url);
                        }
                        //从本地获取已缓存的文件，用于图片二维码识别
                    }
                })
                .build());
//        GenericDraweeHierarchy hierarchy =
//                new GenericDraweeHierarchyBuilder(getContext().getResources())
//                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
//                        .setFadeDuration(300)
//                        .build();
//        photoDraweeView.setImageURI(url);
//        photoDraweeView.update(1080,720);
//        photoDraweeView.setHierarchy(hierarchy);
        addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void addLongImageView(String url) {
        IntensifyImageView intensifyImageView = new IntensifyImageView(getContext());
        if (utils==null){
            utils=new Utils();
        }
        utils.loadImage(getContext(),intensifyImageView,url);
        addView(intensifyImageView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

    }
}
