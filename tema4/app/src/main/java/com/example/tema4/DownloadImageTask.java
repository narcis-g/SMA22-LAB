package com.example.tema4;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private Context context;


    public DownloadImageTask(Context context) {
        this.context = context;
        Toast.makeText(context, "Please wait, it may take a few seconds.", Toast.LENGTH_SHORT).show();
    }

    protected Bitmap doInBackground(String... urls) {

        Bitmap bmp = null;

        return bmp;


    }

    protected void onPostExecute(Bitmap result) {
        // save bitmap result in application class

        // send intent to stop foreground service

    }
}