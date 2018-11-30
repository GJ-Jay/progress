package gj.com.week3_1.frag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

import gj.com.week3_1.R;

public class Frag01 extends Fragment {

    private ViewPager pager;
    private TabLayout tabLayout;
    //创建大集合
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<Fragment> fragmentList = new ArrayList<>();//存放切换页面的帧布局

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag01,container,false);
        //初始化组件
        pager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tablayout);
        // 添加数据
        for (int i = 0;i<3;i++){
            titleList.add("标题"+(i+1));
        }
        //添加视图
        Frag_a frag_a = new Frag_a();
        Frag_b frag_b = new Frag_b();
        Frag_c frag_c = new Frag_c();
        fragmentList.add(frag_a);
        fragmentList.add(frag_b);
        fragmentList.add(frag_c);
        //设置适配器  注意括号
        pager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount() {
                return titleList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        });
        //设置模式
        /**
         * 注意 设置横向模式
         */
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //将tablayout和viewpager结合
        tabLayout.setupWithViewPager(pager);
        return view;
    }
}
