package gj.com.week3_1;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //全局配置
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this).build();
        //创建实例
        ImageLoader instance = ImageLoader.getInstance();
        instance.init(imageLoaderConfiguration);
    }
}
