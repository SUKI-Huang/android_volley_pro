package com.volleypro;

import java.io.File;

/**
 * Created by tony1 on 2/3/2017.
 */

public interface VolleyProEvent {
    void OnSuccess(File file, String filePath);
    void OnSuccess(String result);
    void OnFailed(int code, String msg);
    void OnFileProgress(float progress);
    void OnMultiPartProgress(float progress);
}
