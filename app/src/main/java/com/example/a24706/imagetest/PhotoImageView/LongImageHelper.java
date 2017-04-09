package com.example.a24706.imagetest.PhotoImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.kareluo.intensify.image.IntensifyImage;
import me.kareluo.intensify.image.IntensifyImageView;

/**
 * Created by 24706 on 2017/3/8.
 */

public class LongImageHelper {

    private ExecutorService mExecutor;

    /**
     * 判断图片是否为gif
     *
     * @param path
     * @return
     */
    public boolean isGif(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        String type = options.outMimeType;
        if (type.contains("gif")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGif(InputStream inputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        String type = options.outMimeType;
        if (type.contains("gif")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 加载长图，区分本地图片和远程图片
     *
     * @param context
     * @param intensifyImage
     * @param url
     * @param photoLoadingView
     */
    public void loadImage(final Context context, final IntensifyImageView intensifyImage, final String url, PhotoLoadingView photoLoadingView) {
        if (url.startsWith("/storage/") || url.startsWith("/data")) {
            loadLocalLongImage(context, intensifyImage, url, photoLoadingView);
        } else {
            loadRemoteLongImage(context, intensifyImage, url, photoLoadingView);
        }
    }

    /**
     * 加载本地长图
     *
     * @param context
     * @param intensifyImage
     * @param path
     * @param photoLoadingView
     */
    private void loadLocalLongImage(final Context context, final IntensifyImageView intensifyImage, final String path, final PhotoLoadingView photoLoadingView) {
        if (isGif(path)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);
                final Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), getBitmapOption(1));
                intensifyImage.setScaleType(IntensifyImage.ScaleType.FIT_AUTO);
                intensifyImage.setMaximumScale(getMaxScale(bitmap.getWidth(), bitmap.getHeight(), context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels));
                intensifyImage.setMinimumScale(1f);
                final InputStream inputStream = Bitmap2InputStream(bitmap);
                intensifyImage.post(new Runnable() {
                    @Override
                    public void run() {
                        intensifyImage.setImage(inputStream);
                        photoLoadingView.dismiss();
                    }
                });
            }
        }).start();
    }


    /**
     * 加载网络长图
     *
     * @param context
     * @param intensifyImage
     * @param url
     * @param photoLoadingView
     */
    private void loadRemoteLongImage(final Context context, final IntensifyImageView intensifyImage, final String url, final PhotoLoadingView photoLoadingView) {
        if (mExecutor == null) {
            mExecutor = Executors.newCachedThreadPool();
        }
        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableBitmap>>() {
            @Override
            public void onNewResultImpl(
                    DataSource<CloseableReference<CloseableBitmap>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }
                CloseableReference<CloseableBitmap> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableBitmap> closeableReference = imageReference.clone();
                    try {
                        CloseableBitmap closeableBitmap = closeableReference.get();
                        final Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
//                        Log.d("image", "bitmap width====>" + bitmap.getWidth() + "bitmap height=====>" + bitmap.getHeight());
                        if (bitmap != null && !bitmap.isRecycled()) {
                            FileBinaryResource resource = (FileBinaryResource)Fresco.getImagePipelineFactory().getMainFileCache().getResource(new SimpleCacheKey(url));
                            File file = resource.getFile();
                            if (file.exists()) {
                                final Bitmap tartgetBitmap = BitmapFactory.decodeFile(file.getPath(), getBitmapOption(1));
                                intensifyImage.setScaleType(IntensifyImage.ScaleType.FIT_AUTO);
                                intensifyImage.setMaximumScale(getMaxScale(bitmap.getWidth(), bitmap.getHeight(), context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels));
                                intensifyImage.setMinimumScale(1f);
                                final InputStream inputStream = Bitmap2InputStream(tartgetBitmap);
                                intensifyImage.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        intensifyImage.setImage(inputStream);
                                        photoLoadingView.dismiss();
                                    }
                                });
                            }
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                // handle failure
            }
        };
        getBitmap(context, Uri.parse(url), dataSubscriber);
    }

    private float getMaxScale(int bmWidth, int bmHeight, int parentWidth, int parentHeight) {
        if (bmHeight >= parentHeight) {
            return (float) parentWidth * 3 / (float) bmWidth;
        } else {
            return (float) parentWidth * ((float) parentHeight / (float) bmHeight) * 3 / bmWidth;
        }
    }

    /**
     * @param context
     * @param uri
     * @param dataSubscriber
     */
    public void getBitmap(Context context, Uri uri, DataSubscriber dataSubscriber) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest request = builder.build();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(request, context);
        dataSource.subscribe(dataSubscriber, mExecutor);
    }


    // 将Bitmap转换成InputStream
    public InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        return options;
    }

}
