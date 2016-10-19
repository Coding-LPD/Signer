package com.scnu.zhou.signer.elss.cache;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by zhou on 2016/9/12.
 */
public class ClearCache {

    public static void clear() {
        clearImageCache();
    }

    public static void clearImageCache() {

        // 清除图片缓存
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiscCache();
    }

}
