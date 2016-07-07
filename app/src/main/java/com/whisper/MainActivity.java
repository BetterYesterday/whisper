package com.whisper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

    int MAX_PAGES = 3;
    ViewPager viewpager;
    Button leftbtn, rightbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager = (ViewPager) findViewById(R.id.main_pager);
        viewpager.setAdapter(new Main_pagerAdapter(getSupportFragmentManager()));
        viewpager.setCurrentItem(1);

        leftbtn = (Button) findViewById(R.id.left_btn);
        leftbtn.setOnClickListener(onClickListener);
        rightbtn = (Button) findViewById(R.id.right_btn);
        rightbtn.setOnClickListener(onClickListener);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled ( int position, float positionOffset,
            int positionOffsetPixels){

            }
            @Override
            public void onPageSelected ( int position){
                if(viewpager.getCurrentItem()==0||viewpager.getCurrentItem()==2){
                    leftbtn.setVisibility(View.INVISIBLE);
                    rightbtn.setVisibility(View.INVISIBLE);
                }else{
                    leftbtn.setVisibility(View.VISIBLE);
                    rightbtn.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged ( int state){

            }
        }
        );
    }
    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.left_btn:
                    viewpager.setCurrentItem(0);
                    break;
                case R.id.right_btn:
                    viewpager.setCurrentItem(2);
                    break;
            }
        }
    };
    public class Main_pagerAdapter extends FragmentPagerAdapter {
        public Main_pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment returnfragment = null;
            if(!(position<0||position>=MAX_PAGES)){
                switch(position){
                    case 0:
                        returnfragment = Main_profilefragment.create(position);
                        break;
                    case 1:
                        returnfragment = Main_mainfragment.create(position);
                        break;
                    case 2:
                        returnfragment = Main_settingfragment.create(position);
                        break;
                }
            }
            return returnfragment;
        }
        @Override
        public int getCount() {
            return MAX_PAGES;
        }
    }
}
