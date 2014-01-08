package nl.fhict.intellicloud.answers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import nl.fhict.intellicloud.R;
import nl.fhict.intellicloud.answers.backendcommunication.oauth.AuthenticationManager;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.method.DateTimeKeyListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SearchView.OnQueryTextListener {
	private final int AUTHORIZE_REQUEST = 1000;
	private final String PREFERENCES_NAME = "nl.fhict.intellicloud.answers";
	private final String PREFERENCES_KEY = "AUTHORIZATON_CODE";
	
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mLinearLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mFilterTitles;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Get the shared preference which should contain the authorization code
        //If the code is saved in the shared, the application can start getting an access token
        SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(PREFERENCES_KEY))
        	AuthenticationManager.getInstance().Initialize(preferences.getString(PREFERENCES_KEY, null));
        // if the code is not saved yet it should be requested
        // The authorization activity will start a webview for the user to enter his google login credentials
        else
			this.startActivityForResult(new Intent(this, AuthorizationActivity.class), AUTHORIZE_REQUEST);

        mTitle = mDrawerTitle = getTitle();
        mFilterTitles = getResources().getStringArray(R.array.filter_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.lvDrawer);
        mLinearLayout = (RelativeLayout) findViewById(R.id.left_drawer);
        TextView drawerUserTekst = (TextView) findViewById(R.id.txtUserIdText);
        drawerUserTekst.setText("Piet Jansen");
        // set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        SlidingMenuListAdapter smla = new SlidingMenuListAdapter(getApplicationContext(), mFilterTitles);
	    
        mDrawerList.setAdapter(smla);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_navigation_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle); 
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);                
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        
        //int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        //searchView.findViewById(searchPlateId).setBackgroundResource(R.drawable.action_search);

        // change text color
        int searchTextViewId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchTextView = (TextView) searchView.findViewById(searchTextViewId);
        searchTextView.setTextColor(getResources().getColor(R.color.search_question_color));
        searchTextView.setTextSize(22);

        //Set this as the listener for the search text
        searchView.setOnQueryTextListener(this);
        
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    /**
     * Is called when the text in the searchView changes
     * @param searchText The new search query
     * @return
     */
    @Override
    public boolean onQueryTextChange(String searchText) {
        ListFragment fragment = (ListFragment)getFragmentManager().findFragmentById(R.id.content_frame);
        fragment.setSearchFilter(searchText);
        return true;
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ListFragment.ARG_FILTER_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mFilterTitles[position]);
        mDrawerLayout.closeDrawer(mLinearLayout);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    public void onLogoutClick(View v){
    	Toast.makeText(this, getResources().getString(R.string.triedToLogout), Toast.LENGTH_SHORT).show();
    }
    
   /**
    * The authorization code is pulled from the intent. It is then saved in a shared preference so users
    * do not need to authorize the app again. After this is done, the AuthenticationManager is initialized.
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == this.AUTHORIZE_REQUEST) {
    		if(resultCode == Activity.RESULT_OK) {
    			String authorizationCode = data.getExtras().getString(AuthorizationActivity.AUTHORIZATION_CODE);
    			
    			Editor editor = this.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
    			editor.putString(PREFERENCES_KEY, authorizationCode);
    			editor.apply();
    			AuthenticationManager.getInstance().Initialize(authorizationCode);
    			Toast.makeText(this, "Answers is successfully authorized.", Toast.LENGTH_LONG).show();
    		} else {
    			Toast.makeText(this, "Failed to authorize Answers.", Toast.LENGTH_LONG).show();
    		}
    	}
    }
}