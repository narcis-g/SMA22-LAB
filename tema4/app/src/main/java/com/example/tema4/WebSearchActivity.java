package com.example.tema4;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebSearchActivity extends AppCompatActivity implements View.OnClickListener{


    public static String EXTRA_URL = "";
    private String url = "";
    private Button loadFGBtn, loadBGBtn;
    private WebView webView;

    private void copyUrlOnClipboard(){

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData abc = clipboard.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        this.url = item.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview);
        setGoogleWebView();
        setListenersOnButtons();


    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    private void setGoogleWebView() {

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


    }

    private void setListenersOnButtons() {

        loadBGBtn = (Button) findViewById(R.id.bLoadBackground);
        loadFGBtn = (Button) findViewById(R.id.bLoadForeground);

        loadBGBtn.setOnClickListener((View.OnClickListener) this);
        loadFGBtn.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View view){

        copyUrlOnClipboard();

        if(!url.contains("https://images.app.goo.gl/"))
            Toast.makeText(this, "URL not valid. Try another. ", Toast.LENGTH_SHORT).show();
        else{

            switch(view.getId()){

                case R.id.bLoadBackground:
                {
                    Intent intent = new Intent(this, ImageIntentService.class);
                    intent.putExtra(EXTRA_URL, url);
                    startService(intent);
                }break;

                case R.id.bLoadForeground:
                {
                    Intent startIntent = new Intent(this, ForegroundImageService.class);
                    startIntent.setAction(ForegroundImageService.STARTFOREGROUND_ACTION);
                    startIntent.putExtra(EXTRA_URL, url);
                    startService(startIntent);
                }break;

            }
        }

    }
}