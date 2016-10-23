package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Feechan on 9/12/2016.
 */
@SuppressWarnings("serial")
public class Toko implements Serializable{
    public static final String TAG_TOKO = "Toko";
    public static final String TAG_TOKOID = "tokoid";
    public static final String TAG_NAMATOKO = "namatoko";
    public static final String TAG_DESKRIPSITOKO = "deskripsitoko";
    public static final String TAG_LOKASITOKO = "lokasitoko";
    public static final String TAG_LATITUDE = "latitude";
    public static final String TAG_LONGITUDE = "longitude";
    public static final String TAG_GAMBARTOKO = "gambartoko";
    public static final String TAG_JENISTOKOID = "jenistokoid";
    public static final String TAG_ACCOUNTID = "accountid";
    public static final String TAG_STATUSTOKO = "statustoko";
    public static final String TAG_NAMAJENIS = "namajenis";

    public static final String TAG_KEYWORD = "keyword";

    private int tokoid;
    private String namatoko;
    private String deskripsitoko;
    private String lokasitoko;
    private double latitude;
    private double longitude;
    private String gambartoko;
    private int jenistokoid;
    private int accountid;
    private int statustoko;
    private String namajenis;

    public Toko()
    {
    }

    public Toko(String namatoko, String deskripsitoko) {
        this.namatoko = namatoko;
        this.deskripsitoko = deskripsitoko;
    }
    public int getTokoid() {
        return tokoid;
    }

    public void setTokoid(int tokoid) {
        this.tokoid = tokoid;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public String getDeskripsitoko() {
        return deskripsitoko;
    }

    public void setDeskripsitoko(String deskripsitoko) {
        this.deskripsitoko = deskripsitoko;
    }

    public String getLokasitoko() {
        return lokasitoko;
    }

    public void setLokasitoko(String lokasitoko) {
        this.lokasitoko = lokasitoko;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGambartoko() {
        return gambartoko;
    }

    public void setGambartoko(String gambartokoid) {
        this.gambartoko = gambartokoid;
    }

    public int getJenistokoid() {
        return jenistokoid;
    }

    public void setJenistokoid(int jenistokoid) {
        this.jenistokoid = jenistokoid;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public int getStatustoko() {
        return statustoko;
    }

    public void setStatustoko(int statustoko) {
        this.statustoko = statustoko;
    }

    public String getNamajenis() { return namajenis; }

    public void setNamajenis(String namajenis) { this.namajenis = namajenis; }

    public static Toko GetTokoFromJson(JSONObject jsonObject)
    {
        try {
            Toko toko = new Toko();

            toko.setTokoid(jsonObject.getInt(Toko.TAG_TOKOID));
            toko.setNamatoko(jsonObject.getString(Toko.TAG_NAMATOKO));
            toko.setDeskripsitoko(jsonObject.getString(Toko.TAG_DESKRIPSITOKO));
            toko.setLokasitoko(jsonObject.getString(Toko.TAG_LOKASITOKO));
            toko.setLatitude(jsonObject.getDouble(Toko.TAG_LATITUDE));
            toko.setLongitude(jsonObject.getDouble(Toko.TAG_LONGITUDE));
            toko.setGambartoko(jsonObject.getString(Toko.TAG_GAMBARTOKO));
            toko.setJenistokoid(jsonObject.getInt(Toko.TAG_JENISTOKOID));
            toko.setAccountid(jsonObject.getInt(Toko.TAG_ACCOUNTID));
            toko.setStatustoko(jsonObject.getInt(Toko.TAG_STATUSTOKO));
            toko.setNamajenis(jsonObject.getString(Toko.TAG_NAMAJENIS));
            return toko;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }

}
