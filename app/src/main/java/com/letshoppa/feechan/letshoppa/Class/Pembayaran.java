package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Feechan on 11/14/2016.
 */

public class Pembayaran implements Serializable {
    public static final String TAG_PEMBAYARAN = "Pembayaran";
    public static final String TAG_PEMBAYARANID = "pembayaranid";
    public static final String TAG_TANGGALPEMBAYARAN = "tanggalpembayaran";
    public static final String TAG_KETERANGAN = "keterangan";
    public static final String TAG_BUKTIPEMBAYARAN = "buktipembayaran";
    public static final String TAG_TOTALPEMBAYARAN = "totalpembayaran";
    public static final String TAG_ORDERID = "orderid";

    private int pembayaranid;
    private Timestamp tanggalpembayaran;
    private String keterangan;
    private String buktipembayaran;
    private double totalpembayaran;
    private int orderid;

    public int getPembayaranid() {
        return pembayaranid;
    }

    public void setPembayaranid(int pembayaranid) {
        this.pembayaranid = pembayaranid;
    }

    public Timestamp getTanggalpembayaran() {
        return tanggalpembayaran;
    }

    public void setTanggalpembayaran(String tanggalpembayaran) {
        Date date;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tanggalpembayaran);
        }
        catch(ParseException e)
        {
            date = new Date();
        }
        this.tanggalpembayaran = new Timestamp(date.getTime());
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getBuktipembayaran() {
        return buktipembayaran;
    }

    public void setBuktipembayaran(String buktipembayaran) {
        this.buktipembayaran = buktipembayaran;
    }

    public double getTotalpembayaran() {
        return totalpembayaran;
    }

    public void setTotalpembayaran(double totalpembayaran) {
        this.totalpembayaran = totalpembayaran;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public static Pembayaran GetPembayaranFromJson(JSONObject jsonObject)
    {
        try
        {
            Pembayaran pembayaran = new Pembayaran();
            pembayaran.setPembayaranid(jsonObject.getInt(Pembayaran.TAG_PEMBAYARANID));
            pembayaran.setTanggalpembayaran(jsonObject.getString(Pembayaran.TAG_TANGGALPEMBAYARAN));
            pembayaran.setKeterangan(jsonObject.getString(Pembayaran.TAG_KETERANGAN));
            pembayaran.setBuktipembayaran(jsonObject.getString(Pembayaran.TAG_BUKTIPEMBAYARAN));
            pembayaran.setTotalpembayaran(jsonObject.getDouble(Pembayaran.TAG_TOTALPEMBAYARAN));
            pembayaran.setOrderid(jsonObject.getInt(Pembayaran.TAG_ORDERID));
            return pembayaran;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
