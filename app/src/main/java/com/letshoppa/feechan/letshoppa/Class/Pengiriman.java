package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Feechan on 11/13/2016.
 */

public class Pengiriman {
    public static final String TAG_PENGIRIMAN = "Pengiriman";
    public static final String TAG_PENGIRIMANID = "pengirimanid";
    public static final String TAG_TANGGALPENGIRIMAN = "tanggalpengiriman";
    public static final String TAG_KETERANGAN = "keterangan";
    public static final String TAG_JASAEKSPEDISI = "jasaekspedisi";
    public static final String TAG_NORESI = "noresi";
    public static final String TAG_BIAYAKIRIM = "biayakirim";
    public static final String TAG_ORDERID = "orderid";

    private int pengirimanid;
    private Timestamp tanggalpengiriman;
    private String keterangan;
    private String jasaekspedisi;
    private String noresi;
    private double biayakirim;
    private int orderid;

    public int getPengirimanid() {
        return pengirimanid;
    }

    public void setPengirimanid(int pengirimanid) {
        this.pengirimanid = pengirimanid;
    }

    public Timestamp getTanggalpengiriman() {
        return tanggalpengiriman;
    }

    public void setTanggalpengiriman(String tanggalpengiriman) {
        Date date;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tanggalpengiriman);
        }
        catch(ParseException e)
        {
            date = new Date();
        }
        this.tanggalpengiriman = new Timestamp(date.getTime());
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getJasaekspedisi() {
        return jasaekspedisi;
    }

    public void setJasaekspedisi(String jasaekspedisi) {
        this.jasaekspedisi = jasaekspedisi;
    }

    public String getNoresi() {
        return noresi;
    }

    public void setNoresi(String noresi) {
        this.noresi = noresi;
    }

    public double getBiayakirim() {
        return biayakirim;
    }

    public void setBiayakirim(double biayakirim) {
        this.biayakirim = biayakirim;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public static Pengiriman GetPengirimanFromJson(JSONObject jsonObject)
    {
        try
        {
            Pengiriman pengiriman = new Pengiriman();

            pengiriman.setPengirimanid(jsonObject.getInt(Pengiriman.TAG_PENGIRIMANID));
            pengiriman.setTanggalpengiriman(jsonObject.getString(Pengiriman.TAG_TANGGALPENGIRIMAN));
            pengiriman.setKeterangan(jsonObject.getString(Pengiriman.TAG_KETERANGAN));
            pengiriman.setJasaekspedisi(jsonObject.getString(Pengiriman.TAG_JASAEKSPEDISI));
            pengiriman.setNoresi(jsonObject.getString(Pengiriman.TAG_NORESI));
            pengiriman.setBiayakirim(jsonObject.getDouble(Pengiriman.TAG_BIAYAKIRIM));
            pengiriman.setOrderid(jsonObject.getInt(Pengiriman.TAG_ORDERID));
            return pengiriman;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
