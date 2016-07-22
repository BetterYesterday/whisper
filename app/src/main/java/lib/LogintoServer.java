package lib;

import android.os.Handler;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by clp on 16. 7. 22.
 */
public class LogintoServer extends Thread {
    private String id;
    private String pwd;

    public LogintoServer(String username, String password){
        this.id=username;
        this.pwd=password;
    }

    @Override
    public void run() {
        mSocket.connect();
        Handler h = new Handler();
        h.handleMessage();

    }
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://foo.mooncp.net:20901");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
