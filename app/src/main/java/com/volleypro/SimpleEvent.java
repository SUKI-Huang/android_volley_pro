package com.volleypro;

import java.lang.reflect.Type;

/**
 * Created by tony1 on 2/3/2017.
 */

public class SimpleEvent<T> implements BaseVolleyPro.Event<T> {
    private Type type;

    public SimpleEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
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
