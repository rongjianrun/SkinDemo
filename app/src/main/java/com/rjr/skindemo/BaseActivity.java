package com.rjr.skindemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;

/**
 * Created by Administrator on 2018/3/28.
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new SkinFactory());
    }
}
