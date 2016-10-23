package com.letshoppa.feechan.letshoppa.AdapterList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.letshoppa.feechan.letshoppa.Fragment.InboxFragment;
import com.letshoppa.feechan.letshoppa.Fragment.SentboxFragment;

/**
 * Created by Feechan on 10/23/2016.
 */

public class MessagePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MessagePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    InboxFragment tab1;
    SentboxFragment tab2;
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                //TabFragment1 tab1 = new TabFragment1();
                // return tab1;
                if(tab1 == null) {
                    tab1 = new InboxFragment();
                }
                return tab1;
            case 1:
                //TabFragment2 tab2 = new TabFragment2();
                //return tab2;
                if(tab2==null) {
                    tab2 = new SentboxFragment();
                }
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}