package com.example.fujitsu.solarview;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.database.Query;

import java.util.Date;
import java.util.prefs.PreferenceChangeListener;

public class InternalDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="parameters";
    public static final int DATABASE_VERSION=1;

    private String email;
    private String password;
    private double lastDate;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLastDate(String email) {

        String reqLast="SELECT lastdate FROM parameter WHERE email = '"+email+"'";

        Cursor cursor=this.getReadableDatabase().rawQuery(reqLast,null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            lastDate = cursor.getInt(0);
        cursor.close();
        }
        else lastDate=0;
        return lastDate;
    }

    public void setLastDate(double lastDate,String email) {
        String Request="UPDATE parameter SET lastdate='"+lastDate+"' WHERE email='"+email+"'";
        this.getWritableDatabase().execSQL(Request);
    }

    public InternalDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    public void insertUser(String email,String password){
        String sql="INSERT INTO parameter(email,password,lastDate) ('"+email+"','"+password+"','"+new Date().getTime()+"')";
        this.getWritableDatabase().execSQL(sql);
        setEmail(email);
        setPassword(password);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE parameters("
                +"ID integer primary key autoincrement,"
                +"email text not null,"
                +"password text not null,"
                +"lastdate double not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
