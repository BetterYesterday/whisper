package com.whisper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import lib.ChatApplication;

/**
 * Created by FullofOrange on 16. 8. 12..
 */


public class StartChatActivity extends Activity {

    TextInputEditText input_message;
    Button btn_sendmessage;

    Boolean isConnected = false;
    String cacheMessage;

    Socket socket;
    ChatApplication chatapp;
    SQLiteDatabase sqLiteDatabase;

    public int DB_MODE = Context.MODE_PRIVATE;
    public String DB_NAME = "RoomContents.db";

    private Socket startChatSocket;
    {
        try {
            startChatSocket = IO.socket("http://foo.mooncp.net:20902");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat);

        chatapp = new ChatApplication();
        socket = startChatSocket;
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, onConnection);
        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT,onConnectError);
        socket.on("chat_message",onGetRoomStatus);

        input_message = (TextInputEditText) findViewById(R.id.input_message);

        btn_sendmessage = (Button) findViewById(R.id.send_new_message);
        btn_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    socket.connect();
                }
                startChatLogin();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        socket.disconnect();
        isConnected=false;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        socket.disconnect();
        socket.off(Socket.EVENT_CONNECT,onConnection);
        socket.off(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR,onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT,onConnectError);
        socket.off("chat_message",onGetRoomStatus);
    }

    private Emitter.Listener onConnection = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = true;
                    Log.d("startchat_tag","startchat_con");
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Log.d("startchat_tag","startchat_discon");
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Log.d("startchat_tag","startchat_err");
                }
            });
        }
    };

    private Emitter.Listener onGetRoomStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                String isConnectRoom;
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        isConnectRoom = data.getString("roomstatus");
                    }catch (JSONException e){
                        return;
                    }
                    if(isConnectRoom.equals("connected")){
                        Log.d("startchat_tag","connect");
                        cacheMessage = input_message.getText().toString();
                        startChat(cacheMessage);
                    }else{
                        Toast.makeText(getApplicationContext(),"서버오류",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    private Emitter.Listener onGetRoomNum = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String roomnNum;
                    JSONObject data = (JSONObject) args[0];
                    try{
                        roomnNum = data.getString("room_num");
                    }catch (JSONException e){
                        return;
                    }
                    sqLiteDatabase.rawQuery("insert",);
                }
            });
        }
    };

    private void startChatLogin(){
        if(input_message.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "빈칸입니다.", Toast.LENGTH_SHORT).show();
        }else {
            JSONObject signInData = new JSONObject();
            try {
                signInData.put("Email", chatapp.getUserEmail());
                signInData.put("key", Integer.parseInt(chatapp.getKey()));
            } catch (JSONException e) {
                return;
            }
            socket.emit("sign_in", signInData);
        }
    }

    private void startChat(String message){
        JSONObject chatData = new JSONObject();
        try{
            chatData.put("message",message);
            chatData.put("status",true);
        }catch (JSONException e){
            return;
        }
        socket.emit("room_change",chatData);
    }

    public void openDB(){
        sqLiteDatabase = openOrCreateDatabase(DB_NAME,DB_MODE,null);
    }
}
