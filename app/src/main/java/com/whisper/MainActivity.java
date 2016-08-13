package com.whisper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

    int MAX_PAGES = 3;
    ViewPager viewpager;
    Button leftbtn, rightbtn;
    Toolbar toolbar;

    SQLiteDatabase sqLiteDatabase;
    public int DB_MODE = Context.MODE_PRIVATE;
    public String DB_NAME = "RoomNum.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);

        openDB();
        try {
            sqLiteDatabase.execSQL("create table Roomnum (id INTEGER, roomNum INTEGER);");
        }catch (SQLiteException e){

        }
        viewpager = (ViewPager) findViewById(R.id.main_pager);
        viewpager.setAdapter(new Main_pagerAdapter(getSupportFragmentManager()));
        viewpager.setCurrentItem(1);
        toolbar.setTitle("샤니또");
        /*
        leftbtn = (Button) findViewById(R.id.left_btn);
        leftbtn.setOnClickListener(onClickListener);
        rightbtn = (Button) findViewById(R.id.right_btn);
        rightbtn.setOnClickListener(onClickListener);
        */
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewpager.getCurrentItem() == 0 ) {
                    toolbar.setTitle("프로필보기");
                    //leftbtn.setVisibility(View.INVISIBLE);
                    //rightbtn.setVisibility(View.INVISIBLE);
                } else if(viewpager.getCurrentItem() == 2){
                    toolbar.setTitle("방목록");
                    //leftbtn.setVisibility(View.INVISIBLE);
                    //rightbtn.setVisibility(View.INVISIBLE);
                } else {
                    toolbar.setTitle("샤니또");
                    //leftbtn.setVisibility(View.VISIBLE);
                    //rightbtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
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
                        returnfragment = Main_roomlistfragment.create(position);
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

    public void openDB(){
        sqLiteDatabase = openOrCreateDatabase(DB_NAME,DB_MODE,null);
    }
}
