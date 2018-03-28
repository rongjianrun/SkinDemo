package com.rjr.skindemo.utils;

import android.content.res.Resources;
import android.support.annotation.IdRes;

/**
 * Created by Administrator on 2018/3/28.
 */

public class SkinManager {

    private static SkinManager instance;
    private Resources skinRes;

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager();
                }
            }
        }
        return instance;
    }

    public @IdRes int getColor(@IdRes int resId) {
        if (skinRes != null) {

        }
        return resId;
    }
}
