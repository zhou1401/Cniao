package com.kaige.cniao.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaige.cniao.R;
import com.kaige.cniao.adapter.DividerItemDecortion;
import com.kaige.cniao.adapter.HomeCatgoryAdapter;
import com.kaige.cniao.bean.Banner;
import com.kaige.cniao.bean.HomeCategory;
import com.kaige.cniao.http.OkHttpHelper;
import com.kaige.cniao.http.SpotsCallBack;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;


/**
 * Created by Administrator on 2017/7/18.
 */

public class HomeFragment extends Fragment {
    private SliderLayout mSliderLayout;
//    private PagerIndicator  indicator;
    private RecyclerView mRecyclerView;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    private HomeCatgoryAdapter mAdatper;
    private Gson mGson=new Gson();
    private List<Banner> mBanner;

    private static  final  String TAG="HomeFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
//        indicator= (PagerIndicator) view.findViewById(R.id.custom_indicator);
        requestImages();
        initSlider();
        initRecyclerView(view);
        return  view;
    }
    private void requestImages(){
        String url ="http://112.124.22.238:8081/course_api/banner/query?type=1";
        /*OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormEncodingBuilder()
                .add("type","1")
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    String json=response.body().string();
                    Type type=new TypeToken<List<Banner>>(){}.getType();
                    mBanner=mGson.fromJson(json,type);

                    initSlider();
                }
            }
        });*/
        httpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()){
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                initSlider();
            }
            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        List<HomeCategory> datas = new ArrayList<>(15);

        HomeCategory category = new HomeCategory("热门活动",R.drawable.img_big_1,R.drawable.img_1_small1,R.drawable.img_1_small2);
        datas.add(category);

        category = new HomeCategory("有利可图",R.drawable.img_big_4,R.drawable.img_4_small1,R.drawable.img_4_small2);
        datas.add(category);
        category = new HomeCategory("品牌街",R.drawable.img_big_2,R.drawable.img_2_small1,R.drawable.img_2_small2);
        datas.add(category);

        category = new HomeCategory("金融街 包赚翻",R.drawable.img_big_1,R.drawable.img_3_small1,R.drawable.imag_3_small2);
        datas.add(category);

        category = new HomeCategory("超值购",R.drawable.img_big_0,R.drawable.img_0_small1,R.drawable.img_0_small2);
        datas.add(category);

        mAdatper = new HomeCatgoryAdapter(datas);
        mRecyclerView.setAdapter(mAdatper);
        mRecyclerView.addItemDecoration(new DividerItemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }
    private void initSlider(){
/*       DefaultSliderView textSliderView = new DefaultSliderView(this.getActivity());
        textSliderView.image("http://m.360buyimg.com/mobilecms/s480x180_jfs/t2278/35/409524152/232719/1d29f7a9/56078dbfNae4f16a3.jpg");
        textSliderView.description("aaaa");
        textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);

        DefaultSliderView textSliderView2 = new DefaultSliderView(this.getActivity());
        textSliderView2.image("http://m.360buyimg.com/mobilecms/s480x180_jfs/t2071/116/426908452/111248/3e6d388c/5608a437N723ee2ba.jpg");
        textSliderView2.description("bbbb");
        textSliderView2.setScaleType(BaseSliderView.ScaleType.Fit);

        DefaultSliderView textSliderView3 = new DefaultSliderView(this.getActivity());
        textSliderView3.image("http://m.360buyimg.com/mobilecms/s480x180_jfs/t2113/230/413819408/114393/d8a62616/56078bacN9c9c6dc8.jpg");
        textSliderView3.description("秒杀");
        textSliderView3.setScaleType(BaseSliderView.ScaleType.Fit);

        mSliderLayout.addSlider(textSliderView);
        mSliderLayout.addSlider(textSliderView2);
        mSliderLayout.addSlider(textSliderView3);*/
        if(mBanner !=null){
            for (Banner banner : mBanner){
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSliderLayout.stopAutoCycle();
    }
}