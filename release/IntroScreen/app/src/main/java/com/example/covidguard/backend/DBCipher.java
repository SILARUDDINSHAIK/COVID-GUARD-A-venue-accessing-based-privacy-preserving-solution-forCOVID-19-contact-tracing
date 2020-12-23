package com.example.covidguard.backend;
import android.app.NotificationManager;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import com.example.covidguard.R;
import com.example.covidguard.beacon.BeaconHandler;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import java.io.File;
import com.example.covidguard.dto.VenueRecordDto;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
public class DBCipher extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "UserDbCipher";
    private static final String TABLE_Users = "UserInformation";
    private static final String KEY_UUID = "UUID";
    private static final String KEY_START_TIME = "Start_Time";
    private static final String KEY_END_TIME = "End_Time";
    private static final String INFECTED = "InfectedUserData";
    private static final String KEY_uuid = "UUID";
    private static final String KEY_start_time = "Start_Time";
    private static final String KEY_end_time = "End_Time";
    private static final String TABLE_Infected_Users = "InfectedUserInformation";
    private static final String KEY_IsINFECTED = "IsInfected";
    private static final String KEY_INFECTED_DATE = "Infected_Date";
    private static final String KEY_LAST_UPDATED_DATE = "Last_Updated_Date";
    private static final String myname ="test123";
    private static final String PASSWORD = myname;
    private static final String TAG = "DB_CIPHER";
    //private static final String MY_QUERY = "";
    private long currentRowId;
    private Context myContext;
    public DBCipher(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

        myContext = context;
    }
    public void createTable() {

        SQLiteDatabase.loadLibs(myContext);
        File db_path = myContext.getDatabasePath(DB_NAME + ".db");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(db_path, PASSWORD, null);

        Log.d(BeaconHandler.TAG, "Database path : " + db_path);

        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_Users + " ("
                + KEY_UUID + " STRING NOT NULL,"
                + KEY_START_TIME + " STRING NOT NULL,"
                + KEY_END_TIME + " STRING NOT NULL);";
        Log.d(BeaconHandler.TAG, "Create Query : " + CREATE_TABLE);

        String CREATE_DOWNLOAD_TABLE = "CREATE TABLE IF NOT EXISTS " + INFECTED + " ("
                + KEY_uuid + " STRING NOT NULL,"
                + KEY_start_time + " STRING NOT NULL,"
                + KEY_end_time + " STRING NOT NULL);";

        try {
            db.execSQL(CREATE_TABLE);

            db.execSQL(CREATE_DOWNLOAD_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        createTable();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Users);
        // Create tables again
        onCreate(db);
    }
    //Get User Details. Uploading data on Server// check if we can convert into object like the query output
    //This method should be called only when we there is a periodic call to download the infected data and check aganist the local array list.
    public ArrayList<HashMap<String, String>> getUsers() {
        File db_path = myContext.getDatabasePath(DB_NAME + ".db");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(db_path, PASSWORD, null);
        //group by UUID's for a particular Start and End time
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT UUID, Start_Time, End_Time FROM " + TABLE_Users + "Group by" + KEY_UUID;
        Cursor cursor = db.query(query);
        // Cursor cursor = db.rawQuery(query, null);
        //one UUID can have multiple timestamps.
        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("UUID", cursor.getString(cursor.getColumnIndex(KEY_UUID)));
            user.put("Start_Time", cursor.getString(cursor.getColumnIndex(KEY_START_TIME)));
            user.put("End_Time", cursor.getString(cursor.getColumnIndex(KEY_END_TIME)));
            userList.add(user);
        }
        for (int i = 0; i < userList.size(); i++) {
            System.out.println(userList.get(i));
        }
        db.close();
        return userList;
    }
    public void insert(String UUID, String Start_Time, String End_Time) {
        //Get the Data Repository in write mode

        File db_path = myContext.getDatabasePath(DB_NAME + ".db");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(db_path, PASSWORD, null);
        Log.d(TAG, "Database path insert: " + db_path);
//        // Insert the new row, returning the primary key value of the new row
        try {
            String sql = "INSERT INTO " + TABLE_Users + " VALUES('" + UUID + "','" + Start_Time + "','" + End_Time + "');";
            //String sql = "INSERT INTO " + INFECTED + " VALUES('" + UUID + "','" + Start_Time + "','" + End_Time + "');";
            db.execSQL(sql);
            Log.d(TAG, "beacon data inserted");
            //db.execSQL(sql1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /* After Generating the notifications truncate the Infected table */
    /*
    public void truncate()
    {
        File db_path = myContext.getDatabasePath(DB_NAME + ".db");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(db_path, PASSWORD, null);
        Log.d(BeaconHandler.TAG, "Deleting data from the INFECTED table: " + db);
        db.execSQL("TRUNCATE TABLE "+ INFECTED);
    }*/
    public List<VenueRecordDto> getAll() {

        String query = "SELECT UUID, Start_Time, End_Time FROM " + TABLE_Users;
        /*upload only last 14 days data*/
        //String query ="SELECT  UUID,Start_Time,End_Time FROM InfectedUserData WHERE Start_Time > (SELECT DATETIME('now', '-14 day'))";
        List<VenueRecordDto> venueRecordDtos = new ArrayList<>();
        File db_path = myContext.getDatabasePath(DB_NAME + ".db");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(db_path, PASSWORD, null);
        Cursor cursor = db.query(query);
        while (cursor.moveToNext()) {

            String UUID = cursor.getString(cursor.getColumnIndex(KEY_UUID));
            String startTime = cursor.getString(cursor.getColumnIndex(KEY_START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndex(KEY_END_TIME));

            venueRecordDtos.add(new VenueRecordDto(UUID, startTime, endTime));
        }
        return venueRecordDtos;
    }

    public void insert_infect(String UUID, String Start_Time, String End_Time) {
        File db_path = myContext.getDatabasePath(DB_NAME + ".db");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(db_path, PASSWORD, null);

        // Insert the new row, returning the primary key value of the new row
        try {
            String sql = "INSERT INTO " + INFECTED + " VALUES('" + UUID + "','" + Start_Time + "','" + End_Time + "');";
            db.execSQL(sql);
            Log.d(TAG, "Data inserted: " + sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();

    }
    public int checkHighAlert() {
        SQLiteDatabase.loadLibs(myContext);
        File db_path = myContext.getDatabasePath(DB_NAME + ".db");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(db_path, PASSWORD, null);
        Log.d(TAG, "Database path insert: " + db_path);
        String MY_QUERY1 = "SELECT UserInformation.UUID,UserInformation.Start_Time from UserInformation JOIN InfectedUserData\n" +
                "ON UserInformation.UUID= InfectedUserData.UUID\n" +
                "AND \n" +
                "(UserInformation.Start_Time BETWEEN InfectedUserData.Start_Time and InfectedUserData.End_Time\n" +
                "or  UserInformation.End_Time BETWEEN InfectedUserData.Start_Time and InfectedUserData.End_Time\n" +
                "or  InfectedUserData.Start_Time BETWEEN UserInformation.Start_Time and UserInformation.End_Time\n" +
                "or  InfectedUserData.End_Time BETWEEN UserInformation.Start_Time and UserInformation.End_Time)";
        //High alert notification
        int count = 0;
        try {
            // db.rawQuery(MY_QUERY, new String[]{KEY_UUID});
            Cursor cursor;
            cursor = db.rawQuery(MY_QUERY1, null);
            if(cursor.getCount()!=0)
            {
                count = cursor.getCount();
            }
            else
            {

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        System.out.println("High Alert result found" + count);
        return count;
    }
    public int checkExposure() {
        //String res="";
        SQLiteDatabase.loadLibs(myContext);
        File db_path = myContext.getDatabasePath(DB_NAME + ".db");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(db_path, PASSWORD, null);
        Log.d(TAG, "Database path insert: " + db_path);
        String MY_QUERY ="SELECT UserInformation.UUID,UserInformation.Start_Time from UserInformation JOIN InfectedUserData\n" +
                "ON UserInformation.UUID= InfectedUserData.UUID";
        int count = 0;
        try {
            // db.rawQuery(MY_QUERY, new String[]{KEY_UUID});
            Cursor cursor;
            cursor = db.rawQuery(MY_QUERY, null);
            if(cursor.getCount()!=0)
            {
                count = cursor.getCount();
            }
            else
            {

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        System.out.println("Exposure result found"+count);
        return count;
    }
    }

