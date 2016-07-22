package com.whisper;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lib.ChatApplication;

public class LoginActivity extends AppCompatActivity{

    TextInputEditText input_id, input_password;
    Button signin_btn,signup_btn;

    Socket socket;

    Boolean isConnected = false;
    String email,password;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ChatApplication chatapp = new ChatApplication();
        socket = chatapp.getLoginSocket();

        input_id = (TextInputEditText) findViewById(R.id.input_email);
        input_password = (TextInputEditText) findViewById(R.id.input_password);
        signin_btn = (Button) findViewById(R.id.signin_btn);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = input_id.getText().toString();
                password = input_password.getText().toString();
                signIn(email,password);
            }
        });

        //소켓 on
        socket.on(Socket.EVENT_CONNECT, onConnection);
        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT,onConnectError);
        socket.on("login",onLogin);
    }
    @Override
    protected void onResume(){
        super.onResume();
        socket.connect();
    }
    @Override
    protected void onPause(){
        super.onPause();
        socket.disconnect();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        socket.disconnect();
        socket.off(Socket.EVENT_CONNECT,onConnection);
        socket.off(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR,onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT,onConnectError);
        socket.off("login",onLogin);
    }

    private void signIn(String email, String password){
        JSONArray data_array = new JSONArray();

        socket.emit("login",data_array);
    }
    private Emitter.Listener onConnection = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = true;
                    Log.d("login_tag","연결됨");
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
                    Log.d("login_tag","연결끊김");
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
                    Log.d("login_tag","에러");
                }
            });
        }
    };
    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                int isConnect;
                String Email;
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        isConnect = data.getInt("connect_status");
                        Email = data.getString("pushemail");
                    } catch (JSONException e) {
                        return;
                    }
                    Log.d("login_tag",Integer.toString(isConnect));
                    Log.d("login_tag",Email);
                    if(isConnect==1){
                        intent = new Intent(LoginActivity.this, ChatRoomActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    };

}
