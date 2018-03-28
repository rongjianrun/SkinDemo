package com.rjr.skindemo;

import android.app.Application;
import android.os.Environment;

import com.rjr.skindemo.utils.SkinManager;

import java.io.File;

/**
 * Created by Administrator on 2018/3/28.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        File file = new File(Environment.getExternalStorageDirectory(), "skin.apk");
        SkinManager.getInstance(this).loadSkin(file.getAbsolutePath());
    }
}
