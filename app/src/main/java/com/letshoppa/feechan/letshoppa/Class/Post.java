package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Feechan on 10/22/2016.
 */

public class Post implements Serializable {

    public static final String TAG_POST = "Post";
    public static final String TAG_POSTID = "postid";
    public static final String TAG_TANGGALPOST = "tanggalpost";
    public static final String TAG_TULISAN = "tulisan";
    public static final String TAG_ACCOUNTID = "accountid";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_LINKGAMBARACCOUNT = "linkgambaraccount";
    public static final String TAG_STATUSPOST = "statuspost";

    private int postid;
    private Timestamp tanggalpost;
    private String tulisan;
    private int accountid;
    private String nama;
    private String linkgambaraccount;
    private int statuspost;

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public Timestamp getTanggalpost() {
        return tanggalpost;
    }

    public void setTanggalpost(String tanggalpost) {
        Date date;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(tanggalpost);
        }
        catch(ParseException e)
        {
            date = new Date();
        }
        this.tanggalpost = new Timestamp(date.getTime());
    }

    public String getTulisan() {
        return tulisan;
    }

    public void setTulisan(String tulisan) {
        this.tulisan = tulisan;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLinkgambaraccount() {
        return linkgambaraccount;
    }

    public void setLinkgambaraccount(String linkgambaraccount) {
        this.linkgambaraccount = linkgambaraccount;
    }

    public int getStatuspost() {
        return statuspost;
    }

    public void setStatuspost(int statuspost) {
        this.statuspost = statuspost;
    }

    public static Post GetPostFromJson(JSONObject jsonObject)
    {
        try {
            Post post = new Post();

            post.setPostid(jsonObject.getInt(Post.TAG_POSTID));
            post.setTanggalpost(jsonObject.getString(Post.TAG_TANGGALPOST));
            post.setTulisan(jsonObject.getString(Post.TAG_TULISAN));
            post.setAccountid(jsonObject.getInt(Post.TAG_ACCOUNTID));
            post.setNama(jsonObject.getString(Post.TAG_NAMA));
            post.setLinkgambaraccount(jsonObject.getString(Post.TAG_LINKGAMBARACCOUNT));
            post.setStatuspost(jsonObject.getInt(Post.TAG_STATUSPOST));

            return post;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
