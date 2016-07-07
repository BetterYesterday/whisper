package lib;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by FullofOrange on 16. 7. 8..
 */
public class ChatApplication {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://foo.mooncp.net:20800");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
