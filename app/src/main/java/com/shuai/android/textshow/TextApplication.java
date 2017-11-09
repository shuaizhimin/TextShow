package com.shuai.android.textshow;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * 作者: shuaizhimin
 * 描述:
 * 日期: 2017-11-08
 * 时间: 18:43
 * 版本:
 */
public class TextApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
