package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Feechan on 11/17/2016.
 */

public class Shoporderreport {
    public static final String TAG_SHOPORDERREPORT = "Shoporderreport";
    public static final String TAG_YEAR = "year";
    public static final String TAG_MONTH = "month";
    public static final String TAG_JUMLAHORDER = "jumlahorder";

    private int year;
    private int month;
    private int jumlahorder;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getJumlahorder() {
        return jumlahorder;
    }

    public void setJumlahorder(int jumlahorder) {
        this.jumlahorder = jumlahorder;
    }

    public static Shoporderreport GetShoporderreportFromJson(JSONObject jsonObject)
    {
        try {
            Shoporderreport shoporderreport = new Shoporderreport();

            shoporderreport.setYear(jsonObject.getInt(Shoporderreport.TAG_YEAR));
            shoporderreport.setMonth(jsonObject.getInt(Shoporderreport.TAG_MONTH));
            shoporderreport.setJumlahorder(jsonObject.getInt(Shoporderreport.TAG_JUMLAHORDER));
            return shoporderreport;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
