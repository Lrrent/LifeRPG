package com.example.da.liferpg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ViewPager myViewPager;

    // Ҫʹ�õ�ViewPager
    private View page1, page2, page3,page4;
    // ViewPager������ҳ��
    private List<View> pageList;
    // View     Pager������ҳ���б�һ���adapter������һ��list
    private MyPagerAdapter myPagerAdapter;
    private TextView te;
    private Intent intent;
    SharedPreferences loginJudge;  //�ж�֮ǰ�Ƿ��½��
    // ������
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginJudge = getSharedPreferences("user",MODE_PRIVATE); //��sharepreference��ȡ����½��Ϣ
        String logined = loginJudge.getString("logined","noLogined"); //Ĭ��û�е�½
       // Log.d("flag",logined);
        if(logined.equals("logined")){
            //Log.d("ahah","haha");
            intent = new Intent(MainActivity.this,main_window.class);
            startActivity(intent);
        }
        else{
            setContentView(R.layout.welcome);
            findAllView();
            initView();
        }
    }
    private void findAllView(){
        myViewPager = (ViewPager) findViewById(R.id.myViewPager);
    }
    private void initView() {
        // TODO Auto-generated method stub
        LayoutInflater inflater = getLayoutInflater();
        page1 = inflater.inflate(R.layout.page1, null);
        page2 = inflater.inflate(R.layout.page2, null);
        page3 = inflater.inflate(R.layout.page3, null);
        page4 = inflater.inflate(R.layout.page4, null);
        pageList = new ArrayList<View>();
        pageList.add(page1);
        pageList.add(page2);
        pageList.add(page3);
        pageList.add(page4);
        myPagerAdapter = new MyPagerAdapter(pageList);
        myViewPager.setAdapter(myPagerAdapter);
    }
    public class MyPagerAdapter extends PagerAdapter {
        private List<View> pageList;

        public MyPagerAdapter(List<View> pageList) {

            this.pageList = pageList;
        }
        @Override
        public int getCount() {
            // ����Ҫչʾ��ͼƬ����
            return pageList.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            // �տ�ʼ��viewpager��ֱ��д��return arg0 == arg1;���ͺ���
            return arg0 == arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            // ����ǰλ�õ�View�Ƴ�
            container.removeView(pageList.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position){
            container.addView(pageList.get(position));
            te = (TextView)findViewById(R.id.te);
            if(position == 3){
                te.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });

            }
            System.out.println(position);
            return pageList.get(position);
        }
    }


}



