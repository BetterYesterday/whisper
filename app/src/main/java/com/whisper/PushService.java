package com.whisper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.IBinder;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import lib.ChatApplication;

public class PushService extends Service {

    Socket socket;
    ChatApplication chatapp;
    SQLiteDatabase sqLiteDatabase;

    public int DB_MODE = Context.MODE_PRIVATE;
    public String DB_NAME = "RoomContents.db";


    Boolean isConnected = false;

    private Socket pushSocket;
    {
        try {
            pushSocket = IO.socket("http://foo.mooncp.net:20904");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        openDB();

        chatapp = new ChatApplication();
        socket = pushSocket;
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, onConnection);
        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT,onConnectError);
        socket.on(chatapp.getUserEmail(),onNewMessage);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        socket.disconnect();
        socket.off(Socket.EVENT_CONNECT, onConnection);
        socket.off(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR,onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT,onConnectError);
        socket.off(chatapp.getUserEmail(),onNewMessage);
    }

    private Emitter.Listener onConnection = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = true;
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = false;
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            isConnected = false;
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String message, messageTime;
            int roomNum;
            JSONObject data = (JSONObject) args[0];
            try{
                message = data.getString("new_message");
                roomNum = data.getInt("room_num");
                messageTime = data.getString("message_time");
            }catch (JSONException e){
                return;
            }
            try {
                sqLiteDatabase.execSQL("create table " + roomNum + " (my_message TEXT, other_message TEXT, time TEXT);");
                sqLiteDatabase.execSQL("insert into " + roomNum + " values (null," + message +"," + messageTime + ");");
            }catch (SQLiteException e){
                return;
            }

        }
    };

    public void openDB(){
        sqLiteDatabase = openOrCreateDatabase(DB_NAME,DB_MODE,null);
    }
    public void callIsMessageArrive(){

    }
}
