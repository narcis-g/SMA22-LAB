package com.example.tema4;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class URLTools {

    public static String getLongUrl(String shortUrl) throws MalformedURLException, IOException {
        String result = shortUrl;
        String header;
        do {
            URL url = new URL(result);
            HttpURLConnection.setFollowRedirects(false);
            URLConnection conn = url.openConnection();
            header = conn.getHeaderField(null);
            String location = conn.getHeaderField("location");
            if (location != null) {
                result = location;
            }
        } while (header.contains("301"));

        // also decode URL
        result = URLDecoder.decode(result, "UTF-8");
        // trim to extract bitmap
        result = result.substring(result.indexOf("imgurl=") + "imgurl=".length());
        result = result.substring(0, result.indexOf("&"));

        return result;
    }
}