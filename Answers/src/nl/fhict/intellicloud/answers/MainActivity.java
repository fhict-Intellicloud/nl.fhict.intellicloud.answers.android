package nl.fhict.intellicloud.answers;

import java.util.Locale;

import nl.fhict.intellicloud.R;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 
 * @author Joris & Remco
 * 
 * Class containing the TabListener for two tabs.
 * The first tab is a TabFragmentIncomingQuestions fragment.
 * The second tab is a TabFragmentReviewQuestions fragment.
 *
 */
public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	
	/**
	 * Perform initialization of all fragments and loaders.
	 * 
	 * @param savedInstanceState If the activity is being re-initialized after previously being shut down then 
	 * this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	
	/**
	 * Called when a tab enters the selected state.
	 * 
	 * @param tab The tab that was reselected.
	 * @param ft A FragmentTransaction for queuing fragment operations to execute during a tab switch. 
	 * The previous tab's unselect and this tab's select will be executed in a single transaction. 
	 * This FragmentTransaction does not support being added to the back stack.
	 */
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	/**
	 * Called when a tab exits the selected state.
	 * 
	 * @param tab The tab that was reselected.
	 * @param ft A FragmentTransaction for queuing fragment operations to execute during a tab switch. 
	 * This tab's unselect and the newly selected tab's select will be executed in a single transaction. 
	 * This FragmentTransaction does not support being added to the back stack.
	 */
	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * Called when a tab that is already selected is chosen again by the user. 
	 * Some applications may use this action to return to the top level of a category.
	 * 
	 * @param tab The tab that was reselected.
	 * @param ft A FragmentTransaction for queuing fragment operations to execute once this method returns. 
	 * This FragmentTransaction does not support being added to the back stack.
	 */
	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * Return the Fragment associated with a specified position.
		 * 
		 * @param position The position within this adapter.
		 */
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// the first page is for incoming questions
			// the second page is for reviewing answers
			
			Fragment fragment = null;
			
			switch(position) {
			
				case 0:
				fragment = new TabFragmentIncomingQuestions();
				break;
				
				case 1:
					fragment = new TabFragmentReviewQuestions();
					break;
					
				default:
					break;
			}
			return fragment;
		}

		/**
		 * Returns the total number of pages within the adapter.
		 */
		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		/**
		 * Returns the title of the page at a position.
		 * 
		 * @param position The position within this adapter.
		 */
		@Override
		public CharSequence getPageTitle(int position) {			
			//Return the page title for each tab
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.tabbarname_incomingquestions).toUpperCase(l);
			case 1:
				return getString(R.string.tabbarname_reviewquestions).toUpperCase(l);
			}
			return null;
		}
	}
}
