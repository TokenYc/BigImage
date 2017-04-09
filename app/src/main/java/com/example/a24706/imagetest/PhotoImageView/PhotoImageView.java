package com.example.a24706.imagetest.PhotoImageView;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.a24706.imagetest.photodraweeview.PhotoDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import me.kareluo.intensify.image.IntensifyImageView;

/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author 杨晨 on 2017/4/2 18:42
 * @e-mail 247067345@qq.com
 * @see [相关类/方法](可选)
 */

public class PhotoImageView extends RelativeLayout {
    private static final int DEFAULT_LONG_IMAGE_RATIO=4;
    private int longImageRatio;
    private LongImageHelper longImageHelper;
    private PhotoLoadingView photoLoadingView;

    public PhotoImageView(Context context) {
        this(context, null);
    }

    public PhotoImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        photoLoadingView = new PhotoLoadingView(getContext());
    }

    public void loadImage(final String url) {
        Uri uri;
        if (url.startsWith("/storage/") || url.startsWith("/data")) {
            uri = Uri.parse("file://" + getContext().getPackageName() + "/" + url);
        } else {
            uri = Uri.parse(url);
        }
        final PhotoDraweeView photoDraweeView = new PhotoDraweeView(getContext());
//
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
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
                        if ((imageInfo.getHeight() / imageInfo.getWidth() > 4)
                                &&!url.endsWith(".gif")) {
                            removeView(photoDraweeView);
                            removeView(photoLoadingView);
                            addLongImageView(url, photoLoadingView);
                            addView(photoLoadingView);
                        } else {
                            photoLoadingView.dismiss();
                        }
                    }
                })
                .build());
        addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(photoLoadingView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        photoLoadingView.show();
    }

    private void addLongImageView(String url, PhotoLoadingView photoLoadingView) {
        IntensifyImageView intensifyImageView = new IntensifyImageView(getContext());
        if (longImageHelper == null) {
            longImageHelper = new LongImageHelper();
        }
        longImageHelper.loadImage(getContext(), intensifyImageView, url, photoLoadingView);
        addView(intensifyImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
