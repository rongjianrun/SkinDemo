package com.rjr.skindemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("skin", Context.MODE_APPEND);
        editor = sp.edit();
        if (sp.getBoolean("change", false)) {
            switchSkin();
        }
    }

    public void skinPeeler(View view) {
        switchSkin();
        editor.putBoolean("change", true);
        editor.commit();
    }

    public void jump(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
