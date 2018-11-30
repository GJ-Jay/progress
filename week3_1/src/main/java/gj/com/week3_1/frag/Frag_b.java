package gj.com.week3_1.frag;

import android.content.ContentValues;
import android.database.Cursor;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import gj.com.week3_1.R;
import gj.com.week3_1.bean.Bean;
import gj.com.week3_1.sql.Dao;
import gj.com.week3_1.util.NetStateUtils;
import gj.com.week3_1.util.NetUtils;

public class Frag_b extends Fragment {

    private PullToRefreshListView plv_b;

    //路径
    String baseUrl = "http://api.expoon.com/AppNews/getNewsList/type/1/p/";
    //创建大集合
    ArrayList<Bean.DataBean> list = new ArrayList<>();
    int page;
    private String s;
    private long insert;
    private Dao dao;
    private MyAdaper myAdaper;
    private ImageLoader instance;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_b, container, false);
        //初始化组件
        plv_b = view.findViewById(R.id.plv_b);
        //允许上下拉
        plv_b.setMode(PullToRefreshBase.Mode.BOTH);
        instance = ImageLoader.getInstance();
        //设置适配器
        myAdaper = new MyAdaper();
        //添加适配器
        plv_b.setAdapter(myAdaper);
        //设置请求路径
        initData(page);
        //设置监听
        plv_b.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                list.clear();
                initData(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                int a = page++;
                initData(a);
            }
        });

        return view;
    }

    private void initData(int page) {
        //网络请求路径
        String s = baseUrl + page;
        //判断网络
        if (NetStateUtils.isConn(this)) {
            //有网络请求网络
            new MAsyncTask().execute(s);
        } else {
            //没有网路
            Toast.makeText(getContext(),
                    "没有网络，请稍后连接", Toast.LENGTH_SHORT).show();
            //查询数据库
            Cursor cursor = dao.query("jsontable", null, null, null,
                    null, null, null);
            //创建集合
            ArrayList<Bean.DataBean> beans = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    //具体到字段
                    String news_title = cursor.getString
                            (cursor.getColumnIndex("news_title"));

                    String news_summary = cursor.getString
                            (cursor.getColumnIndex("news_summary"));

                    String pic_url = cursor.getString
                            (cursor.getColumnIndex("pic_url"));

                    //创建对象添加到集合中
                    Bean.DataBean bean = new Bean.DataBean(news_title,
                            news_summary, pic_url);
                    beans.add(bean);
                } while (cursor.moveToNext());
            }
            list.addAll(beans);
        }
    }

    private class MyAdaper extends BaseAdapter {
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
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.fragitema, null);
                vh = new ViewHolder();
                vh.tv1 = convertView.findViewById(R.id.tv1);
                vh.tv2 = convertView.findViewById(R.id.tv2);
                vh.iv = convertView.findViewById(R.id.iv);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tv1.setText(list.get(position).getNews_summary());
            vh.tv2.setText(list.get(position).getNews_title());
            instance.displayImage(list.get(position).getPic_url(), vh.iv);
            return convertView;
        }
    }

    class ViewHolder {
        TextView tv1;
        TextView tv2;
        ImageView iv;
    }

    //请求网络获取数据
    private class MAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String json = NetUtils.getJson(strings[0]);
            return json;
        }

        //主线程
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //解析json并存入数据库
            Gson gson = new Gson();
            Bean bean = gson.fromJson(s, Bean.class);
            List<Bean.DataBean> data = bean.getData();
            //添加到集合中
            list.addAll(data);


            myAdaper.notifyDataSetChanged();
            //刷新头刷新底部
            plv_b.onRefreshComplete();
            //添加数据库
            dao = new Dao(getContext());
            /*//先删除
            dao.delete("jsontable",null,null);*/

            for (int i = 0; i < data.size(); i++) {
                Bean.DataBean dataBean = data.get(i);
                ContentValues values = new ContentValues();
                values.put("news_title", dataBean.getNews_title());
                values.put("news_summary", dataBean.getNews_summary());
                values.put("pic_url", dataBean.getPic_url());
                insert = dao.insert("jsontable", null, values);
            }


            Toast.makeText(getContext(), "添加数据到数据库的条数为：" + insert, Toast.LENGTH_SHORT).show();
        }
    }
}
