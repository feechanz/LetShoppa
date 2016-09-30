package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Feechan on 9/30/2016.
 */

public class Produk {
    public static final String TAG_PRODUK = "Produk";
    public static final String TAG_PRODUKID = "produkid";
    public static final String TAG_NAMAPRODUK = "namaproduk";
    public static final String TAG_HARGAPRODUK = "hargaproduk";
    public static final String TAG_DESKRIPSIPRODUK = "deskripsiproduk";
    public static final String TAG_STATUSPRODUK = "statusproduk";
    public static final String TAG_TANGGALPRODUK = "tanggalproduk";
    public static final String TAG_GAMBARPRODUK = "gambarproduk";
    public static final String TAG_TOKOID = "tokoid";
    public static final String TAG_KATEGORIPRODUKID = "kategoriprodukid";
    public static final String TAG_NAMAKATEGORI = "namakategori";

    private int produkid;
    private String namaproduk;
    private double hargaproduk;
    private String deskripsiproduk;
    private int statusproduk;
    private Timestamp tanggalproduk;
    private String gambarproduk;
    private int tokoid;
    private int kategoriprodukid;
    private String namakategori;

    public Produk()
    {

    }

    public int getProdukid() {
        return produkid;
    }

    public void setProdukid(int produkid) {
        this.produkid = produkid;
    }

    public String getNamaproduk() {
        return namaproduk;
    }

    public void setNamaproduk(String namaproduk) {
        this.namaproduk = namaproduk;
    }

    public double getHargaproduk() {
        return hargaproduk;
    }

    public void setHargaproduk(double hargaproduk) {
        this.hargaproduk = hargaproduk;
    }

    public String getDeskripsiproduk() {
        return deskripsiproduk;
    }

    public void setDeskripsiproduk(String deskripsiproduk) {
        this.deskripsiproduk = deskripsiproduk;
    }

    public int getStatusproduk() {
        return statusproduk;
    }

    public void setStatusproduk(int statusproduk) {
        this.statusproduk = statusproduk;
    }

    public Timestamp getTanggalproduk() {
        return tanggalproduk;
    }

    public void setTanggalproduk(String tanggalproduk)
    {
        Date date;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggalproduk);
        }
        catch(ParseException e)
        {
            date = new Date();
        }
        this.tanggalproduk = new Timestamp(date.getTime());
    }

    public String getGambarproduk() {
        return gambarproduk;
    }

    public void setGambarproduk(String gambarproduk) {
        this.gambarproduk = gambarproduk;
    }

    public int getTokoid() {
        return tokoid;
    }

    public void setTokoid(int tokoid) {
        this.tokoid = tokoid;
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

    public static Produk GetProdukFromJson(JSONObject jsonObject)
    {
        try {
            Produk produk = new Produk();

            produk.setProdukid(jsonObject.getInt(Produk.TAG_PRODUKID));
            produk.setNamaproduk(jsonObject.getString(Produk.TAG_NAMAPRODUK));
            produk.setHargaproduk(jsonObject.getDouble(Produk.TAG_HARGAPRODUK));
            produk.setDeskripsiproduk(jsonObject.getString(Produk.TAG_DESKRIPSIPRODUK));
            produk.setStatusproduk(jsonObject.getInt(Produk.TAG_STATUSPRODUK));
            produk.setTanggalproduk(jsonObject.getString(Produk.TAG_TANGGALPRODUK));
            produk.setGambarproduk(jsonObject.getString(Produk.TAG_GAMBARPRODUK));
            produk.setTokoid(jsonObject.getInt(Produk.TAG_TOKOID));
            produk.setKategoriprodukid(jsonObject.getInt(Produk.TAG_KATEGORIPRODUKID));
            produk.setNamakategori(jsonObject.getString(Produk.TAG_NAMAKATEGORI));

            return produk;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
