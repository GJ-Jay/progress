package gj.com.week3_1.util;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetUtils {
    public static String getJson(String string) {
        try {
            URL url = new URL(string);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //多了请求方式这一步
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(8000);//设置链接超时间
            int responseCode = urlConnection.getResponseCode();
            if(responseCode==200){
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //拼接字符串
                String temp = "";
                StringBuilder stringBuilder = new StringBuilder();
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
