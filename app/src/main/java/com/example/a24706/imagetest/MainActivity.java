package com.example.a24706.imagetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.kareluo.intensify.image.IntensifyImage;
import me.kareluo.intensify.image.IntensifyImageView;


public class MainActivity extends AppCompatActivity {

    private String mUrl = "http://qianfan-qianfanyun.qiniudn.com/1487042529922_965.jpg?imageView2/1/w/640/h/338/interlace/1/q/100";

    private IntensifyImageView intensifyImageView;

    private ViewPager viewPager;

    private String[] urls = {
            "http://qianfan-qianfanyun.qiniudn.com/1487042529922_965.jpg?imageView2/1/w/640/h/338/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/20170307271488875120322113.jpg?imageView2/1/w/640/h/359/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/20170307271488866783683931.jpg?imageView2/1/w/640/h/359/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/20170301271488330620302561.jpg?imageView2/1/w/640/h/359/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/20170227271488161061406929.jpg?imageView2/1/w/640/h/852/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/20170227271488161042740429.jpg?imageView2/1/w/640/h/640/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/20170214271487056042560969.jpg?imageView2/1/w/640/h/640/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/1487042529922_965.jpg?imageView2/1/w/640/h/338/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/1487042456411_16.jpg?imageView2/1/w/640/h/360/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/1487042529922_965.jpg?imageView2/1/w/640/h/338/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/20170214271487056042560969.jpg?imageView2/1/w/640/h/640/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/20170210271486692447480144.jpg?imageView2/1/w/640/h/1137/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/ww.jpg?imageView2/1/w/640/h/853/interlace/1/q/100"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPagerAdapter());
//        String imagePath= Environment.getExternalStorageDirectory()+ File.separator+"hhz.png";

//        intensifyImageView.setImage(new File(imagePath));
//        Utils utils = new Utils();
//        utils.loadImage(this, intensifyImageView, mUrl);
    }

    class MyPagerAdapter extends PagerAdapter {
        Utils utils;

        @Override
        public int getCount() {
            return urls.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            IntensifyImageView intensifyImageView = new IntensifyImageView(MainActivity.this);
            if (utils==null){
                utils=new Utils();
            }
            File file = new File("/storage/emulated/0/tencent/QQ_Images/20170210271486692447480144.jpg");
            Bitmap bitmap=BitmapFactory.decodeFile(file.getPath(),getBitmapOption(1));
            //TODO 根据图片大小缩放，防止超过4000
            intensifyImageView.setImage(Bitmap2InputStream(bitmap));
//            utils.loadImage(MainActivity.this,intensifyImageView,urls[position]);
            container.addView(intensifyImageView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            return intensifyImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }
}
