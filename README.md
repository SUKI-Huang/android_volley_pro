# What is VolleyPro?
This library is base on [android-volley](https://github.com/mcxiaoke/android-volley), in order to decrease code and save developer's time, it easier to use than orginal volley library.

# Why VolleyPro?
In the oringal Volley project, there are many kinds of request, developers use complicated flow and lots of code to build request, in addition, the usability of cache feature is not enough, so this is what VolleyPro solve for.
  - Short and simple code
  - Process cache automatically
  - Support generics class (by using [Gson](https://github.com/google/gson))

# Usage
**1. Gradle dependency** (recommended)
  -  Add the following to your project level `build.gradle`:
 
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

  -  Add this to your app `build.gradle`:
 
```gradle
android {
    ...
    useLibrary 'org.apache.http.legacy'
    packagingOptions {
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
    }
    ...
}
dependencies {
    compile 'com.github.SUKI-Huang:android_volley_pro:1.0.16'
    //Libraries down below are required
    compile 'com.google.code.gson:gson:2.4'
    compile 'com.android.support:support-annotations:25.1.1'
    compile 'org.apache.httpcomponents:httpclient-android:4.3.5.1'
    compile('org.apache.httpcomponents:httpmime:4.3.6') {
        exclude module: "httpclient"
    }
   
}
```

**2. Proguard file**
```
#apache
-keep class org.apache.http.** { *; }
-keep class org.apache.commons.codec.** { *; }
-keep class org.apace.commons.logging.** { *; }

#android net relative
-keep class com.android.internal.http.multipart.** { *; }
-keep class android.net.compatibility.** { *; }
-keep class android.net.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**
```

**3. In your Java code**

  -  Basic string request:
```
String endpoint = "https://xxx.xxx.xxx/xxx/xx/xx";
volleyPro.setOnEvent(new SimpleEvent<String>(String.class) {
    @Override
    public void OnSuccess(String result) {
        //on success
    }

    @Override
    public void OnFailed(int code, String msg) {
        //on failed
    }
});
volleyPro.request(Method.GET, endpoint);

```

  -  String request with header and parameters:
```
String endpoint = "https://xxx.xxx.xxx/xxx/xx/xx";
volleyPro.setOnEvent(new SimpleEvent<String>(String.class) {
    @Override
    public void OnSuccess(String result) {
        //on success
    }

    @Override
    public void OnFailed(int code, String msg) {
        //on failed
    }
});

volleyPro.request(
        Method.POST,
        endpoint,
        new BaseVolleyPro.Option()
                .setHeader(
                        new HashMap<String, String>() {
                            {
                                put("token", "xxxxxxxxxx");
                                put("userid", "xxxxxxxxx");
                            }
                        }
                )
                .setParameters(
                        new HashMap<String, String>() {
                            {
                                put("name", "Suki");
                                put("gender", "1");
                            }
                        }
                )
);

```
  -  String request with header, parameters, and cache setting :
```
String endpoint="https://xxx.xxx.xxx/xxx/xx/xx";
volleyPro.setOnEvent(new SimpleEvent<String>(String.class) {
    @Override
    public void OnSuccess(String result) {
        //on success
    }

    @Override
    public void OnFailed(int code, String msg) {
        //on failed
    }
});

volleyPro.request(
        Method.GET,
        endpoint,
        new VolleyPro.Option()
                //set header (optional)
                .setHeader(
                        new HashMap<String, String>() {{
                            put("userid", "xxxx");
                            put("usercode", "xxxx");
                            put("apiKey", "xxxx");
                        }}
                )
                //set parameters (optional)
                .setParameters(
                        new HashMap<String, String>() {{
                            put("title", "this is title");
                        }}
                )
                //set cache (optional)
                .setCache(
                        //cache path
                        "data/data/" + getPackageName() + "/volleyCache.json",

                        //expired time (millisecond)
                        6000,

                        //if network is unavailable use older cache
                        true
                )
);

```
  - MultiPart request
```
String endpoint = "https://xxx.xxx.xxx/xxx/xx/xx";
volleyPro.setOnEvent(new SimpleEvent<String>(String.class) {
    @Override
    public void OnSuccess(String result) {
        //on success
    }

    @Override
    public void OnFailed(int code, String msg) {
        //on failed
    }

    @Override
    public void OnMultiPartProgress(float progress) {
        //uplaod progress
    }
});

volleyPro.request(
        endpoint,
        new VolleyPro.MultiPartOption()
                .setHeader(
                        new HashMap<String, String>() {{
                            put("userid", "xxxx");
                            put("usercode", "xxxx");
                            put("apiKey", "xxxx");
                        }}
                )
                //set parameters (optional)
                .setParameters(
                        new ContentHashMap<String, ContentBody>() {{
                            putText("title", "this is title");
                            putFile("file", filePath);
                            putFile("file", filePath);
                            putFile("file", new File(filePath));
                            putBinary("byte", getByte("filePath"), "xxx.jpg");
                            putStream("stream", getFileInputStream(filePath), "xxx.jpg");
                        }})
                .enableMultiPartProgress()
);
```
  -  File request:
```
String endpoint="https://xxx.xxx.xxx/xxx/xx/xx";
volleyPro.setOnEvent(new SimpleEvent<File>(File.class) {
    @Override
    public void OnSuccess(File file) {
        //on success
    }

    @Override
    public void OnFailed(int code, String msg) {
        //on failed
    }
    @Override
    public void OnFileProgress(float progress) {
        //on download progress changed
    }
});

volleyPro.requestFile(
        Method.GET,
        endpoint,
        new VolleyPro.Option()
                //set header (optional)
                .setHeader(
                        new HashMap<String, String>() {{
                            put("userid", "xxxx");
                            put("usercode", "xxxx");
                            put("apiKey", "xxxx");
                        }}
                )
                //set parameters (optional)
                .setParameters(
                        new HashMap<String, String>() {{
                            put("title", "this is title");
                        }}
                )
                //set cache (required)
                .setCache(
                        //cache path
                        "data/data/" + getPackageName() + "/volleyCache.json",

                        //expired time (millisecond)
                        6000,

                        //if network is unavailable use older cache
                        true
                )
                //enable FileProgress callback
                .enableFileProgress()
);

```

  -  Generic after string request:
  
  Example is `DataItem`, change `DataItem` to the class you need, VolleyPro will convert via gson automatically
```
String endpoint="https://xxx.xxx.xxx/xxx/xx/xx";
volleyPro.setOnEvent(new SimpleEvent<DataItem>(DataItem.class) {
    @Override
    public void OnSuccess(DataItem dataItem) {
        //on success
    }

    @Override
    public void OnFailed(int code, String msg) {
        //on failed
    }
});

volleyPro.request(Method.GET, endpoint);

```

  -  Generic after string request (ArrayList):
  
  Example is `DataItem`, change `DataItem` to the class you need, VolleyPro will convert via gson automatically
```
String endpoint="https://xxx.xxx.xxx/xxx/xx/xx";
volleyPro.setOnEvent(new SimpleEvent<ArrayList<DataItem>>((Class<ArrayList<User>>) new ArrayList<DataItem>().getClass()) {
    @Override
    public void OnSuccess(DataItem dataItem) {
        //on success
    }

    @Override
    public void OnFailed(int code, String msg) {
        //on failed
    }
});

volleyPro.request(Method.GET, endpoint);

```

## License


    Copyright (C) 2017 Suki Huang
    Copyright (C) 2011 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

