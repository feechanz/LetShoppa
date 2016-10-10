package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Feechan on 10/10/2016.
 */

public class Kategoriproduk {
    public static final String TAG_KATEGORIPRODUK = "Kategoriproduk";
    public static final String TAG_KATEGORIPRODUKID = "kategoriprodukid";
    public static final String TAG_NAMAKATEGORI = "namakategori";
    public static final String TAG_JENISTOKOID = "jenistokoid";

    private int kategoriprodukid;
    private String namakategori;
    private int jenistokoid;

    public Kategoriproduk() {
    }

    public int getKategoriprodukid() {
        return kategoriprodukid;
    }

    public void setKategoriprodukid(int kategoriprodukid) {
        this.kategoriprodukid = kategoriprodukid;
    }

    public String getNamakategori() {
        return namakategori;
    }

    public void setNamakategori(String namakategori) {
        this.namakategori = namakategori;
    }

    public int getJenistokoid() {
        return jenistokoid;
    }

    public void setJenistokoid(int jenistokoid) {
        this.jenistokoid = jenistokoid;
    }

    public static Kategoriproduk GetKategoriprodukFromJson(JSONObject jsonObject) {
        Kategoriproduk kategoriproduk = new Kategoriproduk();
        try
        {
            kategoriproduk.setKategoriprodukid(jsonObject.getInt(TAG_KATEGORIPRODUKID));
            kategoriproduk.setJenistokoid(jsonObject.getInt(TAG_JENISTOKOID));
            kategoriproduk.setNamakategori(jsonObject.getString(TAG_NAMAKATEGORI));
            return kategoriproduk;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
