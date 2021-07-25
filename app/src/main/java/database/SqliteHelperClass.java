package database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Date;

public class SqliteHelperClass extends SQLiteOpenHelper {
    private static final String Dbname="MyDb";
    private static final String StatusTable="StatusTable";
    private static final int version=7;
    public static final String  studentnamekey="name";
    public static final String  snokey="sno";
    public static final String  datekey="date";

    public static final String  statuskey="status";
    private static final String  selectquery="select * from "+StatusTable;

    public SqliteHelperClass(@Nullable Context context) {
        super(context, Dbname, null, version);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="create table "+StatusTable+" (sno Integer, date DATE not null, name TEXT not null, status TEXT not null)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query="drop table if exists "+StatusTable;
        db.execSQL(query);
        onCreate(db);
    }

   public long insertdata(long sno,String date,String name,String status){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(snokey,sno);
        values.put(datekey,date);
        values.put(studentnamekey,name);
        values.put(statuskey,status);
        return database.insert(StatusTable,null,values);
    }

    public void updateStatus(long sno, String date,String status){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(statuskey,status);
        String whereClause = datekey+" = '"+date+"' AND " +snokey+"="+sno;
        database.update(StatusTable, values, whereClause, null);
    }

    public Cursor getClassTable(){

        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery(selectquery,null);
        if (cursor.moveToNext()){
            cursor.getString(cursor.getColumnIndex(statuskey));
        }
        return cursor;
    }
    public String getStatus(long sno, String date){
        String status=null;
        SQLiteDatabase database=this.getReadableDatabase();
        String whereClause = datekey+"='"+date+"' AND " +snokey+"="+sno;
        @SuppressLint("Recycle") Cursor cursor=database.query(StatusTable,null,whereClause,null,null,null,null);
        if (cursor.moveToNext()){
            status=cursor.getString(cursor.getColumnIndex(statuskey));
        }
        return status;
    }
}
