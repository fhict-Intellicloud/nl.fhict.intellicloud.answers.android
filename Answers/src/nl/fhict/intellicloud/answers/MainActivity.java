package nl.fhict.intellicloud.answers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import nl.fhict.intellicloud.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mFilterTitles;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mFilterTitles = getResources().getStringArray(R.array.filter_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mFilterTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
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
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
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
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
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
        mDrawerLayout.closeDrawer(mDrawerList);
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

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class ListFragment extends Fragment {
        public static final String ARG_FILTER_NUMBER = "filter_number";

        public ListFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            int i = getArguments().getInt(ARG_FILTER_NUMBER);
            String filter = getResources().getStringArray(R.array.filter_array)[i];

            getActivity().setTitle(filter);
            return rootView;
        }
        
		@Override
    	public void onViewCreated(View view, Bundle savedInstanceState) {
    		super.onViewCreated(view, savedInstanceState);
    		
    		ListView lv = (ListView)getActivity().findViewById(R.id.lvIncomingQuestions);
    	    
    	    
    	    int i = getArguments().getInt(ARG_FILTER_NUMBER);
    	    ArrayList<Question> list = createListWithFilter(createDummyQuestions(), i);
    	    IncomingQuestionsListAdapter iqla = new IncomingQuestionsListAdapter(getView().getContext(), list);
    	    lv.setAdapter(iqla);
    	}
		
		private ArrayList<Question> createListWithFilter(ArrayList<Question> listToFilter, int filterId) {
			ArrayList<Question> list = new ArrayList<Question>();
			switch(filterId) {
			case 0:
				list = listToFilter;
				break;
			case 1:
				for(Question q : listToFilter){
					if(q.getQuestionState().equals(QuestionState.Open)){
						list.add(q);
					}
				}
				break;
			case 2:
				for(Question q : listToFilter){
					if(q.getQuestionState().equals(QuestionState.Closed)){
						list.add(q);
					}
				}
				break;
			case 3:
				for(Question q : listToFilter){
					if(q.getQuestionState().equals(QuestionState.UpForAnswer)){
						list.add(q);
					}
				}
				break;
			case 4:
				for(Question q : listToFilter){
					if(q.getQuestionState().equals(QuestionState.UpForFeedback)){
						list.add(q);
					}
				}
				break;
			}
			return list;
		}
        public ArrayList<Question> createDummyQuestions() {
        	
    		ArrayList<Question> list = new ArrayList<Question>();
    		list.add(new Question(1, "What does the fox say?", null, null, QuestionState.Open, new Date()));
    		list.add(new Question(2, "What is love?", null, null, QuestionState.Open, new Date()));
    		list.add(new Question(3, "Do you know the muffin man?", null, null, QuestionState.Open, new Date()));
    		list.add(new Question(4, "What is your name?", null, null, QuestionState.Open, new Date()));
    		list.add(new Question(5, "What is your quest?", null, null, QuestionState.Open, null));
    		list.add(new Question(6, "What is the airspeed velocity of an unladen swallow?", null, null, QuestionState.Open, setDate("2013-12-04 09:27:37")));
    		list.add(new Question(1, "Does looking at a picture of the sun hurt your eyes?", null, null, QuestionState.Closed, setDate("2013-12-05 09:27:37")));
    		list.add(new Question(2, "How can I lose weight without moving?", null, null, QuestionState.Closed, new Date()));
    		list.add(new Question(3, "How do I get accepted into Hogwarts?", null, null, QuestionState.UpForAnswer, new Date()));
    		list.add(new Question(4, "Is it posible to make 1+1=3?", null, null, QuestionState.UpForAnswer, new Date()));
    		list.add(new Question(5, "HOW DO I TURN OF CAPSLOCK?", null, null, QuestionState.UpForFeedback, null));
    		list.add(new Question(6, "Is this an oke question?", null, null, QuestionState.UpForFeedback, new Date()));
    		return list;
    	}
        
        private Date setDate(String date) {
        	String dtStart = date;  
        	SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	Date myDate;
        	try {
        		return format.parse(dtStart);
        	} catch (Exception e) {  
        	    // TODO Auto-generated catch block  
        	    e.printStackTrace();  
        	}
			return null;
        }
    }
}