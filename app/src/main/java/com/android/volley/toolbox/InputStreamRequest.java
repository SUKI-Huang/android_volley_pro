package com.android.volley.toolbox;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SsuChi on 8/24/2015.
 */
public class InputStreamRequest extends Request<byte[]> implements Response.ProgressListener {
    private final Response.Listener<byte[]> mListener;
    private Map<String, String> mParams;
    //create a static map for directly accessing headers
    public Map<String, String> responseHeaders ;

    public InputStreamRequest(int post, String mUrl, Response.Listener<byte[]> listener,
                              Response.ErrorListener errorListener, HashMap<String, String> params) {
        // TODO Auto-generated constructor stub

        super(post, mUrl, errorListener);
        // this request would never use cache.
        setShouldCache(false);
        mListener = listener;
        mParams=params;
    }


    @Override
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return mParams;
    }


    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //Initialise local responseHeaders map with response headers received
        responseHeaders = response.headers;

        //Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }


    @Override
    public void onProgress(long transferredBytes, long totalSize) {
        Log.i("inputstream",String.format("totalSize:%d\ttransferredBytes:\t%d",totalSize,transferredBytes));
//        if(null != progressListener){
//            progressListener.onProgress(transferredBytes, totalSize);
//        }
    }
}