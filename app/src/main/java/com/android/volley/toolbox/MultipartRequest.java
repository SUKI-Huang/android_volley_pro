package com.android.volley.toolbox;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


public class MultipartRequest extends Request<String> implements Response.ProgressListener
{
	private String TAG=getClass().getSimpleName();
    private MultipartEntityBuilder entity;
    private final Listener<String> mListener;
    private HttpEntity httpentity;

	private OnMultiPartProgress onMultiPartProgress;
	public interface OnMultiPartProgress{
		void onProgress(long transferredBytes, long totalSize);
	}

	public MultipartRequest setOnMultiPartProgress(OnMultiPartProgress onMultiPartProgress) {
		this.onMultiPartProgress = onMultiPartProgress;
		return this;
	}

	public MultipartRequest(int method,String url, MultipartEntityBuilder entity, Listener<String> listener, ErrorListener errorListener)
    {
        super(method, url, errorListener);
        this.entity = entity;
        this.mListener = listener;
    }


	@Override
	public String getBodyContentType()
	{
		return httpentity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			httpentity = entity.build();
//			httpentity.writeTo(bos);
			long fileLength=httpentity.getContentLength();
			httpentity.writeTo(new CountingOutputStream(bos, fileLength, onMultiPartProgress));
		}
		catch (IOException e)
		{
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
		}
		catch (UnsupportedEncodingException e)
		{
			return Response.error(new ParseError(e));
		}
		catch (Exception e)
		{
			return Response.error(new ParseError(e));
		}

    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

	@Override
	public void onProgress(long transferredBytes, long totalSize) {
		
	}

	public static class CountingOutputStream extends FilterOutputStream {
		private final OnMultiPartProgress onMultiPartProgress;
		private long transferred;
		private long fileLength;

		public CountingOutputStream(final OutputStream out, long fileLength,
									final OnMultiPartProgress onMultiPartProgress) {
			super(out);
			this.fileLength = fileLength;
			this.onMultiPartProgress = onMultiPartProgress;
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			transferred += len;
			if (onMultiPartProgress != null) {
				this.onMultiPartProgress.onProgress(transferred, fileLength);
			}
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			if (onMultiPartProgress != null) {
				this.onMultiPartProgress.onProgress(transferred, fileLength);
			}
		}

	}
}
