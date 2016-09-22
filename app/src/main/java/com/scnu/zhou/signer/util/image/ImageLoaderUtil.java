package com.scnu.zhou.signer.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.scnu.zhou.signer.R;

/**
 * Created By Zhou Chupeng
 */
public class ImageLoaderUtil {

	private ImageLoaderUtil() {

	}

	private static ImageLoaderUtil instance = null;

	public static synchronized ImageLoaderUtil getInstance() {
		if (instance == null) {
			instance = new ImageLoaderUtil();
		}
		return instance;
	}

	/**
	 * 初始化ImageLoader
	 */
	public void initImageLoader(Context context) {
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(context);

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
	}

	public void displayHeaderImage(ImageView imageView, String imageUrl) {

		// 显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.default_header_male)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
	}
}
