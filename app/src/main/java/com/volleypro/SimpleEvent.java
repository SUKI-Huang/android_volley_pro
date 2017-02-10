package com.volleypro;

/**
 * Created by tony1 on 2/3/2017.
 */

public class SimpleEvent<T> implements BaseVolleyPro.Event<T> {
    private Class<?> type;

    public SimpleEvent(Class<T> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

    public void OnSuccess(T result) {
    }

    public void OnFailed(int code, String msg) {
    }

    public void OnFileProgress(float progress) {
    }

    public void OnMultiPartProgress(float progress) {
    }
}
