package com.scnu.zhou.signer.component.cache;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by zhou on 2016/9/12.
 */
public class ClearCache {

    private ClearCache(){}

    private static class ClearCacheHolder{

        private static final ClearCache instance = new ClearCache();
    }

    public static final ClearCache getInstance(){

        return ClearCacheHolder.instance;
    }

    public void clear() {
        clearImageCache();
    }

    public void clearImageCache() {

        // 清除图片缓存
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiscCache();
    }

}
