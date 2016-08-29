package com.whisper;

/**
 * Created by FullofOrange on 16. 8. 14..
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomListViewAdapter extends BaseAdapter {

    private ArrayList<ChatRoomListViewItem> listViewItemList = new ArrayList<ChatRoomListViewItem>() ;

    ChatRoomListViewItem listViewItem;

    public ChatRoomListViewAdapter() {

    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contents_listview, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;

        listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getNewMessage());
        descTextView.setText(listViewItem.getDesc());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(int roomnum, String message, String desc) {
        ChatRoomListViewItem item = new ChatRoomListViewItem();

        item.setNewMessage(message);
        item.setDesc(desc);

        listViewItemList.add(item);
    }
}
