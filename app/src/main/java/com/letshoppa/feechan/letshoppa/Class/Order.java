package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Feechan on 10/18/2016.
 */

public class Order {
    public static final String TAG_ORDER = "Order";
    public static final String TAG_ORDERID = "orderid";
    public static final String TAG_TANGGALORDER = "tanggalorder";
    public static final String TAG_STATUSORDER = "statusorder";
    public static final String TAG_ACCOUNTID = "accountid";
    public static final String TAG_NAMAPRODUK = "namaproduk";
    public static final String TAG_DESKRIPSIPRODUK = "deskripsiproduk";
    public static final String TAG_HARGAPRODUk = "hargaproduk";
    public static final String TAG_JUMLAHPRODUK = "jumlahproduk";
    public static final String TAG_PRODUKID = "produkid";
    public static final String TAG_GAMBARPRODUK = "gambarproduk";

    private int orderid;
    private Timestamp tanggalorder;
    private int statusorder;
    private int accountid;
    private String namaproduk;
    private String deskripsiproduk;
    private double hargaproduk;
    private int jumlahproduk;
    private int produkid;



    private String gambarproduk;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public Timestamp getTanggalorder() {
        return tanggalorder;
    }

    public void setTanggalorder(String tanggalorder) {
        Date date;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tanggalorder);
        }
        catch (ParseException e)
        {
            date = new Date();
        }
        this.tanggalorder = new Timestamp(date.getTime());
    }

    public int getStatusorder() {
        return statusorder;
    }

    public void setStatusorder(int statusorder) {
        this.statusorder = statusorder;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public String getNamaproduk() {
        return namaproduk;
    }

    public void setNamaproduk(String namaproduk) {
        this.namaproduk = namaproduk;
    }

    public String getDeskripsiproduk() {
        return deskripsiproduk;
    }

    public void setDeskripsiproduk(String deskripsiproduk) {
        this.deskripsiproduk = deskripsiproduk;
    }

    public double getHargaproduk() {
        return hargaproduk;
    }

    public void setHargaproduk(double hargaproduk) {
        this.hargaproduk = hargaproduk;
    }

    public int getJumlahproduk() {
        return jumlahproduk;
    }

    public void setJumlahproduk(int jumlahproduk) {
        this.jumlahproduk = jumlahproduk;
    }

    public int getProdukid() {
        return produkid;
    }

    public void setProdukid(int produkid) {
        this.produkid = produkid;
    }

    public String getGambarproduk() {
        return gambarproduk;
    }

    public void setGambarproduk(String gambarproduk) {
        this.gambarproduk = gambarproduk;
    }

    public static Order GetOrderFromJson(JSONObject jsonObject)
    {
        try {
            Order order = new Order();
            order.setOrderid(jsonObject.getInt(Order.TAG_ORDERID));
            order.setTanggalorder(jsonObject.getString(Order.TAG_TANGGALORDER));
            order.setStatusorder(jsonObject.getInt(Order.TAG_STATUSORDER));
            order.setAccountid(jsonObject.getInt(Order.TAG_ACCOUNTID));
            order.setNamaproduk(jsonObject.getString(Order.TAG_NAMAPRODUK));
            order.setDeskripsiproduk(jsonObject.getString(Order.TAG_DESKRIPSIPRODUK));
            order.setHargaproduk(jsonObject.getDouble(Order.TAG_HARGAPRODUk));
            order.setJumlahproduk(jsonObject.getInt(Order.TAG_JUMLAHPRODUK));
            order.setProdukid(jsonObject.getInt(Order.TAG_PRODUKID));
            order.setGambarproduk(jsonObject.getString(Order.TAG_GAMBARPRODUK));
            return order;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
