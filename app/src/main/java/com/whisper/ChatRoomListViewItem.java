package com.whisper;

/**
 * Created by FullofOrange on 16. 8. 14..
 */
public class ChatRoomListViewItem {
    String newMessage;
    String desc;

    public void setNewMessage(String message){
        newMessage = message;
    }

    public void setDesc(String stringdesc){
        desc = stringdesc;
    }

    public String getNewMessage(){
        return newMessage;
    }

    public String getDesc(){
        return desc;
    }
}
