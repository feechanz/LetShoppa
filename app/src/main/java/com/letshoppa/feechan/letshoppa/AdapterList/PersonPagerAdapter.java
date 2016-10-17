package com.letshoppa.feechan.letshoppa.AdapterList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.letshoppa.feechan.letshoppa.Fragment.PeopleDetailFragment;
import com.letshoppa.feechan.letshoppa.Fragment.PeopleFolowFragment;
import com.letshoppa.feechan.letshoppa.Fragment.PeopleShopFragment;

/**
 * Created by Feechan on 10/14/2016.
 */

public class PersonPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PersonPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    PeopleDetailFragment tab1;
    PeopleShopFragment tab2;
    PeopleFolowFragment tab3;

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (tab1 == null) {
                    tab1 = new PeopleDetailFragment();
                }
                return tab1;
            case 1:
                if (tab2 == null) {
                    tab2 = new PeopleShopFragment();
                }
                return tab2;
            case 2:
                if (tab3 == null) {
                    tab3 = new PeopleFolowFragment();
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