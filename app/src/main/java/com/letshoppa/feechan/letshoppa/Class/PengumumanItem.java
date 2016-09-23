package com.letshoppa.feechan.letshoppa.Class;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Feechan on 6/21/2016.
 */
public class PengumumanItem
{
    private String isiPengumuman;
    private String penulisPengumuman;

    public PengumumanItem(String isiPengumuman, String penulisPengumuman)
    {
        this.penulisPengumuman = penulisPengumuman;
        this.isiPengumuman = isiPengumuman;
    }

    public String getIsiPengumuman() {
        return isiPengumuman;
    }

    public void setIsiPengumuman(String isiPengumuman) {
        this.isiPengumuman = isiPengumuman;
    }

    public String getPenulisPengumuman() {
        return penulisPengumuman;
    }

    public void setPenulisPengumuman(String penulisPengumuman) {
        this.penulisPengumuman = penulisPengumuman;
    }
}

