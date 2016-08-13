package com.whisper;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import lib.ChatApplication;

public class LoginActivity extends Activity{

    TextInputEditText input_id, input_password;
    Button signin_btn,signup_btn;

    private Socket socket;
    ChatApplication chatapp;

    private Boolean isConnected = false;
    String email,password;
    Intent intent;

    private Socket signupSocket;
    {
        try {
            signupSocket = IO.socket("http://foo.mooncp.net:20910");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        chatapp = new ChatApplication();
        socket = signupSocket;
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, onConnection);
        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT,onConnectError);
        socket.on("login",onSignIn);

        input_id = (TextInputEditText) findViewById(R.id.input_email);
        input_password = (TextInputEditText) findViewById(R.id.input_password);

        signin_btn = (Button) findViewById(R.id.signin_btn);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    socket.connect();
                    isConnected=true;
                }
                email = input_id.getText().toString();
                password = input_password.getText().toString();
                signIn(email,password);
            }
        });

        signup_btn = (Button) findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.disconnect();
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        //소켓 on
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
        socket.off("login",onSignIn);
    }

    private Emitter.Listener onConnection = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = true;
                    Log.d("login_tag","login_con");
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
                    Log.d("login_tag","login_discon");
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
                    Log.d("login_tag","login_err");
                }
            });
        }
    };
    private Emitter.Listener onSignIn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                int isConnect;
                String Key;
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        isConnect = data.getInt("connect_status");
                        Key = data.getString("pushcode");
                    } catch (JSONException e) {
                        return;
                    }
                    if(isConnect==1){//성공
                        chatapp.SaveKey(Key);
                        Toast.makeText(getApplicationContext(),"로그인 되었습니다!",Toast.LENGTH_SHORT).show();
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else if(isConnect==0){
                        chatapp.SaveEmail(null);
                        if(Key.equals("wrong_password")) {
                            Toast.makeText(getApplicationContext(), "비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                        }else if(Key.equals("null_email")){
                            Toast.makeText(getApplicationContext(), "가입되어있지 않은 이메일입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        chatapp.SaveEmail(null);
                        Toast.makeText(getApplicationContext(), "서버에러", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
    private void signIn(String email, String password) {
        if(email.equals("")||email.equals(" ")) {
            Toast.makeText(getApplicationContext(), "이메일이 빈칸입니다.", Toast.LENGTH_SHORT).show();
        }else if(password.equals("")||password.equals(" ")){
            Toast.makeText(getApplicationContext(),"비밀번호가 빈칸입니다.",Toast.LENGTH_SHORT).show();
        }else if(!chatapp.checkEmail(email)){
            Toast.makeText(getApplicationContext(),"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show();
        }else if(chatapp.checkEmail(email)) {
            JSONObject data = new JSONObject();
            try {
                data.put("email", email);
                data.put("password", password);
            } catch (JSONException e) {
                return;
            }
            chatapp.SaveEmail(email);
            socket.emit("sign_in", data);
        }
    }
}
