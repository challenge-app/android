package br.com.challengeaccepted;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
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
		setContentView(R.layout.activity_main);
		
		pager = (SmoothScrollViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		
		// Set the pager with an adapter
		pager.setAdapter(new TabViewPagerAdapter(getSupportFragmentManager(), MainActivity.this));
		pager.setOffscreenPageLimit(1);
		
		// Bind the widget to the adapter
		tabs.setViewPager(pager);
		tabs.setShouldExpand(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
