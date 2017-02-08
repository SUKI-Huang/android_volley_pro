package com.volleypro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.InputStreamRequest;
import com.android.volley.toolbox.MultipartRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony1 on 1/18/2017.
 */

public abstract class BaseVolleyPro {
    private String TAG = getClass().getSimpleName();

    public static final String SOURCE_CACHE = "cache";
    public static final String SOURCE_NETWORK = "network";
    public static final String SOURCE_NONE = "none";

    public static void initialize(Context context) {
        HttpError.Message.initialize(context.getApplicationContext());
    }

    void OnSuccess(File file, String filePath) {
        callOnSuccess(file, filePath);
    }

    void OnSuccess(String result) {
        callOnSuccess(result);
    }

    void OnFailed(int code, String msg) {
        callOnFailed(code, msg);
    }

    public enum Status {
        SUCCESS,
        FAILED
    }

    public enum Method {
        GET,
        POST,
        PUT,
        DELETE,
        PATCH
    }

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
            default:
                return "none";
        }
    }

    private SimpleVolleyProEvent simpleVolleyProEvent;

    public void setOnEvent(SimpleVolleyProEvent simpleVolleyProEvent) {
        this.simpleVolleyProEvent = simpleVolleyProEvent;
    }


    public final void callOnSuccess(String result) {
        if (simpleVolleyProEvent == null) {
            return;
        }
        simpleVolleyProEvent.OnSuccess(result);
    }

    public final void callOnFileProgress(float progress) {
        if (simpleVolleyProEvent == null) {
            return;
        }
        simpleVolleyProEvent.OnFileProgress(progress);
    }

    public final void callOnMultiPartProgress(float progress) {
        if (simpleVolleyProEvent == null) {
            return;
        }
        simpleVolleyProEvent.OnMultiPartProgress(progress);
    }

    public final void callOnSuccess(File file, String filePath) {
        if (simpleVolleyProEvent == null) {
            return;
        }
        simpleVolleyProEvent.OnSuccess(file, filePath);
    }

    public final void callOnFailed(int code, String msg) {
        if (simpleVolleyProEvent == null) {
            return;
        }
        simpleVolleyProEvent.OnFailed(code, msg);
    }

    private Context context;
    private Handler handler;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private MultipartRequest multipartRequest;
    private InputStreamRequest inputStreamRequest;

    private int retryTimes = 0;
    private int timeout = 60000;
    private boolean isLoading = false;


    public BaseVolleyPro(Context context) {
        this.context = context;
        this.handler = new Handler();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public final void request(String endpoint, final MultiPartOption multiPartOption) {
        boolean hasOption = (multiPartOption != null && multiPartOption.getParameters() != null);
        if (!hasOption) {
            throw new RuntimeException("multi part option is null");
        }

        //release request
        cancelRequest();

        if (!isNetworkAvailable()) {
            //network unavailable
            OnFailed(HttpError.Code.NETWORK_UNAVAILABLE, HttpError.Message.getMessage(HttpError.Code.NETWORK_UNAVAILABLE));
            return;
        }

        multipartRequest = new MultipartRequest(endpoint, multiPartOption.getMultipartEntityBuilder(), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                isLoading = false;
                OnSuccess(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                //load failed

                boolean hasError = error != null && error.networkResponse != null;

                OnFailed(hasError ? error.networkResponse.statusCode : HttpError.Code.UNKNOW_ERROR, HttpError.Message.getMessage(hasError ? error.networkResponse.statusCode : HttpError.Code.UNKNOW_ERROR));


            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (multiPartOption.getHeader() != null) {
                    return multiPartOption.getHeader();
                }
                return super.getHeaders();
            }

            @Override
            public void onProgress(final long transferredBytes, final long totalSize) {
                if (multiPartOption.isEnableFileProgress() && totalSize>0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callOnFileProgress((float) transferredBytes / (float) totalSize);
                        }
                    });
                }
            }
        };
        multipartRequest.setOnMultiPartProgress(new MultipartRequest.OnMultiPartProgress() {
            @Override
            public void onProgress(final long transferredBytes, final long totalSize) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callOnMultiPartProgress((float) transferredBytes / (float) totalSize);
                    }
                });
            }
        });
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, retryTimes, 0));
        isLoading = true;
        requestQueue.add(multipartRequest);

    }

    public final void request(final Method method, final String endpoint, final HashMap<String, String> header, final HashMap<String, String> parameters, final String cachePath, final String cacheResult, final Boolean forceUseCacheOnNoNetwork) {
        //release request
        cancelRequest();
        //check network
        if (!isNetworkAvailable()) {
            if (cacheResult != null && forceUseCacheOnNoNetwork) {
                //force user cache on network unavailable
                OnSuccess(cacheResult);
            } else {
                //network unavailable
                OnFailed(HttpError.Code.NETWORK_UNAVAILABLE, HttpError.Message.getMessage(HttpError.Code.NETWORK_UNAVAILABLE));
            }
            return;
        }

        //request
        stringRequest = new StringRequest(
                getMethod(method),
                endpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        isLoading = false;
                        OnSuccess(result);
                        writeCache(cachePath, result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isLoading = false;
                        if (cacheResult != null && (forceUseCacheOnNoNetwork != null && forceUseCacheOnNoNetwork)) {
                            //force user cache on network unavailable
                            OnSuccess(cacheResult);
                            return;
                        } else {
                            //load failed
                            boolean hasError = error != null && error.networkResponse != null;

                            OnFailed(hasError ? error.networkResponse.statusCode : HttpError.Code.UNKNOW_ERROR, HttpError.Message.getMessage(hasError ? error.networkResponse.statusCode : HttpError.Code.UNKNOW_ERROR));
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (parameters != null) {
                    return parameters;
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (header != null) {
                    return header;
                }
                return super.getHeaders();
            }

        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, retryTimes, 0));
        isLoading = true;
        requestQueue.add(stringRequest);
    }

    public final void requestFile(final Method method, final String endpoint, final HashMap<String, String> header, final HashMap<String, String> parameters, final String cachePath, final File cacheResult, final Boolean forceUseCacheOnNoNetwork, final boolean enableProgress) {
        //release request
        cancelRequest();
        //check network
        if (!isNetworkAvailable()) {
            if (cacheResult != null && forceUseCacheOnNoNetwork) {
                //force user cache on network unavailable
                OnSuccess(cacheResult, cachePath);
            } else {
                //network unavailable
                OnFailed(HttpError.Code.NETWORK_UNAVAILABLE, HttpError.Message.getMessage(HttpError.Code.NETWORK_UNAVAILABLE));
            }
            return;
        }
        inputStreamRequest = new InputStreamRequest(
                getMethod(method),
                endpoint,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] result) {
                        isLoading = false;
                        writeCache(result, cachePath);
                        OnSuccess(new File(cachePath), cachePath);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                if (cacheResult != null && (forceUseCacheOnNoNetwork != null && forceUseCacheOnNoNetwork)) {
                    //force user cache on network unavailable
                    OnSuccess(cacheResult, cachePath);
                    return;
                } else {
                    //load failed
                    boolean hasError = error != null && error.networkResponse != null;
                    int errorCode = error.networkResponse.statusCode;
                    OnFailed(hasError ? errorCode : HttpError.Code.UNKNOW_ERROR, HttpError.Message.getMessage(hasError ? errorCode : HttpError.Code.UNKNOW_ERROR));
                }

            }
        }, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (parameters != null) {
                    return parameters;
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (header != null) {
                    return header;
                }
                return super.getHeaders();
            }

            @Override
            public void onProgress(final long transferredBytes, final long totalSize) {
                if (enableProgress && totalSize>0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callOnFileProgress((float) transferredBytes / (float) totalSize);
                        }
                    });
                }
            }
        };


        inputStreamRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, retryTimes, 0));
        isLoading = true;
        requestQueue.add(inputStreamRequest);
    }

    private void cancelRequest() {
        if (stringRequest != null) {
            if (!stringRequest.isCanceled()) {
                stringRequest.cancel();
            }
            stringRequest = null;
        }

        if (multipartRequest != null) {
            if (!multipartRequest.isCanceled()) {
                multipartRequest.cancel();
            }
            multipartRequest = null;
        }

        if (inputStreamRequest != null) {
            if (!inputStreamRequest.isCanceled()) {
                inputStreamRequest.cancel();
            }
            inputStreamRequest = null;
        }

    }

    private void writeCache(final String cachePath, final String result) {

        if (cachePath == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeFile(cachePath, result);
            }
        }).start();
    }

    private void writeCache(byte[] response, String filePath) {
        if (response == null) {
            return;
        }
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

    public boolean isLoading() {
        return isLoading;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isNetworkAvailable() {
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

    public static class Option {
        private boolean enableFileProgress;
        private HashMap<String, String> header;
        private HashMap<String, String> parameters;
        private String cachePath;
        private long expiredDuration = 0;
        private boolean forceUseCacheOnNoNetwork = false;

        public Option enableFileProgress() {
            enableFileProgress = true;
            return this;
        }

        public boolean isEnableFileProgress() {
            return enableFileProgress;
        }

        public Option setCache(String cachePath, long expiredDuration, boolean forceUseCacheOnNoNetwork) {
            this.cachePath = cachePath;
            this.expiredDuration = expiredDuration;
            this.forceUseCacheOnNoNetwork = forceUseCacheOnNoNetwork;
            return this;
        }

        public String getCachePath() {
            return cachePath;
        }

        public Option setCachePath(String cachePath) {
            this.cachePath = cachePath;
            return this;
        }

        public long getExpiredDuration() {
            return expiredDuration;
        }

        public Option setExpiredDuration(long expiredDuration) {
            this.expiredDuration = expiredDuration;
            return this;
        }

        public boolean isForceUseCacheOnNoNetwork() {
            return forceUseCacheOnNoNetwork;
        }

        public Option setForceUseCacheOnNoNetwork(boolean forceUseCacheOnNoNetwork) {
            this.forceUseCacheOnNoNetwork = forceUseCacheOnNoNetwork;
            return this;
        }

        public HashMap<String, String> getHeader() {
            if (header == null) {
                return null;
            }
            HashMap<String, String> hashMap = new HashMap<>();
            for (String key : header.keySet()) {
                hashMap.put(key, header.get(key));
            }
            return hashMap;
        }

        public Option setHeader(HashMap<String, String> header) {
            this.header = header;
            return this;
        }

        public HashMap<String, String> getParameters() {
            if (parameters == null) {
                return null;
            }
            HashMap<String, String> hashMap = new HashMap<>();
            for (String key : parameters.keySet()) {
                hashMap.put(key, parameters.get(key));
            }
            return hashMap;
        }

        public Option setParameters(HashMap<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }
    }

    public static class MultiPartOption {
        private boolean enableFileProgress = false;
        private HashMap<String, String> header;
        private HashMap<String, ContentBody> parameters;

        public HashMap<String, String> getHeader() {
            return header;
        }

        public MultiPartOption enableFileProgress() {
            enableFileProgress = true;
            return this;
        }

        public boolean isEnableFileProgress() {
            return enableFileProgress;
        }


        public MultiPartOption setHeader(HashMap<String, String> header) {
            this.header = header;
            return this;
        }

        public HashMap<String, ContentBody> getParameters() {
            return parameters;
        }

        public MultiPartOption setParameters(HashMap<String, ContentBody> parameters) {
            this.parameters = parameters;
            return this;
        }

        private MultipartEntityBuilder getMultipartEntityBuilder() {
            if (parameters == null) {
                return null;
            }

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (String key : parameters.keySet()) {
                multipartEntityBuilder.addPart(key, parameters.get(key));
            }
            return multipartEntityBuilder;
        }
    }


}
