package com.letshoppa.feechan.letshoppa.AdapterList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.letshoppa.feechan.letshoppa.Fragment.MyReviewFragment;
import com.letshoppa.feechan.letshoppa.Fragment.MyShopDetailFragment;
import com.letshoppa.feechan.letshoppa.Fragment.MyShopProductFragment;

/**
 * Created by Feechan on 9/27/2016.
 */

public class MyShopPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MyShopPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    MyShopDetailFragment tab1;
    MyShopProductFragment tab2;
    MyReviewFragment tab3;
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                //TabFragment1 tab1 = new TabFragment1();
                // return tab1;
                if(tab1 == null) {
                    tab1 = new MyShopDetailFragment();
                }
                return tab1;
            case 1:
                //TabFragment2 tab2 = new TabFragment2();
                //return tab2;
                if(tab2==null) {
                    tab2 = new MyShopProductFragment();
                }
                return tab2;
            case 2:
                if(tab3==null) {
                    tab3 = new MyReviewFragment();
                }
                return tab3;
                //TabFragment3 tab3 = new TabFragment3();
                //return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}