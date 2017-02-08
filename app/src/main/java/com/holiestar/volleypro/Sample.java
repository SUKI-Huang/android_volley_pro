package com.holiestar.volleypro;

/**
 * Created by tony1 on 2/8/2017.
 */

import android.content.Context;
import android.media.MediaPlayer;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.volleypro.BaseVolleyPro;
import com.volleypro.ContentHashMap;
import com.volleypro.SimpleVolleyProEvent;
import com.volleypro.VolleyPro;

import org.apache.http.entity.mime.content.ContentBody;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Sample {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private VolleyPro volleyPro;
    private Gson gson;

    public Sample(Context context) {
        this.context = context;
        init();
        initAction();

    }

    private void init() {
        gson=new Gson();
        volleyPro=new VolleyPro(context);

    }

    private void initAction() {
        volleyPro.setOnEvent(new SimpleVolleyProEvent() {
            @Override
            public void OnFailed(int code, String msg) {
            }

            @Override
            public void OnSuccess(String result) {
            }

            @Override
            public void OnSuccess(File file, String filePath) {
            }

            @Override
            public void OnFileProgress(float progress) {
            }

            @Override
            public void OnMultiPartProgress(float progress) {

            }
        });
    }

    private void load() {
        String endpoint = "http://vtapi.azurewebsites.net/reponse.php";

        //already test all work!
        volleyPro.request(
                VolleyPro.Method.POST,
                endpoint,
                new VolleyPro.Option()
                        .setHeader(
                                new HashMap<String, String>() {{
                                    put("userid", "xxxxxx");
                                    put("usercode", "xxxxxxx");
                                    put("apiKey", "xxxxxxx");
                                }}
                        )
                        .setParameters(
                                new HashMap<String, String>() {{
                                    put("title", "this is title");
                                }}
                        )
                        .setCache("data/data/"+context.getPackageName()+"/volleyCache.json", 6000, true)
                        .enableFileProgress()
        );


        //already test, all work !!
        volleyPro.request(
                endpoint,
                new BaseVolleyPro.MultiPartOption()
                        .setHeader(null)
                        .setHeader(
                                new HashMap<String, String>() {{
                                    put("apiKey", "xxxxx");
                                }})
                        .setParameters(
                                new ContentHashMap<String, ContentBody>() {{
                                    putText("page", "yoyo");
                                    putFile("file", "/storage/emulated/0/DCIM/Camera/20170201_163602.jpg");
//                                    putFile("file", "/storage/emulated/0/DCIM/Camera/20170131_140546.jpg");
//                                    putFile("file", new File("/storage/emulated/0/DCIM/Camera/20170131_140546.jpg"));
//                                    putBinary("byte", getByte("/storage/emulated/0/DCIM/Camera/20170131_140546.jpg"),"20170131_140546.jpg");
//                                    putStream("stream", getFileInputStream("/storage/emulated/0/DCIM/Camera/20170131_140546.jpg"),"20170131_140546.jpg");
                                }})
                        .enableFileProgress()
        );

//        //already test, all work !!
        endpoint = "http://speedtest.ftp.otenet.gr/files/test10Mb.db";
        volleyPro.requestFile(
                VolleyPro.Method.GET,
                endpoint,
                new VolleyPro.Option()
                        .setHeader(
                                new HashMap<String, String>() {{
                                    put("userid", "xxxxx");
                                    put("usercode", "xxxxx");
                                    put("apiKey", "xxxxx");
                                }}
                        )
                        .setParameters(
                                new HashMap<String, String>() {{
                                    put("title", "this is title");
                                }}
                        )
                        .setCache(
                                "data/data/"+context.getPackageName()+"/apple.mp3",
                                0,
                                true
                        )
                        .enableFileProgress()
        );


    }

    private FileInputStream getFileInputStream(String filePath) {
        try {
            return new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getByte(String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    private void playFile(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Stop
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
        }
    }
}
