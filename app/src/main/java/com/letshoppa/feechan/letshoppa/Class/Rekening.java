package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Feechan on 11/21/2016.
 */

public class Rekening implements Serializable {
    public static final String TAG_REKENING = "Rekening";
    public static final String TAG_REKENINGID = "rekeningid";
    public static final String TAG_NOMORREKENING = "nomorrekening";
    public static final String TAG_NAMABANK = "namabank";
    public static final String TAG_TOKOID = "tokoid";

    private int rekeningid;
    private String nomorrekening;
    private String namabank;
    private int tokoid;

    public int getRekeningid() {
        return rekeningid;
    }

    public void setRekeningid(int rekeningid) {
        this.rekeningid = rekeningid;
    }

    public String getNomorrekening() {
        return nomorrekening;
    }

    public void setNomorrekening(String nomorrekening) {
        this.nomorrekening = nomorrekening;
    }

    public String getNamabank() {
        return namabank;
    }

    public void setNamabank(String namabank) {
        this.namabank = namabank;
    }

    public int getTokoid() {
        return tokoid;
    }

    public void setTokoid(int tokoid) {
        this.tokoid = tokoid;
    }

    public static Rekening GetRekeningFromJson(JSONObject jsonObject)
    {
        try {
            Rekening rekening = new Rekening();

            rekening.setRekeningid(jsonObject.getInt(Rekening.TAG_REKENINGID));
            rekening.setNomorrekening(jsonObject.getString(Rekening.TAG_NOMORREKENING));
            rekening.setNamabank(jsonObject.getString(Rekening.TAG_NAMABANK));
            rekening.setTokoid(jsonObject.getInt(Rekening.TAG_TOKOID));

            return rekening;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
