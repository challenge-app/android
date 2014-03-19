package br.com.challengeaccepted;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import br.com.challengeaccepted.adapter.TabViewPagerAdapter;
import br.com.challengeaccepted.commons.SmoothScrollViewPager;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

public class MainActivity extends ActionBarActivity {
	
	// Tabs
	private SmoothScrollViewPager pager;
	private PagerSlidingTabStrip tabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initLayout();
		
		// Set the pager with an adapter
		pager.setAdapter(new TabViewPagerAdapter(getSupportFragmentManager(), MainActivity.this));
		pager.setOffscreenPageLimit(4);
		
		// Bind the widget to the adapter
		tabs.setViewPager(pager);
		tabs.setShouldExpand(true);
	}

	private void initLayout() {
		setContentView(R.layout.activity_main);
		
		pager = (SmoothScrollViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      case R.id.menu_challenge:
	    	  Intent i = new Intent(MainActivity.this, ChooseFriendActivity.class);
	    	  startActivity(i);
	    	  overridePendingTransition(R.anim.slide_out_left_transition, R.anim.slide_in_right_transition);
	        return true;
	      default:
            return super.onOptionsItemSelected(item);
	  }
	}
	
}
