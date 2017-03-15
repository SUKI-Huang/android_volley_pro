package com.volleypro;

import android.content.Context;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.InputStreamRequest;
import com.android.volley.toolbox.MultipartRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.volleypro.enums.Method;
import com.volleypro.error.HttpError;
import com.volleypro.util.UtilVolley;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony1 on 1/18/2017.
 */

public class BaseVolleyPro {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private Handler handler;

    //request
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private MultipartRequest multipartRequest;
    private InputStreamRequest inputStreamRequest;
    private SimpleEvent simpleEvent;

    //params
    private int retryTimes = 0;
    private int timeout = 60000;
    private boolean isLoading = false;

    //generic used
    private Type type;
    private Gson gson;


    public BaseVolleyPro(Context context) {
        HttpError.Message.initialize(context.getApplicationContext());
        this.context = context;
        this.handler = new Handler();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public void setOnEvent(SimpleEvent simpleEvent) {
        this.simpleEvent = simpleEvent;
        this.type = simpleEvent.getType();
    }

    public BaseVolleyPro setGson(Gson gson) {
        this.gson = gson;
        return this;
    }

    public final void request(String endpoint, final MultiPartOption multiPartOption) {
        boolean hasOption = (multiPartOption != null && multiPartOption.getParameters() != null);
        if (!hasOption) {
            throw new RuntimeException("multi part option is null");
        }

        //release request
        cancelRequest();

        if (!UtilVolley.isNetworkAvailable(context)) {
            //network unavailable
            callOnFailed(HttpError.Code.NETWORK_UNAVAILABLE, HttpError.Message.getMessage(HttpError.Code.NETWORK_UNAVAILABLE));
            return;
        }

        multipartRequest = new MultipartRequest(endpoint, multiPartOption.getMultipartEntityBuilder(), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                isLoading = false;
                callOnSuccess(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                //load failed

                boolean hasError = error != null && error.networkResponse != null;

                callOnFailed(hasError ? error.networkResponse.statusCode : HttpError.Code.UNKNOW_ERROR, HttpError.Message.getMessage(hasError ? error.networkResponse.statusCode : HttpError.Code.UNKNOW_ERROR));


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
                //if enable file progress
//                if (multiPartOption.isEnableFileProgress() && totalSize > 0) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            callOnFileProgress((float) transferredBytes / (float) totalSize);
//                        }
//                    });
//                }
            }
        };
        if(multiPartOption.enableMultiPartProgress){
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
        }
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, retryTimes, 0));
        isLoading = true;
        requestQueue.add(multipartRequest);

    }

    public final void request(final Method method, final String endpoint, final HashMap<String, String> header, final HashMap<String, String> parameters, final String cachePath, final String cacheResult, final Boolean forceUseCacheOnNoNetwork) {
        //release request
        cancelRequest();
        //check network
        if (!UtilVolley.isNetworkAvailable(context)) {
            if (cacheResult != null && forceUseCacheOnNoNetwork) {
                //force user cache on network unavailable
                callOnSuccess(cacheResult);
            } else {
                //network unavailable
                callOnFailed(HttpError.Code.NETWORK_UNAVAILABLE, HttpError.Message.getMessage(HttpError.Code.NETWORK_UNAVAILABLE));
            }
            return;
        }

        //request
        stringRequest = new StringRequest(
                UtilVolley.getMethod(method),
                endpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        isLoading = false;
                        callOnSuccess(result);
                        UtilVolley.writeFileAsync(cachePath, result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isLoading = false;
                        if (cacheResult != null && (forceUseCacheOnNoNetwork != null && forceUseCacheOnNoNetwork)) {
                            //force user cache on network unavailable
                            callOnSuccess(cacheResult);
                            return;
                        } else {
                            //load failed
                            boolean hasError = error != null && error.networkResponse != null;

                            callOnFailed(hasError ? error.networkResponse.statusCode : HttpError.Code.UNKNOW_ERROR, HttpError.Message.getMessage(hasError ? error.networkResponse.statusCode : HttpError.Code.UNKNOW_ERROR));
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
        if (!UtilVolley.isNetworkAvailable(context)) {
            if (cacheResult != null && forceUseCacheOnNoNetwork) {
                //force user cache on network unavailable
                callOnSuccess(cacheResult);
            } else {
                //network unavailable
                callOnFailed(HttpError.Code.NETWORK_UNAVAILABLE, HttpError.Message.getMessage(HttpError.Code.NETWORK_UNAVAILABLE));
            }
            return;
        }
        inputStreamRequest = new InputStreamRequest(
                UtilVolley.getMethod(method),
                endpoint,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] result) {
                        isLoading = false;
                        UtilVolley.writeByteAsync(result, cachePath);
                        callOnSuccess(new File(cachePath));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                if (cacheResult != null && (forceUseCacheOnNoNetwork != null && forceUseCacheOnNoNetwork)) {
                    //force user cache on network unavailable
                    callOnSuccess(cacheResult);
                    return;
                } else {
                    //load failed
                    boolean hasError = error != null && error.networkResponse != null;
                    int errorCode = error.networkResponse.statusCode;
                    callOnFailed(hasError ? errorCode : HttpError.Code.UNKNOW_ERROR, HttpError.Message.getMessage(hasError ? errorCode : HttpError.Code.UNKNOW_ERROR));
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
                if (enableProgress && totalSize > 0) {
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

    public void cancelRequest() {
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

    public final void callOnSuccess(Object result) {
        if(result ==null){
            simpleEvent.OnFailed(HttpError.Code.GSON_PARSE_ERROR, HttpError.Message.getMessage(HttpError.Code.GSON_PARSE_ERROR));
            return;
        }
        if (simpleEvent == null) {
            return;
        }
        if ((result instanceof String) && type.hashCode() == String.class.hashCode()) {
            simpleEvent.OnSuccess(result);
            return;
        }
        if ((result instanceof File) && type.hashCode() == File.class.hashCode()) {
            simpleEvent.OnSuccess(result);
            return;
        }

        if (gson == null) {
            gson = new Gson();
        }

        if ((result instanceof String)) {
            try {
                simpleEvent.OnSuccess(gson.fromJson((String) result, type));
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                simpleEvent.OnFailed(HttpError.Code.GSON_PARSE_ERROR, HttpError.Message.getMessage(HttpError.Code.GSON_PARSE_ERROR));
            }
            return;
        }
        Log.e(TAG, "callOnSuccess\t" + result.getClass().getSimpleName() + " can not cast to specified class");
        simpleEvent.OnFailed(HttpError.Code.UNKNOW_ERROR, HttpError.Message.getMessage(HttpError.Code.UNKNOW_ERROR));


    }

    public final void callOnFileProgress(float progress) {
        if (simpleEvent == null) {
            return;
        }
        simpleEvent.OnFileProgress(progress);
    }

    public final void callOnMultiPartProgress(float progress) {
        if (simpleEvent == null) {
            return;
        }
        simpleEvent.OnMultiPartProgress(progress);
    }

    public final void callOnFailed(int code, String msg) {
        if (simpleEvent == null) {
            return;
        }
        simpleEvent.OnFailed(code, msg);
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
        private boolean enableMultiPartProgress = false;
        private HashMap<String, String> header;
        private HashMap<String, ContentBody> parameters;

        public HashMap<String, String> getHeader() {
            return header;
        }

        public boolean isEnableMultiPartProgress() {
            return enableMultiPartProgress;
        }

        public MultiPartOption enableMultiPartProgress() {
            this.enableMultiPartProgress = true;
            return this;
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

            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            return multipartEntityBuilder;
        }
    }

    public interface Event<T> {
        void OnSuccess(T result);

        void OnFileProgress(float progress);

        void OnMultiPartProgress(float progress);
    }

    public static boolean enableLog = true;

    public static void enableLog(boolean b) {
        BaseVolleyPro.enableLog = b;
    }

    public static class Log {
        public static void i(String str, String msg) {
            if (enableLog) {
                android.util.Log.i(str, msg);
            }
        }

        public static void e(String str, String msg) {
            if (enableLog) {
                android.util.Log.e(str, msg);
            }
        }
    }


}
