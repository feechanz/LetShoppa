package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Feechan on 10/23/2016.
 */

public class Kontak implements Serializable
{
    public static final String TAG_KONTAK = "Kontak";
    public static final String TAG_KONTAKID = "kontakid";
    public static final String TAG_JENISKONTAK = "jeniskontak";
    public static final String TAG_ISIKONTAK = "isikontak";
    public static final String TAG_ACCOUNTID = "accountid";

    private int kontakid;
    private String jeniskontak;
    private String isikontak;
    private int accountid;

    public int getKontakid() {
        return kontakid;
    }

    public void setKontakid(int kontakid) {
        this.kontakid = kontakid;
    }

    public String getJeniskontak() {
        return jeniskontak;
    }

    public void setJeniskontak(String jeniskontak) {
        this.jeniskontak = jeniskontak;
    }

    public String getIsikontak() {
        return isikontak;
    }

    public void setIsikontak(String isikontak) {
        this.isikontak = isikontak;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public static Kontak GetKontakFromJson(JSONObject jsonObject)
    {
        try {
            Kontak kontak = new Kontak();
            kontak.setKontakid(jsonObject.getInt(Kontak.TAG_KONTAKID));
            kontak.setJeniskontak(jsonObject.getString(Kontak.TAG_JENISKONTAK));
            kontak.setIsikontak(jsonObject.getString(Kontak.TAG_ISIKONTAK));
            kontak.setAccountid(jsonObject.getInt(Kontak.TAG_ACCOUNTID));
            return kontak;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
