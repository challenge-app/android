package br.com.challengeaccepted.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.fragments.ChallengeFragment;

public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
	 
    private final int PAGES = 1;
    private String[] titles = {""};
    private Context ctx;
 
    public TabViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.ctx = context;
        
        // Initialize Tabs Titles
        titles[0] = context.getString(R.string.tab1_challenges);
    }
 
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChallengeFragment(ctx);
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
