package com.copasso.cocobill.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhouas666 on 2017/12/28.
 * OkHttp网络连接封装工具类
 */
public class OkHttpUtils {
    private OkHttpClient client;
    private static OkHttpUtils instance;

    public OkHttpUtils() {
        client = new OkHttpClient();
    }

    /**
     * 获取句柄
     * @return
     */
    public static OkHttpUtils getInstance() {
        if(instance == null) {
            instance = new OkHttpUtils();
        }

        return instance;
    }

    /**
     * post 请求
     * @param url
     * @param params
     * @param callback
     */
    public void post(final String url, final Map<String, String> params, Callback callback) {
        post(null, url, params, callback);
    }

    /**
     * post 请求
     * @param context 发起请求的context
     * @param url
     * @param params
     * @param callback
     */
    public void post(Context context, final String url, final Map<String, String> params, Callback callback ) {
        //post builder 参数
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request;

        //发起request
        if(context == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .tag(context)
                    .build();
        }


        client.newCall(request).enqueue(callback);
    }

    /**
     * get 请求
     * @param url url
     * @param params 参数
     * @param callback 回调
     */
    public void get(final String url, final Map<String, String> params, Callback callback) {
        get(null, url, params, callback);
    }

    /**
     * get 请求
     * @param context 发起请求的context
     * @param url url
     * @param params 参数
     * @param callback 回调
     */
    public void get(Context context, final String url, final Map<String, String> params, Callback callback) {
        //拼接url
        String get_url = url;
        if(params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if(i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }

        Request request;

        //发起request
        if(context == null) {
            request = new Request.Builder()
                    .url(get_url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(get_url)
                    .tag(context)
                    .build();
        }

        client.newCall(request).enqueue(callback);
    }

    /**
     * 上传文件
     * @param url url
     * @param files 上传的文件files
     * @param callback 回调
     */
    public void upload(String url, Map<String, File> files, Callback callback) {
        upload(null, url, null, files, callback);
    }

    /**
     * 上传文件
     * @param url url
     * @param params 参数
     * @param files 上传的文件files
     * @param callback 回调
     */
    public void upload(String url, Map<String, String> params, Map<String, File> files, Callback callback) {
        upload(null, url, params, files, callback);
    }

    /**
     * 上传文件
     * @param context 发起请求的context
     * @param url url
     * @param files 上传的文件files
     * @param callback 回调
     */
    public void upload(Context context, String url, Map<String, File> files, Callback callback) {
        upload(context, url, null, files, callback);
    }

    /**
     * 上传文件
     * @param context 发起请求的context
     * @param url url
     * @param params 参数
     * @param files 上传的文件files
     * @param callback 回调
     */
    public void upload(Context context, String url, Map<String, String> params, Map<String, File> files, Callback callback) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //添加参数
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }

        //添加上传文件
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                multipartBuilder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        Request request;
        if(context == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(multipartBuilder.build())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(multipartBuilder.build())
                    .tag(context)
                    .build();
        }

        client.newCall(request).enqueue(callback);
    }

    /**
     * 下载文件
     * @param url 下载地址
     * @param filedir 下载目的目录
     * @param filename 下载目的文件名
     * @param callback 下载回调
     */
    public void download(String url, String filedir, String filename, Callback callback) {
        download(null, url, filedir, filename, callback);
    }

    /**
     * 下载文件
     * @param context 发起请求的context
     * @param url 下载地址
     * @param filedir 下载目的目录
     * @param filename 下载目的文件名
     * @param callback 下载回调
     */
    public void download(Context context, String url, String filedir, String filename, Callback callback) {

        Request request;
        if(context == null) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .tag(context)
                    .build();
        }

        client.newBuilder()
                .addNetworkInterceptor(new Interceptor() {      //设置拦截器
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(originalResponse.body())
                                .build();
                    }
                })
                .build()
                .newCall(request)
                .enqueue(callback);
    }

    /**
     * 取消当前context的所有请求
     * @param context
     */
    public void cancel(Context context) {
        if(client != null) {
            for(Call call : client.dispatcher().queuedCalls()) {
                if(call.request().tag().equals(context))
                    call.cancel();
            }
            for(Call call : client.dispatcher().runningCalls()) {
                if(call.request().tag().equals(context))
                    call.cancel();
            }
        }
    }

    //保存文件
    private File saveFile(Response response, String filedir, String filename) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(filedir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

    //获取mime type
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
