package lib;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by FullofOrange on 16. 7. 8..
 */
public class ChatApplication {

    public static String UserEmail="";

    private Socket mainSocket;
    {
        try {
            mainSocket = IO.socket("http://foo.mooncp.net:20900");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private Socket loginSocket;
    {
        try {
            loginSocket = IO.socket("http://foo.mooncp.net:20901");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public Socket getMainSocket() {
        return mainSocket;
    }
    public Socket getLoginSocket() {
        return loginSocket;
    }

    public void SaveEmail(String email){
        UserEmail = email;
    }
    public String getUserEmail(){
        if (!(UserEmail=="")) {
            return UserEmail;
        } else {
            return null;
        }
    }
}
