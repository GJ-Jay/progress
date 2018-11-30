package gj.com.week3_1.frag;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import gj.com.week3_1.R;
import gj.com.week3_1.bean.Bean;
import gj.com.week3_1.net.Net;

/**
 * 设置无限轮播和上下拉加载
 */
public class Frag_a extends Fragment {

    private Banner banner;
    private PullToRefreshListView plv;
    private com.nostra13.universalimageloader.core.ImageLoader instance;
    int page;
    //网络路径
    String urlString = "http://api.expoon.com/AppNews/getNewsList/type/1/p/";
    String urlBitmap = "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg";
    //创建大集合
    ArrayList<String> bitmaplist = new ArrayList<>();
    ArrayList<Bean.DataBean> list = new ArrayList<>();
    private MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_a,container,false);
        //初始化组件
        banner = view.findViewById(R.id.banner);
        plv = view.findViewById(R.id.plv);
        /**
         * 设置无线轮播 注意具体的适配
         */
        //设置  要放在数据的上边
        banner.setImageLoader(new ImageLoader() {

            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //获取实例
                instance = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
                //设置图片 第一个参数路径 第二个参数 布局
                instance.displayImage((String) path,imageView);
            }
        });
        //图片数据
        for (int i =0;i<4;i++){
            bitmaplist.add(urlBitmap);
        }

        //设置images
        banner.setImages(bitmaplist);
        //调用start
        banner.start();

        /**
         * 设置上拉加载下拉刷新PullToRefresh
         */
        //1.设置允许上下拉方法模式
        plv.setMode(PullToRefreshBase.Mode.BOTH);
        //设置适配器
        myAdapter = new MyAdapter();
        //添加
        plv.setAdapter(myAdapter);
        //请求网络
        initData(page);
        //设置上下拉监听
        plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                list.clear();
                initData(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                page++;
                initData(page);
            }
        });

        return view;
    }

    //调用请求网络的方法
    private void initData(int page) {
        String s = urlString + page;
        new MAsyncTask().execute(s);
    }

    //请求网络
    private class MAsyncTask extends AsyncTask<String,Void,String> {
        //子线程
        @Override
        protected String doInBackground(String... strings) {
            String stringJson = Net.getStringJson(strings[0]);
            return stringJson;
        }
        //主线程

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //解析json
            Gson gson = new Gson();
            Bean bean = gson.fromJson(s, Bean.class);
            List<Bean.DataBean> data = bean.getData();
            //添加到大集合中
            list.addAll(data);
            //刷新适配器
            myAdapter.notifyDataSetChanged();
            //取消刷新头和底
            plv.onRefreshComplete();
        }
    }

    private class MyAdapter extends BaseAdapter {
        //多条目

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position%2;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //多条目
            int itemViewType = getItemViewType(position);
            ViewHolder vh = null;
            switch (itemViewType){
                case 0:
                    if(convertView==null){
                        convertView = View.inflate(getContext(),R.layout.fragitema,null);
                        vh = new ViewHolder();
                        vh.tv1 = convertView.findViewById(R.id.tv1);
                        vh.tv2 = convertView.findViewById(R.id.tv2);
                        vh.iv = convertView.findViewById(R.id.iv);
                        convertView.setTag(vh);
                    }else{
                        vh = (ViewHolder) convertView.getTag();
                    }
                    vh.tv1.setText(list.get(position).getNews_title());
                    vh.tv2.setText(list.get(position).getNews_summary());
                    instance.displayImage(list.get(position).getPic_url(),vh.iv);
                    break;

                case 1:
                    if(convertView==null){
                        convertView = View.inflate(getContext(),R.layout.fragitema,null);
                        vh = new ViewHolder();
                        vh.tv1 = convertView.findViewById(R.id.tv1);
                        vh.tv2 = convertView.findViewById(R.id.tv2);
                        convertView.setTag(vh);
                    }else{
                        vh = (ViewHolder) convertView.getTag();
                    }
                    vh.tv1.setText(list.get(position).getNews_title());
                    vh.tv2.setText(list.get(position).getNews_summary());
                    break;
            }
            return convertView;
        }
    }
    class ViewHolder{
        TextView tv1;
        TextView tv2;
        ImageView iv;
    }
}
