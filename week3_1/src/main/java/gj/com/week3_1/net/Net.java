package gj.com.week3_1.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Net {
    public static String getStringJson(String string) {
        try {
            //网络请求
            URL url = new URL(string);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //获取响应码
            int responseCode = urlConnection.getResponseCode();
            //设置超时时间
            urlConnection.setConnectTimeout(8000);
            //判断
            if(responseCode==200){
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //拼接字符串
                String temp = "";
                StringBuilder stringBuilder= new StringBuilder();
                while ((temp = bufferedReader.readLine())!=null){
                    stringBuilder.append(temp);
                }
                return stringBuilder.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
