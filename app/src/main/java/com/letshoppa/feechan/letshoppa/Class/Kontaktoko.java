package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Feechan on 11/22/2016.
 */

public class Kontaktoko implements Serializable
{
    public static final String TAG_KONTAKTOKO = "Kontaktoko";
    public static final String TAG_KONTAKTOKOID = "kontaktokoid";
    public static final String TAG_JENISKONTAK = "jeniskontak";
    public static final String TAG_ISIKONTAK = "isikontak";
    public static final String TAG_TOKOID = "tokoid";

    private int kontaktokoid;
    private String jeniskontak;
    private String isikontak;
    private int tokoid;

    public int getKontaktokoid() {
        return kontaktokoid;
    }

    public void setKontaktokoid(int kontaktokoid) {
        this.kontaktokoid = kontaktokoid;
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

    public int getTokoid() {
        return tokoid;
    }

    public void setTokoid(int tokoid) {
        this.tokoid = tokoid;
    }

    public static Kontaktoko GetKontaktokoFromJson(JSONObject jsonObject)
    {
        try {
            Kontaktoko kontaktoko = new Kontaktoko();
            kontaktoko.setKontaktokoid(jsonObject.getInt(Kontaktoko.TAG_KONTAKTOKOID));
            kontaktoko.setJeniskontak(jsonObject.getString(Kontaktoko.TAG_JENISKONTAK));
            kontaktoko.setIsikontak(jsonObject.getString(Kontaktoko.TAG_ISIKONTAK));
            kontaktoko.setTokoid(jsonObject.getInt(Kontaktoko.TAG_TOKOID));
            return kontaktoko;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}