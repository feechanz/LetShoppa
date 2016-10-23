package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Feechan on 10/23/2016.
 */

public class Pesan implements Serializable{
    public static final String TAG_PESAN = "Pesan";
    public static final String TAG_PESANID = "pesanid";
    public static final String TAG_ISIPESAN = "isipesan";
    public static final String TAG_TANGGALPESAN = "tanggalpesan";
    public static final String TAG_STATUSPESAN = "statuspesan";
    public static final String TAG_PENGIRIMACCOUNTID = "pengirimaccountid";
    public static final String TAG_PENERIMAACCOUNTID = "penerimaaccountid";
    public static final String TAG_NAMAPENGIRIM = "namapengirim";
    public static final String TAG_NAMAPENERIMA = "namapenerima";

    private int pesanid;
    private String isipesan;
    private Timestamp tanggalpesan;
    private int statuspesan;
    private int pengirimaccountid;
    private int penerimaaccountid;
    private String namapengirim;
    private String namapenerima;

    public int getPesanid() {
        return pesanid;
    }

    public void setPesanid(int pesanid) {
        this.pesanid = pesanid;
    }

    public String getIsipesan() {
        return isipesan;
    }

    public void setIsipesan(String isipesan) {
        this.isipesan = isipesan;
    }

    public Timestamp getTanggalpesan() {
        return tanggalpesan;
    }

    public void setTanggalpesan(String tanggalpesan) {
        Date date;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(tanggalpesan);
        }
        catch(ParseException e)
        {
            date = new Date();
        }
        this.tanggalpesan = new Timestamp(date.getTime());
    }

    public int getStatuspesan() {
        return statuspesan;
    }

    public void setStatuspesan(int statuspesan) {
        this.statuspesan = statuspesan;
    }

    public int getPengirimaccountid() {
        return pengirimaccountid;
    }

    public void setPengirimaccountid(int pengirimaccountid) {
        this.pengirimaccountid = pengirimaccountid;
    }

    public int getPenerimaaccountid() {
        return penerimaaccountid;
    }

    public void setPenerimaaccountid(int penerimaaccountid) {
        this.penerimaaccountid = penerimaaccountid;
    }

    public String getNamapengirim() {
        return namapengirim;
    }

    public void setNamapengirim(String namapengirim) {
        this.namapengirim = namapengirim;
    }

    public String getNamapenerima() {
        return namapenerima;
    }

    public void setNamapenerima(String namapenerima) {
        this.namapenerima = namapenerima;
    }

    public static Pesan GetPesanFromJson(JSONObject jsonObject)
    {
        try {
            Pesan pesan = new Pesan();

            pesan.setPesanid(jsonObject.getInt(Pesan.TAG_PESANID));
            pesan.setIsipesan(jsonObject.getString(Pesan.TAG_ISIPESAN));
            pesan.setTanggalpesan(jsonObject.getString(Pesan.TAG_TANGGALPESAN));
            pesan.setStatuspesan(jsonObject.getInt(Pesan.TAG_STATUSPESAN));
            pesan.setPengirimaccountid(jsonObject.getInt(Pesan.TAG_PENGIRIMACCOUNTID));
            pesan.setPenerimaaccountid(jsonObject.getInt(Pesan.TAG_PENERIMAACCOUNTID));
            pesan.setNamapengirim(jsonObject.getString(Pesan.TAG_NAMAPENGIRIM));
            pesan.setNamapenerima(jsonObject.getString(Pesan.TAG_NAMAPENERIMA));
            return pesan;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
