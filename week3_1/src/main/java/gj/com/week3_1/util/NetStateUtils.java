package gj.com.week3_1.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import gj.com.week3_1.frag.Frag_b;

public class NetStateUtils {

    private static ConnectivityManager connectivityManager;

    public static boolean isConn(Frag_b frag_b) {
        //设置一个布尔类型的变量
        boolean flag = false;
        //上下文获取系统服务Context.CONNECTIVITY_SERVICE
        frag_b.getActivity().getSystemService(frag_b.getActivity().CONNECTIVITY_SERVICE);
        //获取网络信息
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //判断不为空
        if(networkInfo!=null){
            //可用网
            flag = connectivityManager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }
}
