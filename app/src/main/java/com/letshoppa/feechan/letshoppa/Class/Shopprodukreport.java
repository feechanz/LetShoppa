package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Feechan on 11/17/2016.
 */

public class Shopprodukreport {
    public static final String TAG_SHOPPRODUKREPORT = "Shopprodukreport";
    public static final String TAG_NAMAPRODUK = "namaproduk";
    public static final String TAG_JUMLAHORDER = "jumlahorder";

    private String namaproduk;
    private int jumlahorder;

    public String getNamaproduk() {
        return namaproduk;
    }

    public void setNamaproduk(String namaproduk) {
        this.namaproduk = namaproduk;
    }

    public int getJumlahorder() {
        return jumlahorder;
    }

    public void setJumlahorder(int jumlahorder) {
        this.jumlahorder = jumlahorder;
    }

    public static Shopprodukreport GetShopprodukreportFromJson(JSONObject jsonObject)
    {
        try {
            Shopprodukreport shopprodukreport = new Shopprodukreport();

            shopprodukreport.setNamaproduk(jsonObject.getString(Shopprodukreport.TAG_NAMAPRODUK));
            shopprodukreport.setJumlahorder(jsonObject.getInt(Shopprodukreport.TAG_JUMLAHORDER));
            return shopprodukreport;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
