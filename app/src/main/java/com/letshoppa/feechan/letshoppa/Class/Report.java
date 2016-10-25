package com.letshoppa.feechan.letshoppa.Class;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Feechan on 10/24/2016.
 */

public class Report {
    public static final String TAG_REPORT = "Report";
    public static final String TAG_JENISTOKOID = "jenistokoid";
    public static final String TAG_NAMAJENIS = "namajenis";
    public static final String TAG_STATUSINCART = "statusincart";
    public static final String TAG_STATUSWANTTOBUY = "statuswanttobuy";
    public static final String TAG_STATUSACCEPTED = "statusaccepted";
    public static final String TAG_STATUSCANCELLED = "statuscancelled";
    public static final String TAG_STATUSTOTAL = "statustotal";

    public static final String TAG_BEGINDATE = "begindate";
    public static final String TAG_ENDDATE = "enddate";

    private int jenistokoid;
    private String namajenis;
    private int statusincart;
    private int statuswanttobuy;
    private int statusaccepted;
    private int statuscancelled;
    private int statustotal;

    public int getJenistokoid() {
        return jenistokoid;
    }

    public void setJenistokoid(int jenistokoid) {
        this.jenistokoid = jenistokoid;
    }

    public String getNamajenis() {
        return namajenis;
    }

    public void setNamajenis(String namajenis) {
        this.namajenis = namajenis;
    }

    public int getStatusincart() {
        return statusincart;
    }

    public void setStatusincart(int statusincart) {
        this.statusincart = statusincart;
    }

    public int getStatuswanttobuy() {
        return statuswanttobuy;
    }

    public void setStatuswanttobuy(int statuswanttobuy) {
        this.statuswanttobuy = statuswanttobuy;
    }

    public int getStatusaccepted() {
        return statusaccepted;
    }

    public void setStatusaccepted(int statusaccepted) {
        this.statusaccepted = statusaccepted;
    }

    public int getStatuscancelled() {
        return statuscancelled;
    }

    public void setStatuscancelled(int statuscancelled) {
        this.statuscancelled = statuscancelled;
    }

    public int getStatustotal() {
        return statustotal;
    }

    public void setStatustotal(int statustotal) {
        this.statustotal = statustotal;
    }

    public static Report GetReportFromJson(JSONObject jsonObject)
    {
        try {
            Report report = new Report();

            report.setJenistokoid(jsonObject.getInt(Report.TAG_JENISTOKOID));
            report.setNamajenis(jsonObject.getString(Report.TAG_NAMAJENIS));
            report.setStatusincart(jsonObject.getInt(Report.TAG_STATUSINCART));
            report.setStatuswanttobuy(jsonObject.getInt(Report.TAG_STATUSWANTTOBUY));
            report.setStatusaccepted(jsonObject.getInt(Report.TAG_STATUSACCEPTED));
            report.setStatuscancelled(jsonObject.getInt(Report.TAG_STATUSCANCELLED));
            report.setStatustotal(jsonObject.getInt(Report.TAG_STATUSTOTAL));
            return report;
        }
        catch(JSONException e)
        {
            AppHelper.Message = e.getMessage();
            return null;
        }
    }
}
