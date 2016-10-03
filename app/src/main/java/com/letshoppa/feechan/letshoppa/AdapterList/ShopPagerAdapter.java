package com.letshoppa.feechan.letshoppa.AdapterList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.letshoppa.feechan.letshoppa.Fragment.ReviewFragment;
import com.letshoppa.feechan.letshoppa.Fragment.ShopFragment;
import com.letshoppa.feechan.letshoppa.Fragment.ShopProductFragment;

/**
 * Created by Feechan on 10/2/2016.
 */

public class ShopPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ShopPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    ShopProductFragment tab1;
    ShopFragment tab2;
    ReviewFragment tab3;
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if(tab1 == null) {
                    tab1 = new ShopProductFragment();
                }
                return tab1;
            case 1:
                if(tab2==null) {
                    tab2 = new ShopFragment();
                }
                return tab2;
            case 2:
                if(tab3==null) {
                    tab3 = new ReviewFragment();
                }
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}