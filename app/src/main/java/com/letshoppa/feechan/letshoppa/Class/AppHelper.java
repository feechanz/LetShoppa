package com.letshoppa.feechan.letshoppa.Class;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by Feechan on 6/19/2016.
 */
public class AppHelper {
    //public static boolean isLogin = false;
    public static Jenistoko tipeShopItem = null;
    public static Account currentAccount = null;
    public static final String ConnectionFailed = "Connection Failed";
    public static final String NoFile = "No File";
    public static String Message="";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    //public static final String genderMale = "Pria";
    //public static final String genderFemale = "Wanita";
    public static final String PREF_NAME = "LetShoppaPref";
    public static final String TAG_LATITUDE="latitude";
    public static final String TAG_LONGITUDE="longitude";

    public static ImageView profilePicture;

    public static SharedPreferences pref;
    public static int PRIVATE_MODE = 0;
    public static SharedPreferences.Editor editor;
    private static String KEY_LOGIN = "isLoggedIn";


    public static void createLoginSession()
    {
        editor.putBoolean(KEY_LOGIN,true);
        editor.putString(Account.TAG_ACCOUNTID, currentAccount.getAccountid());
        editor.putString(Account.TAG_EMAIL,currentAccount.getEmail());
        editor.putString(Account.TAG_PASSWORD,currentAccount.getPassword());
        editor.putString(Account.TAG_NAMA,currentAccount.getNama());
        editor.putString(Account.TAG_GENDER,currentAccount.getGender());
        editor.putString(Account.TAG_BIRTHDATE,currentAccount.getBirthdate().toString());
        editor.putString(Account.TAG_LINKGAMBARACCOUNT,currentAccount.getLinkgambaraccount());
        editor.putString(Account.TAG_PREMIUMACCOUNT,currentAccount.getPremiumaccount().toString());
        editor.putInt(Account.TAG_LEVELACCOUNT,currentAccount.getLevelaccount());
        editor.putInt(Account.TAG_STATUSACCOUNT,currentAccount.getStatusaccount());

        editor.commit();
    }
    public static void getAccountFromSession()
    {
        if(isLoggedIn()) {
            currentAccount = new Account();
            currentAccount.setAccountid(pref.getString(Account.TAG_ACCOUNTID, ""));

            currentAccount.setEmail(pref.getString(Account.TAG_EMAIL, ""));
            currentAccount.setPassword(pref.getString(Account.TAG_PASSWORD, ""));
            currentAccount.setNama(pref.getString(Account.TAG_NAMA, ""));
            currentAccount.setGender(pref.getString(Account.TAG_GENDER, ""));
            currentAccount.setBirthdate(pref.getString(Account.TAG_BIRTHDATE, ""));
            currentAccount.setLinkgambaraccount(pref.getString(Account.TAG_LINKGAMBARACCOUNT, ""));
            currentAccount.setPremiumaccount(pref.getString(Account.TAG_PREMIUMACCOUNT, ""));
            currentAccount.setLevelaccount(pref.getInt(Account.TAG_LEVELACCOUNT, 0));
            currentAccount.setStatusaccount(pref.getInt(Account.TAG_STATUSACCOUNT, 0));
        }
    }

    public static boolean isLoggedIn()
    {
        return pref.getBoolean(KEY_LOGIN,false);
    }
    public static void removeLoginSession()
    {
        currentAccount = null;
        editor.clear();
        editor.commit();
    }
    /*public static JSONObject GetJsonObject(String url, String method, List<NameValuePair> params)
    {

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url, method, params);
            return json;

    }*/
    public static JSONObject GetJsonObject(String url, String method, List<NameValuePair> params)
    {
        return GetJsonObject(url,method,params,null);
    }
    public static JSONObject GetJsonObject(String url, String method, List<NameValuePair> params, File file)
    {
        if(file == null) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url, method, params);
            return json;
        }
        else
        {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url, method, params,file);
            return json;
        }
    }


    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}
