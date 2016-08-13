package com.whisper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import lib.ChatApplication;

public class Main_mainfragment extends Fragment{

    Button start_chat_btn;
    TextView count_duck;

    View view;

    ChatApplication chatapp;

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
        view = inflater.inflate(R.layout.fragment_main_main, container, false);

        chatapp = new ChatApplication();

        start_chat_btn = (Button) view.findViewById(R.id.start_chat_btn);
        start_chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),StartChatActivity.class);
                startActivity(intent);
            }
        });

        count_duck = (TextView) view.findViewById(R.id.count_duck);
        count_duck.setText(chatapp.getKey());

        return view;
    }
}
