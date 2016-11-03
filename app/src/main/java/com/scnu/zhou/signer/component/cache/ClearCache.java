package com.scnu.zhou.signer.component.cache;

import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scnu.zhou.signer.component.database.DataBaseHelper;
import com.scnu.zhou.signer.component.database.SearchOperateTable;

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

    public void clear(Context context) {
        clearImageCache();
        clearDataBaseCache(context);
    }

    public void clearImageCache() {

        // 清除图片缓存
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiscCache();
    }


    public static void clearDataBaseCache(Context context) {

        // 清除数据库缓存
        DataBaseHelper helper = new DataBaseHelper(context);

        SearchOperateTable searchTable = new SearchOperateTable(helper.getWritableDatabase());
        searchTable.clear();
    }
}
