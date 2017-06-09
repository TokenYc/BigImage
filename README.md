### 长图兼容库
---
#### 本库基于
 - [Fresco](https://github.com/facebook/fresco "Fresco")
 - [PhotoView](https://github.com/chrisbanes/PhotoView)
 - [IntensifyImageView](https://github.com/kareluo/IntensifyImageView)
 
#### 基本策略
基本策略为使用Fresco加载图片，使用PhotoView实现普通图片与Gif的浏览，使用IntensifyImageView实现长图的浏览。

#### 解决的问题
 - 在安卓系统中，加载过大的图片，例如bitmap的长度大于4096（大多数机型为4096）的图片时，会出现图片无法加载，显示的情况。如果进行压缩，固然可以加载成功，但是长图会非常模糊，无法正常浏览。而BitmapRegionDecoder这个类可以解决这个问题。BitmapRegionDecoder支持局部加载图片，因此可以只加载图片在屏幕中显示的部分，再根据手势等实时更新显示部分来实现长图的浏览。IntensifyImageView就是一个基于BitmapRegionDecoder的库。
 - BitmapRegionDecoder不支持Gif，因此无法单独使用IntensifyImageView来实现图片浏览。
 - BitmapRegionDecoder在部分机型上使用skia解码部分图片时抛出了异常，无固定规律。在测试后发现，使用bitmap的compress再进行一次无损压缩后可以解决这个问题，不再抛出异常。
 
#### 使用方法


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


#### 6.9更新
 - 监听点击事件
 
        photoImageView.setOnTapListener(new PhotoImageView.OnTapListener() {
                     @Override
                     public void onTap() {
                     
                     }
        });
 - 监听长按事件

        
        photoImageView.setOnImageLongClickListener(new PhotoImageView.OnImageLongClickListener() {
                    @Override
                    public void onLongClick() {
                    
                    }
        });
  
 - 监听图片文件加载完成
 
         photoImageView.setOnFileReadyListener(new PhotoImageView.OnFileReadyListener() {
                    @Override
                    public void onFileReady(final File file, final String url) {
                    
                    }
         });