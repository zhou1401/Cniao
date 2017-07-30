package com.kaige.cniao.http;

import android.os.Handler;
import android.os.Looper;
import android.service.voice.VoiceInteractionSession;

import com.google.gson.Gson;
import com.kaige.cniao.fragment.MineFragment;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.framed.FrameReader;
import com.squareup.okhttp.internal.http.HttpMethod;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/7/25.
 */


public class OkHttpHelper {
    private Handler mHandler;
    private Gson mGson;
    private OkHttpClient mHttpClient;
    private static OkHttpHelper mInstance;
    static {
        mInstance=new OkHttpHelper();
    }
    private OkHttpHelper(){
        mHttpClient=new OkHttpClient();
        mHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mHttpClient.setReadTimeout(10,TimeUnit.SECONDS);
        mHttpClient.setWriteTimeout(30,TimeUnit.SECONDS);
        mGson=new Gson();
        mHandler=new Handler(Looper.getMainLooper());
    }
    public static OkHttpHelper getInstance(){
        return mInstance;
    }
    public void get(String url,BaseCallback callback){
        Request request=buildGetRequest(url);
        request(request,callback);
    }

    private void request(final Request request, final BaseCallback callback) {
        callback.onBeforeRequest(request);
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFailure(request,e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.onResponse(response);
                if(response.isSuccessful()){
                    String resultStr=response.body().string();
                    if(callback.mType==String.class){
                        callbackSuccess(callback,response,resultStr);
                    }
                    else {
                        try{
                            Object obj=mGson.fromJson(resultStr,callback.mType);
                            callbackSuccess(callback,response,obj);
                        }catch (com.google.gson.JsonParseException e){
                            callback.onError(response,response.code(),e);
                        }
                    }
                }
                else {
                    callbackError(callback,response,null);
                }
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response,response.code(),e);
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response,obj);
            }
        });
    }

    private Request buildGetRequest(String url) {
        return buildRequest(url, HttpMethodType.GET,null);
    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String,String> params) {
        Request.Builder builder=new Request.Builder()
                .url(url);
        if(methodType==HttpMethodType.POST){
            RequestBody body=builderFormData(params);
            builder.post(body);
        }
        else if(methodType==HttpMethodType.GET){
            builder.get();
        }
        return builder.build();
    }

    private RequestBody builderFormData(Map<String, String> params) {

        FormEncodingBuilder builder=new FormEncodingBuilder();
        if(params!=null){
            for (Map.Entry<String,String>entry:params.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        return  builder.build();
    }

    enum HttpMethodType{
        GET,
        POST,
    }
}
