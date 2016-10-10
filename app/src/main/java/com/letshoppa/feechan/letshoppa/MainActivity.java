package com.letshoppa.feechan.letshoppa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Fragment.FollowingFragment;
import com.letshoppa.feechan.letshoppa.Fragment.MessageFragment;
import com.letshoppa.feechan.letshoppa.Fragment.MyCartFragment;
import com.letshoppa.feechan.letshoppa.Fragment.MyOrderFragment;
import com.letshoppa.feechan.letshoppa.Fragment.MyPurchaseFragment;
import com.letshoppa.feechan.letshoppa.Fragment.MyShopFragment;
import com.letshoppa.feechan.letshoppa.Fragment.ProfileFragment;
import com.letshoppa.feechan.letshoppa.Fragment.RootHomeFragment;
import com.letshoppa.feechan.letshoppa.Fragment.RootShoppingFragment;
import com.letshoppa.feechan.letshoppa.Fragment.SettingFragment;

//import com.letshoppa.feechan.letshoppa.dummy.DummyContent;

public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {
    TextView mNameAccountTextView;
    ImageView mProfileImageView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //floating mail btn
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                HideKeyboard();
                super.onDrawerStateChanged(newState);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        HomeFragmentLoad();
        HideKeyboard();
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        mNameAccountTextView = (TextView) header.findViewById(R.id.accountNameTextView);
        mNameAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProfile();
            }
        });
        mProfileImageView = (ImageView) header.findViewById(R.id.profileImageView);
        AppHelper.profilePicture = mProfileImageView;
        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProfile();
            }
        });
        //setContentView(R.layout.content_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    private void clickProfile()
    {
        if(AppHelper.isLoggedIn()) {
            HideKeyboard();
            ProfileFragmentLoad();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onResume() {
        if (AppHelper.isLoggedIn()) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_login);
            AppHelper.getAccountFromSession();
            loginProfile();
            //HomeFragmentLoad();
        }


        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            LoginActivityLoad();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ///
    private void ProfileFragmentLoad()
    {
        Fragment fragment = new ProfileFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
    }

    private void LoginActivityLoad() {
        HideKeyboard();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void ShopFragmentLoad() {
        Fragment fragment = new RootShoppingFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
    }

    private void FollowingFragmentLoad() {
        Fragment fragment = new FollowingFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
    }

    private void HomeFragmentLoad() {
        Fragment fragment = new RootHomeFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
        HideKeyboard();
    }

    private void MessageFragmentLoad() {
        Fragment fragment = new MessageFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
        HideKeyboard();
    }

    private void MyCartFragmentLoad() {
        Fragment fragment = new MyCartFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
        HideKeyboard();
    }

    private void MyOrderFragmentLoad() {
        Fragment fragment = new MyOrderFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
        HideKeyboard();
    }

    private void MyPurchaseFragmentLoad() {
        Fragment fragment = new MyPurchaseFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
        HideKeyboard();
    }

    private void MyShopFragmentLoad() {
        Fragment fragment = new MyShopFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
        HideKeyboard();
    }

    private void HideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void loginProfile() {
        if (AppHelper.currentAccount != null) {
            mNameAccountTextView.setText(AppHelper.currentAccount.getNama());
            ImageLoadTask imageTask = new ImageLoadTask(AppHelper.currentAccount.getLinkgambaraccount(),mProfileImageView);
            imageTask.execute();
        }
    }

    private void logoutProfile() {
        mNameAccountTextView.setText(getString(R.string.app_name));
        mProfileImageView.setImageResource(android.R.drawable.sym_def_app_icon);
    }

    private void LogoutActivity() {
        if (AppHelper.isLoggedIn()) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            AppHelper.removeLoginSession();
            logoutProfile();
            //AppHelper.isLogin = false;
            //AppHelper.currentAccount = null;
            HomeFragmentLoad();
        }
    }

    private void SettingFragmentLoad() {
        Fragment fragment = new SettingFragment();
        FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction().replace(R.id.mainFrame, fragment).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        HideKeyboard();
        if (id == R.id.nav_home) {
            HomeFragmentLoad();
        } else if (id == R.id.nav_shopping) {
            ShopFragmentLoad();
        } else if (id == R.id.nav_following) {
            FollowingFragmentLoad();
        } else if (id == R.id.nav_message) {
            MessageFragmentLoad();
        } else if (id == R.id.nav_manage) {
            SettingFragmentLoad();
        } else if (id == R.id.nav_mycart) {
            MyCartFragmentLoad();
        } else if (id == R.id.nav_myshop) {
            MyShopFragmentLoad();
        } else if (id == R.id.nav_myorder) {
            MyOrderFragmentLoad();
        } else if (id == R.id.nav_mypurchase) {
            MyPurchaseFragmentLoad();
        } else if (id == R.id.nav_login) {
            //HomeFragmentLoad();
            LoginActivityLoad();
        } else if (id == R.id.nav_logout) {
            LogoutActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.letshoppa.feechan.letshoppa/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.letshoppa.feechan.letshoppa/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
