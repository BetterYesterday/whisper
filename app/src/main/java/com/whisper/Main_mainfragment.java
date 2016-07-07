package com.whisper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Main_mainfragment extends Fragment{

    Button start_chat_btn;
    TextView count_duck;

    public static Main_mainfragment create(int pageNumber){
        Main_mainfragment fragment = new Main_mainfragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        start_chat_btn = (Button)getView().findViewById(R.id.start_chat_btn);
        count_duck = (TextView)getView().findViewById(R.id.count_duck);

        return inflater.inflate(R.layout.fragment_main_main, container, false);
    }
}
