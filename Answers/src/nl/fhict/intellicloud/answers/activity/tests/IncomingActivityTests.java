package nl.fhict.intellicloud.answers.activity.tests;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.ListView;
import nl.fhict.intellicloud.answers.*;

public class IncomingActivityTests extends ActivityUnitTestCase<MainActivity>  {

	private MainActivity activity;
	
	public IncomingActivityTests() {
		super(MainActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
		        MainActivity.class);
		    startActivity(intent, null, null);
		    activity = getActivity();
	}
	
	public void testStringsFilterList() {
	    int drawerList = nl.fhict.intellicloud.R.id.lvDrawer;
	    assertNotNull(activity.findViewById(drawerList));
	    ListView view = (ListView) activity.findViewById(drawerList);
	    
	    int qu = view.getCount();
	    assertEquals(5, qu);
	    
	    //check the filter titles
	    String[] filterArray = this.getActivity().getResources().getStringArray(nl.fhict.intellicloud.R.array.filter_array);
	    
	    assertEquals(filterArray[0], view.getItemAtPosition(0));
	    assertEquals(filterArray[1], view.getItemAtPosition(1));
	    assertEquals(filterArray[2], view.getItemAtPosition(2));
	    assertEquals(filterArray[3], view.getItemAtPosition(3));
	    assertEquals(filterArray[4], view.getItemAtPosition(4));
	  }
}
