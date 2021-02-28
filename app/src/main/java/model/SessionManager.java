package model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;

public class SessionManager {
    public static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int mode=0;
    String Filename="login";
    public static final String Username="username";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(Filename,mode);
        editor=sharedPreferences.edit();
    }

    public HashMap<String ,String > getUserSession(){
        HashMap<String,String> usersession=new HashMap<String, String>();
        usersession.put(Username,sharedPreferences.getString(Username,null));
        return usersession;
    }
}

