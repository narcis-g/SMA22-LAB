package com.example.tema4;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application {

    private static MyApplication singleton;

    private static Bitmap bitmap;

    public MyApplication getInstance(){

        return singleton;
    }

    @Override
    public void onCreate(){

        super.onCreate();
        singleton = this;
    }

    public Bitmap getBitmap(){

        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){

        this.bitmap = bitmap;
    }
}