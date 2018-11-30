 package gj.com.week3_1;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import gj.com.week3_1.frag.Frag01;
import gj.com.week3_1.frag.Frag02;
import gj.com.week3_1.frag.Frag03;

 public class MainActivity extends AppCompatActivity {

     private FrameLayout frag;
     private RadioGroup radioGroup;
     private FragmentManager fragmentManager;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化组件
         frag = findViewById(R.id.frag);
         radioGroup = findViewById(R.id.radio);

         //使用帧布局切换页面
         //获取事务
         FragmentManager fragmentManager = getSupportFragmentManager();
         //开启事务
         FragmentTransaction transaction = fragmentManager.beginTransaction();
         //添加事务
         final Frag01 frag01 = new Frag01();
         final Frag02 frag02 = new Frag02();
         final Frag03 frag03 = new Frag03();
         transaction.add(R.id.frag,frag01).show(frag01);
         transaction.add(R.id.frag,frag02).hide(frag02);
         transaction.add(R.id.frag,frag03).hide(frag03);
         //提交事务
         transaction.commit();

         //选中按钮 切换页面
         //默认第一个页面选中
         radioGroup.check(radioGroup.getChildAt(0).getId());
         radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                 switch (checkedId){
                     case R.id.radio1:
                         transaction1.show(frag01).hide(frag02).hide(frag03);
                         break;
                     case R.id.radio2:
                         transaction1.show(frag02).hide(frag01).hide(frag03);
                         break;
                     case R.id.radio3:
                         transaction1.show(frag03).hide(frag02).hide(frag01);
                         break;
                 }
                 //提交事务
                 transaction1.commit();
             }
         });
     }
}
