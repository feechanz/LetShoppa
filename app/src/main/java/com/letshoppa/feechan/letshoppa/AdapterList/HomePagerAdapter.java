package com.letshoppa.feechan.letshoppa.AdapterList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.letshoppa.feechan.letshoppa.Fragment.HomeFragment;
import com.letshoppa.feechan.letshoppa.Fragment.PeopleFragment;

/**
 * Created by Feechan on 6/22/2016.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    HomeFragment tab1;
    PeopleFragment tab2;

    public HomePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if(tab1==null) {
                    tab1 = new HomeFragment();
                }
                return tab1;
            case 1:
                if(tab2==null) {
                    tab2 = new PeopleFragment();
                }
                return tab2;
            case 2:
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
