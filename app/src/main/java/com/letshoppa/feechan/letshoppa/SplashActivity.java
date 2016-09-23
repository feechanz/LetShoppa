package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;

/**
 * Created by Feechan on 9/17/2016.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppHelper.pref = this.getSharedPreferences(AppHelper.PREF_NAME, AppHelper.PRIVATE_MODE);
        AppHelper.editor = AppHelper.pref.edit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
