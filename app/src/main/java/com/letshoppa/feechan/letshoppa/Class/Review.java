package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Feechan on 10/22/2016.
 */

public class Review {
    public static final String TAG_REVIEW = "Review";
    public static final String TAG_REVIEWID = "reviewid";
    public static final String TAG_ISIREVIEW = "isireview";
    public static final String TAG_STATUSREVIEW = "statusreview";
    public static final String TAG_TOKOID = "tokoid";
    public static final String TAG_ACCOUNTID = "accountid";
    public static final String TAG_TANGGALREVIEW = "tanggalreview";
    public static final String TAG_POINTREVIEW = "pointreview";
    public static final String TAG_NAMA = "nama";

    public static final String TAG_SCORE = "score";
    public static final String TAG_JUMLAH = "jumlah";

    private int reviewid;
    private String isireview;
    private int statusreview;
    private int tokoid;
    private int accountid;
    private Timestamp tanggalreview;
    private int pointreview;
    private String nama;

    public int getReviewid() {
        return reviewid;
    }

    public void setReviewid(int reviewid) {
        this.reviewid = reviewid;
    }

    public String getIsireview() {
        return isireview;
    }

    public void setIsireview(String isireview) {
        this.isireview = isireview;
    }

    public int getStatusreview() {
        return statusreview;
    }

    public void setStatusreview(int statusreview) {
        this.statusreview = statusreview;
    }

    public int getTokoid() {
        return tokoid;
    }

    public void setTokoid(int tokoid) {
        this.tokoid = tokoid;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public Timestamp getTanggalreview() {
        return tanggalreview;
    }

    public void setTanggalreview(String tanggalreview) {
        Date date;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(tanggalreview);
        }
        catch(ParseException e)
        {
            date = new Date();
        }
        this.tanggalreview = new Timestamp(date.getTime());
    }

    public int getPointreview() {
        return pointreview;
    }

    public void setPointreview(int pointreview) {
        this.pointreview = pointreview;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public static Review GetReviewFromJson(JSONObject jsonObject)
    {
        try {
            Review review = new Review();

            review.setReviewid(jsonObject.getInt(Review.TAG_REVIEWID));
            review.setIsireview(jsonObject.getString(Review.TAG_ISIREVIEW));
            review.setIsireview(jsonObject.getString(Review.TAG_ISIREVIEW));
            review.setStatusreview(jsonObject.getInt(Review.TAG_STATUSREVIEW));
            review.setTokoid(jsonObject.getInt(Review.TAG_TOKOID));
            review.setAccountid(jsonObject.getInt(Review.TAG_ACCOUNTID));
            review.setTanggalreview(jsonObject.getString(Review.TAG_TANGGALREVIEW));
            review.setPointreview(jsonObject.getInt(Review.TAG_POINTREVIEW));
            review.setNama(jsonObject.getString(Review.TAG_NAMA));
            return review;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
