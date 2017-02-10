package com.volleypro.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.volleypro.enums.Method;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tony1 on 2/10/2017.
 */

public class UtilVolley {
    public static final String SOURCE_CACHE = "cache";
    public static final String SOURCE_NETWORK = "network";
    public static final String SOURCE_NONE = "none";

    public static int getMethod(Method method) {
        switch (method) {
            case GET:
                return Request.Method.GET;
            case POST:
                return Request.Method.POST;
            case PUT:
                return Request.Method.PUT;
            case DELETE:
                return Request.Method.DELETE;
            case PATCH:
                return Request.Method.PATCH;
            case TRACE:
                return Request.Method.TRACE;
            case HEAD:
                return Request.Method.HEAD;
            case OPTIONS:
                return Request.Method.OPTIONS;
            default:
                return -1;
        }
    }

    public static String getMethodName(Method method) {
        switch (method) {
            case GET:
                return "GET";
            case POST:
                return "POST";
            case PUT:
                return "PUT";
            case DELETE:
                return "DELETE";
            case PATCH:
                return "PATCH";
            case TRACE:
                return "TRACE";
            case HEAD:
                return "HEAD";
            case OPTIONS:
                return "OPTIONS";
            default:
                return "none";
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
    }

    public static boolean isFileExist(String path) {
        if (path == null) {
            return false;
        }
        File f = new File(path);
        return f.exists();
    }

    public static boolean isFileExpired(String path, long expiredDuration) {
        if (path == null) {
            return true;
        }
        File file = new File(path);
        if (file.exists()) {
            long lastModified = file.lastModified();
            if (System.currentTimeMillis() - lastModified > expiredDuration) {
                return true;
            } else {
                return false;
            }

        } else {
            return true;
        }

    }

    public static String readFile(String filePath) {
        try {
            StringBuffer fileData = new StringBuffer();
            BufferedReader reader = new BufferedReader(
                    new FileReader(filePath));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            return fileData.toString();
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static void writeFile(String filePath, String message) {
        try {
            FileOutputStream output = new FileOutputStream(filePath);
            output.write(message.getBytes());
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String path) {
        if (path == null) {
            return;
        }
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void writeFileAsync(final String cachePath, final String result) {

        if (cachePath == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                UtilVolley.writeFile(cachePath, result);
            }
        }).start();
    }

    public static void writeByteAsFile(final byte[] response, final String filePath){
        int count;
        try {
            long lenghtOfFile = response.length;

            //covert reponse to input stream
            InputStream input = new ByteArrayInputStream(response);
            File file = new File(filePath);
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[1024];
//                            long total = 0;
            while ((count = input.read(data)) != -1) {
//                                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByteAsync(final byte[] response, final String filePath) {
        if (response == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                writeByteAsFile(response, filePath);
            }
        }).start();

    }
}
