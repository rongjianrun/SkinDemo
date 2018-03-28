package com.rjr.skindemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;

import com.rjr.skindemo.R;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/3/28.
 */

public class SkinManager {

    private static SkinManager instance;
    private Resources skinRes;
    private Context mContext;
    private String packageName;

    private SkinManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public static SkinManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 加载皮肤apk，拿到resources
     * @param path 皮肤apk的路径
     */
    public void loadSkin(String path) {
        try {
            // 反射拿到AssetManager对象
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", new Class[]{String.class});
            // 调用addAssetPath方法，把皮肤包的路径传入方法
            addAssetPath.invoke(assetManager, path);
            // 初始化皮肤包的resources
            skinRes = new Resources(assetManager,
                    mContext.getResources().getDisplayMetrics(),
                    mContext.getResources().getConfiguration());
            packageName = mContext.getPackageManager().getPackageArchiveInfo(
                    path, PackageManager.GET_ACTIVITIES).packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取皮肤包中的颜色资源
     * @param resId app中的颜色资源id
     * @return 如果皮肤包有该资源id，则返回皮肤包的资源，否则不变
     */
    public int getColor(int resId) {
        if (skinRes != null) {
            // 拿到colors定义的颜色名
            String resEntryName = mContext.getResources().getResourceEntryName(resId);
            int colorResId = skinRes.getIdentifier(resEntryName, "color", packageName);
            if (colorResId != 0) {
                return skinRes.getColor(colorResId);
            }
        }
        return resId;
    }

    public Drawable getDrawable(int resId) {
        if (skinRes != null) {
            String resEntryName = mContext.getResources().getResourceEntryName(resId);
            int drawableResId = skinRes.getIdentifier(resEntryName, "drawable", packageName);
            if (drawableResId != 0) {
                return skinRes.getDrawable(drawableResId);
            }
        }
        return mContext.getResources().getDrawable(resId);
    }

    public Bitmap getSrcImageBitmap(int resId) {
        if (skinRes != null) {
            String resEntryName = mContext.getResources().getResourceEntryName(resId);
            int drawableResId = skinRes.getIdentifier(resEntryName, "drawable", packageName);
            if (drawableResId != 0) {
                return BitmapFactory.decodeResource(skinRes, drawableResId);
            }
        }
        return BitmapFactory.decodeResource(mContext.getResources(), resId);
    }
}
