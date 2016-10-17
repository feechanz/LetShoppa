package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Feechan on 9/4/2016.
 */
public class Account implements Serializable {
    public static final String TAG_ACCOUNT = "account";
    public static final String TAG_ACCOUNTID = "accountid";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_BIRTHDATE = "birthdate";
    public static final String TAG_LINKGAMBARACCOUNT = "linkgambaraccount";
    public static final String TAG_PREMIUMACCOUNT = "premiumaccount";
    public static final String TAG_LEVELACCOUNT = "levelaccount";
    public static final String TAG_STATUSACCOUNT = "statusaccount";

    private String accountid;
    private String email;
    private String password;
    private String nama;
    private String gender;
    private Timestamp birthdate;
    private String linkgambaraccount;
    private Timestamp premiumaccount;
    private int levelaccount;
    private int statusaccount;

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Timestamp getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        Date date;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(birthdate);
        }
        catch(ParseException e)
        {
            date = new Date();
        }
        this.birthdate = new Timestamp(date.getTime());
    }

    public String getLinkgambaraccount() {
        return linkgambaraccount;
    }

    public void setLinkgambaraccount(String linkgambaraccount) {
        this.linkgambaraccount = linkgambaraccount;
    }

    public Timestamp getPremiumaccount() {
        return premiumaccount;
    }

    public void setPremiumaccount(String premiumaccount)  {
        Date date;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(premiumaccount);
        }
        catch (ParseException e)
        {
            date = new Date();
        }
        this.premiumaccount = new Timestamp(date.getTime());
    }

    public int getLevelaccount() {
        return levelaccount;
    }

    public void setLevelaccount(int levelaccount) {
        this.levelaccount = levelaccount;
    }

    public int getStatusaccount() {
        return statusaccount;
    }

    public void setStatusaccount(int statusaccount) {
        this.statusaccount = statusaccount;
    }

    public static Account GetAccountFromJson(JSONObject jsonObject)
    {
        try {
            Account account = new Account();
            account.setAccountid(jsonObject.getString(Account.TAG_ACCOUNTID));
            account.setEmail(jsonObject.getString(Account.TAG_EMAIL));
            account.setPassword(jsonObject.getString(Account.TAG_PASSWORD));
            account.setNama(jsonObject.getString(Account.TAG_NAMA));
            account.setGender(jsonObject.getString(Account.TAG_GENDER));
            account.setBirthdate(jsonObject.getString(Account.TAG_BIRTHDATE));
            account.setLinkgambaraccount(jsonObject.getString(Account.TAG_LINKGAMBARACCOUNT));
            account.setPremiumaccount(jsonObject.getString(Account.TAG_PREMIUMACCOUNT));
            account.setLevelaccount(jsonObject.getInt(Account.TAG_LEVELACCOUNT));
            account.setStatusaccount(jsonObject.getInt(Account.TAG_STATUSACCOUNT));
            return account;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
