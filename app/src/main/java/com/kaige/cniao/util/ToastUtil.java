package com.kaige.cniao.util;

import android.content.Context;
import android.widget.Toast;

import com.kaige.cniao.R;

/**
 * Created by Administrator on 2017/7/24.
 */

public class ToastUtil {
    public static void show(Context context,String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
