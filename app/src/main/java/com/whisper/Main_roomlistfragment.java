package com.whisper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by FullofOrange on 16. 6. 30..
 */
public class Main_roomlistfragment extends Fragment {

    ListView listView;
    ChatRoomListViewAdapter adapter;

    SQLiteDatabase roomNumDatabase;
    SQLiteDatabase roomContentsDatabase;
    public int DB_MODE = Context.MODE_PRIVATE;
    public String DB_NAME_ROOMNUM = "RoomNum.db";
    public String DB_NAME_ROOOMCONTENT = "RoomContents.db";

    int[] itemIntent;
    int itemIntentCount;

    public static Main_roomlistfragment create(int pageNumber){
        Main_roomlistfragment fragment = new Main_roomlistfragment();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_roomlist, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ChatRoomListViewAdapter();
        listView.setAdapter(adapter);

        openDB();
        try {
            roomNumDatabase.execSQL("create table Roomnum (id INTEGER, roomNum INTEGER)");
        }catch (SQLiteException e){

        }
        Cursor cursor = roomNumDatabase.rawQuery("select * from Roomnum",null);
        cursor.moveToLast();
        itemIntentCount=0;
        while(!cursor.isAfterLast()){
            Cursor mCursor = roomContentsDatabase.rawQuery("select * from "+cursor.getInt(0),null);
            mCursor.moveToLast();
            if(mCursor.getString(1)==null) {
                adapter.addItem(cursor.getInt(0), mCursor.getString(2),null);
            }else{
                adapter.addItem(cursor.getInt(0), mCursor.getString(1),null);
            }
            itemIntent[itemIntentCount]=cursor.getInt(0);
            itemIntentCount++;
            cursor.moveToNext();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ChatRoomActivity.class);
                intent.putExtra("room_num",itemIntent[position]);
                startActivity(intent);
            }
        });
        return view;
    }

    public void openDB(){
        roomNumDatabase = getActivity().openOrCreateDatabase(DB_NAME_ROOMNUM,DB_MODE,null);
        roomContentsDatabase = getActivity().openOrCreateDatabase(DB_NAME_ROOOMCONTENT,DB_MODE,null);
    }
}
