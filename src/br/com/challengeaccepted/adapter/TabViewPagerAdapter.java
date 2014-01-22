package br.com.challengeaccepted.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.fragments.*;

public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
	 
    private final int PAGES = 2;
    private String titles[] = {"",""};
    private Context ctx;
 
    public TabViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.ctx = context;
        
        // Initialize Tabs Titles
        titles[0] = context.getString(R.string.tab1_challenges);
        titles[1] = context.getString(R.string.tab2_challenges);
    }
 
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChallengeFriendsActivity(ctx);
            case 1:
            	return new FriendsFragment(ctx);
            default:
                throw new IllegalArgumentException("The item position should be less or equal to:" + PAGES);
        }
    }
 
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
 
    @Override
    public int getCount() {
        return PAGES;
    }
    
}
