package com.volleypro.util;

import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Created by tony1 on 1/25/2017.
 */

public class ContentHashMap<String, ContentBody> extends HashMap<String, ContentBody> {
    private String TAG = (String) getClass().getSimpleName();
    private HashMap<String, String> logMap = new HashMap<>();

    public void putText(String key, String value) {
        if (key == null) {
            Log.e((java.lang.String) TAG, "putText, key is null");
            return;
        }
        if (value == null) {
            Log.e((java.lang.String) TAG, "putText, value is null");
            return;
        }

        logMap.put(key, value);

        try {
            put(key, (ContentBody) new StringBody((java.lang.String) value, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            try {
                put(key, (ContentBody) new StringBody((java.lang.String) value, Charset.defaultCharset()));
            } catch (UnsupportedEncodingException e1) {
                put(key, (ContentBody) new StringBody((java.lang.String) value, ContentType.DEFAULT_TEXT));
            }
        }
//        put(key, (ContentBody) new StringBody((java.lang.String) value, ContentType.DEFAULT_TEXT));
    }

    public void putFile(String key, String filePath) {
        if (key == null) {
            Log.e((java.lang.String) TAG, "putFile, key is null");
            return;
        }
        if (filePath == null) {
            Log.e((java.lang.String) TAG, "putFile, filePath is null");
            return;
        }
        File file = new File((java.lang.String) filePath);
        putFile(key, file);
    }

    public void putFile(String key, File file) {
        if (key == null) {
            Log.e((java.lang.String) TAG, "putFile, key is null");
            return;
        }
        if (file == null) {
            Log.e((java.lang.String) TAG, "putFile, file is null");
            return;
        } else {
            if (file.exists()) {
                if (file.length() == 0) {
                    Log.e((java.lang.String) TAG, "putFile, file size = 0");
                    return;
                }
            } else {
                Log.e((java.lang.String) TAG, "putFile, file not exist");
                return;
            }
        }

        logMap.put(key, (String) ("file：" + file.getAbsolutePath()));
        put(key, (ContentBody) new FileBody(file, ContentType.DEFAULT_BINARY, file != null ? file.getName() : null));
    }

    public void putBinary(String key, byte[] bytes, @NonNull java.lang.String fileName) {
        if (key == null) {
            Log.e((java.lang.String) TAG, "putBinary, key is null");
            return;
        }
        if (bytes == null) {
            Log.e((java.lang.String) TAG, "putBinary, bytes is null");
            return;
        }
        logMap.put(key, (String) ("bytes length：" + bytes.length));
        put(key, (ContentBody) new ByteArrayBody(bytes, ContentType.DEFAULT_BINARY, fileName));
    }

    public void putStream(String key, InputStream inputStream, @NonNull java.lang.String fileName) {
        if (key == null) {
            Log.e((java.lang.String) TAG, "putStream, key is null");
            return;
        }
        if (inputStream == null) {
            Log.e((java.lang.String) TAG, "putStream, inputStream is null");
            return;
        }
        logMap.put(key, (String) "steam content");
        put(key, (ContentBody) new InputStreamBody(inputStream, ContentType.DEFAULT_BINARY, fileName));
    }

    public HashMap<String, String> getLogMap() {
        return logMap;
    }
}
