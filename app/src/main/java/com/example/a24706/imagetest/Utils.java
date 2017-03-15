package com.example.a24706.imagetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
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

public class Utils {

    private  ExecutorService mExecutor;
    /**
     * 判断图片是否为gif
     * @param path
     * @return
     */
    public static boolean isGif(String path){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path, options);
        String type=options.outMimeType;
        if (type.contains("gif")){
            return true;
        }else{
            return false;
        }
    }





    public void loadImage(Context context, final IntensifyImageView intensifyImage, final String url) {
        if (mExecutor==null){
            mExecutor= Executors.newCachedThreadPool();
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
                        if (bitmap != null && !bitmap.isRecycled()) {
                            intensifyImage.setImage(Bitmap2InputStream(bitmap));
                            intensifyImage.setScaleType(IntensifyImage.ScaleType.FIT_AUTO);
                            //// TODO: 2017/3/15  通过计算得到ScaleMin和ScaleMax
                            //如果使用文件作为BitmapRegionDecoder的输入，有的图片在Skia解码的时候会失败，抛出异常需要使用BitmapFactory重新对文件进行解码

//                            handler.sendEmptyMessage(0);//图片加载完毕后获取文件
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                            ImageRequest imageRequest = ImageRequest.fromUri(url);
//                            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
//                                    .getEncodedCacheKey(imageRequest, this);
//                            BinaryResource resource = ImagePipelineFactory.getInstance()
//                                    .getMainDiskStorageCache().getResource(cacheKey);
//                            File file = ((FileBinaryResource) resource).getFile();
//                            if (file.exists()) {
//                                BitmapFactory.Options options=new BitmapFactory.Options();
//                                options.inJustDecodeBounds=true;
//                                BitmapFactory.decodeFile(file.getPath(), options);
//                                String type=options.outMimeType;
//                                Log.d("yangchen", "type---->" + options.inPreferredConfig.toString());
//                                try {
//                                    intensifyImage.setImage("/storage/emulated/0/tencent/QQ_Images/20170210271486692447480144.jpg");
//                                    intensifyImage.setScaleType(IntensifyImage.ScaleType.FIT_AUTO);
//                                }catch (RuntimeException e){
//                                    e.printStackTrace();
//                                }
//
//                            }
//                                }
//                            });
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
}
