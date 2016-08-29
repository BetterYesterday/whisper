package lib;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by FullofOrange on 16. 8. 17..
 */
public class ChatDBManager {

    private static String DB_NAME = "RoomInfo.db";

    private static String TABLE_ROOM_NUM = "RoomNum";

    private static int DB_VERSION = 1;

    private OpenHelper openHelper;
    private SQLiteDatabase sqliteDatabase;

    private Context context;

    public ChatDBManager(Context context) {
        this.context = context;
        this.openHelper = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        sqliteDatabase = openHelper.getWritableDatabase();
    }

    private class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
        int version) {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            sqliteDatabase.execSQL("create table ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void createTable(){

    }

    public void insertData(){

    }

    public void deleteData(){

    }

    public void updataData(){

    }

    public Cursor selectData(){

    }

}
