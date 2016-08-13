package com.whisper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
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
import org.w3c.dom.Text;

import java.net.URISyntaxException;

import lib.ChatApplication;

/**
 * Created by FullofOrange on 16. 8. 10..
 */
public class SignUpActivity extends Activity {

    TextInputEditText input_email, input_password, input_password_resume;
    Button checkEmail_btn, signUp_btn;

    private Boolean isConnected = false;
    int isCheckEmail = 0;
    String emailCash = null;

    ChatApplication chatapp;
    private Socket socket;

    Intent intent;

    private Socket signUpSocket;
    {
        try {
            signUpSocket = IO.socket("http://foo.mooncp.net:20911");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        chatapp = new ChatApplication();
        socket = signUpSocket;
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, onConnection);
        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT,onConnectError);
        socket.on("check_email",onCheckEmail);
        socket.on("sign_up",onSignUp);

        input_email = (TextInputEditText)findViewById(R.id.input_email);
        input_password = (TextInputEditText)findViewById(R.id.input_password);
        input_password_resume = (TextInputEditText)findViewById(R.id.input_password_resume);

        checkEmail_btn = (Button)findViewById(R.id.checkemail_btn);
        checkEmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    socket.connect();
                    isConnected=true;
                }
                checkEmail(input_email.getText().toString());
            }
        });
        signUp_btn = (Button)findViewById(R.id.signup_btn);
        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    socket.connect();
                    isConnected=true;
                }
                signUp(input_password.getText().toString(),input_password_resume.getText().toString());
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
        socket.off("check_email",onCheckEmail);
        socket.off("sign_up",onSignUp);
    }

    private Emitter.Listener onConnection = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = true;
                    Log.d("login_tag","signup_con");
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
                    Log.d("login_tag","signup_discon");
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
                    Log.d("login_tag","signup_err");
                }
            });
        }
    };
    private Emitter.Listener onSignUp = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                int isConnect;
                String Key;
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        isConnect = data.getInt("connect_status");
                        Key = data.getString("pushcode");
                    }catch (JSONException e){
                        return;
                    }
                    if(isConnect==1){
                        chatapp.SaveKey(Key);
                        intent = new Intent(SignUpActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"서버오류",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
    private Emitter.Listener onCheckEmail = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        isCheckEmail = data.getInt("check_status");
                        emailCash = data.getString("push_email");
                    }catch (JSONException e){
                        return;
                    }
                    if(isCheckEmail==1){
                        chatapp.SaveEmail(emailCash);
                        Toast.makeText(getApplicationContext(),"사용가능한 이메일입니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        emailCash = null;
                        Toast.makeText(getApplicationContext(), "이미 사용중인 이메일 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
    private void signUp(String Password, String Password_resume){
        if(Password.equals("")){
            Toast.makeText(getApplicationContext(),"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
        }else if(Password_resume.equals("")) {
            Toast.makeText(getApplicationContext(),"비밀번호를 확인하세요.",Toast.LENGTH_SHORT).show();
        }else if(!Password.equals(Password_resume)){
            Toast.makeText(getApplicationContext(),"비밀번호가 다릅니다.",Toast.LENGTH_SHORT).show();
            input_password_resume.setText("");
        }else{
            JSONObject data = new JSONObject();
            try{
                data.put("email",chatapp.getUserEmail());
                data.put("password",Password);
            }catch (JSONException e){
                return;
            }
            socket.emit("sign_up",data);
        }
    }
    private void checkEmail(String Email){
        if(!chatapp.checkEmail(Email)){
            Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        }else{
            JSONObject data = new JSONObject();
            try{
                data.put("email",Email);
            }catch (JSONException e){

            }
            socket.emit("check_email",data);
        }
    }
}
