package com.letshoppa.feechan.letshoppa.Class;

/**
 * Created by Feechan on 9/12/2016.
 */
public class Toko {
    private int tokoid;
    private String namatoko;
    private String deskripsitoko;
    private String lokasitoko;
    private int gambartokoid;
    private int jenistokoid;
    private int accountid;
    private int statustoko;

    public Toko(String namatoko, String deskripsitoko) {
        this.namatoko = namatoko;
        this.deskripsitoko = deskripsitoko;
    }

    public int getTokoid() {
        return tokoid;
    }

    public void setTokoid(int tokoid) {
        this.tokoid = tokoid;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public String getDeskripsitoko() {
        return deskripsitoko;
    }

    public void setDeskripsitoko(String deskripsitoko) {
        this.deskripsitoko = deskripsitoko;
    }

    public String getLokasitoko() {
        return lokasitoko;
    }

    public void setLokasitoko(String lokasitoko) {
        this.lokasitoko = lokasitoko;
    }

    public int getGambartokoid() {
        return gambartokoid;
    }

    public void setGambartokoid(int gambartokoid) {
        this.gambartokoid = gambartokoid;
    }

    public int getJenistokoid() {
        return jenistokoid;
    }

    public void setJenistokoid(int jenistokoid) {
        this.jenistokoid = jenistokoid;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public int getStatustoko() {
        return statustoko;
    }

    public void setStatustoko(int statustoko) {
        this.statustoko = statustoko;
    }
}
