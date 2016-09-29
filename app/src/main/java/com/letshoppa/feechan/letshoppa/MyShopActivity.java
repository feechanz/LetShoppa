package com.letshoppa.feechan.letshoppa;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.letshoppa.feechan.letshoppa.AdapterList.MyShopPagerAdapter;

public class MyShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_shop);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.ShopText)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.ProductsText)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.Reviews)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_shop);
        final MyShopPagerAdapter adapter = new MyShopPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //initializeDetailShop();
        //initializeButton();
    }

    /*
    private void initializeLocation()
    {
        EditText locationEditText = (EditText) findViewById(R.id.shopLocationEditText);
        TextView latLongTextView = (TextView) findViewById(R.id.latlongTextView);

        locationEditText.setText(currentShop.getLokasitoko());
        latLongTextView.setText(currentShop.getLatitude()+","+currentShop.getLongitude());

    }
    private void initializeButton()
    {
        ImageButton editActiveImgBtn = (ImageButton) findViewById(R.id.editImageBtn);
        editActiveImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editClicked();
            }
        });

        ImageButton saveLocationImgBtn = (ImageButton) findViewById(R.id.saveLocationImageBtn);
        saveLocationImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLocationClicked();
            }
        });
    }
    private void editLocationClicked()
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //run save location
                        Toast.makeText(MyShopActivity.this,getString(R.string.success),Toast.LENGTH_LONG).show();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.prompt_change_location)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }
    Toko currentShop;

    boolean activeEdit;
    EditText shopNameTextView;
    EditText deskripsiText;
    TextView namaKategoriTextView;
    private void editClicked()
    {
        activeEdit = !activeEdit;
        if(activeEdit)
        {
            setToSave();
        }
        else
        {
            setToEdit();
        }
        setActiveControl();
    }
    private void setToEdit()
    {
        //save data
        TextView editTextView = (TextView) findViewById(R.id.editTextView);
        ImageButton editImageButton = (ImageButton) findViewById(R.id.editImageBtn);

        editTextView.setText(getString(R.string.Edit));
        editImageButton.setImageResource(android.R.drawable.ic_menu_edit);
    }
    private void setToSave()
    {
        TextView editTextView = (TextView) findViewById(R.id.editTextView);
        ImageButton editImageButton = (ImageButton) findViewById(R.id.editImageBtn);

        editTextView.setText(getString(R.string.Save));
        editImageButton.setImageResource(android.R.drawable.ic_menu_save);
    }
    private void setActiveControl()
    {
        shopNameTextView.setEnabled(activeEdit);
        deskripsiText.setEnabled(activeEdit);
    }


    private void initializeDetailShop()
    {
        activeEdit=false;
        Intent i = getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        if(currentShop!=null) {
            ImageView shopImageView = (ImageView) findViewById(R.id.shopImageView);
            ImageLoadTask shopImageLoad = new ImageLoadTask(currentShop.getGambartoko(),shopImageView);
            shopImageLoad.execute();

            shopNameTextView = (EditText) findViewById(R.id.shopNameTextView);
            deskripsiText = (EditText) findViewById(R.id.deskripsiText);
            namaKategoriTextView = (TextView) findViewById(R.id.namaKategoriTextView);

            namaKategoriTextView.setText(currentShop.getNamajenis());
            shopNameTextView.setText(currentShop.getNamatoko());
            deskripsiText.setText(currentShop.getDeskripsitoko());

            setActiveControl();
            initializeLocation();
        }
    }
    */


}
