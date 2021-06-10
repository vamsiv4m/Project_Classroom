package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.animation.content.Content;

public class SqliteHelperClass extends SQLiteOpenHelper {
    private static final String Dbname="MyDb";
    private static final String StatusTable="StatusTable";
    private static final int version=2;
    public static final String  studentnamekey="name";
    public static final String  snokey="sno";
    public static final String  statuskey="status";
    private static final String  selectquery="select * from "+StatusTable;

    public SqliteHelperClass(@Nullable Context context) {
        super(context, Dbname, null, version);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="create table "+StatusTable+" (sno TEXT,name TEXT, status TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query="drop table if exists "+StatusTable;
        db.execSQL(query);
        onCreate(db);
    }

    public void insertdata(String sno,String name,String status){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(snokey,sno);
        values.put(studentnamekey,name);
        values.put(statuskey,status);
        database.insert(StatusTable,null,values);
    }
    public Cursor getClassTable(){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery(selectquery,null);
        return cursor;
    }
}
