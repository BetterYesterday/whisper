package com.whisper;

/**
 * Created by FullofOrange on 16. 8. 14..
 */
public class ChatRoomListViewItem {
    String newMessage;
    String desc;
    int roomnum;

    public void setRoomnum(int roomNum){
        roomnum = roomNum;
    }

    public void setNewMessage(String message){
        newMessage = message;
    }

    public void setDesc(String stringdesc){
        desc = stringdesc;
    }

    public int getRoomnum(){
        return roomnum;
    }

    public String getNewMessage(){
        return newMessage;
    }

    public String getDesc(){
        return desc;
    }
}
