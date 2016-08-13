package lib;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * Created by FullofOrange on 16. 7. 8..
 */
public class ChatApplication {

    private static String UserEmail = "";

    private static String Key = null;

    public void SaveEmail(String email){
        UserEmail = email;
    }

    public void SaveKey(String key) { Key = key; }

    public String getKey() { return Key; }

    public String getUserEmail() { return UserEmail; }

    public static boolean checkEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",email.trim());
        return b;
    }
}
