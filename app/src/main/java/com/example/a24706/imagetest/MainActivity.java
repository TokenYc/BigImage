package com.example.a24706.imagetest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.a24706.imagetest.PhotoImageView.PhotoImageView;
import com.example.a24706.imagetest.PhotoImageView.LongImageHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import me.kareluo.intensify.image.IntensifyImageView;


/**
 * 如果只用IntensifyImageView来展示的话，Gif不好处理，因为RegionDecoder是不支持Gif的。
 * 所以设计成以PhotoView为主，IntensifyImageView为辅的策略
 */
public class MainActivity extends AppCompatActivity {

    private String mUrl = "http://qianfan-qianfanyun.qiniudn.com/1487042529922_965.jpg?imageView2/1/w/640/h/338/interlace/1/q/100";

    ///storage/emulated/0/tencent/QQ_Images/20170210271486692447480144.jpg 有问题的本地图片地址
//    private IntensifyImageView intensifyImageView;

    private FixedViewPager viewPager;

    private String[] urls = {
            "http://qianfan-qianfanyun.qiniudn.com/20170301271488330620302561.jpg?imageView2/1/w/640/h/359/interlace/1/q/100",
            "http://qianfan-qianfanyun.qiniudn.com/1487042529922_965.jpg?imageView2/1/w/640/h/338/interlace/1/q/100",
            "http://7xpp4m.com1.z0.glb.clouddn.com/article.jpg",
            //本地图片地址需要测试可以把图片下载到本地，加上本地路径
//            "http://qianfan-qianfanyun.qiniudn.com/20170301271488330620302561.jpg?imageView2/1/w/640/h/359/interlace/1/q/100",
//            "/storage/emulated/0/Tencent/QQ_Images/article.jpg",
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
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        //初始化Fresco
        Fresco.initialize(this, config);

        setContentView(R.layout.activity_main);
        viewPager = (FixedViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPagerAdapter());
    }

    class MyPagerAdapter extends PagerAdapter {

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
            PhotoImageView photoImageView = new PhotoImageView(MainActivity.this);
            photoImageView.loadImage(urls[position]);
            container.addView(photoImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
