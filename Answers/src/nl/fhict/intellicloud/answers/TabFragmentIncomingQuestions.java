package nl.fhict.intellicloud.answers;

import nl.fhict.intellicloud.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author Joris
 * 
 * This fragment contains a list of all incoming questions
 *
 */
public class TabFragmentIncomingQuestions extends Fragment {

	/**
	 * Perform initialization of all fragments and loaders.
	 * 
	 * @param savedInstanceState If the activity is being re-initialized after previously being shut down then 
	 * this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null (which is the default implementation). 
	 * This will be called between onCreate(Bundle) and onActivityCreated(Bundle).
	 * If you return a View from here, you will later be called in onDestroyView() when the view is being released.
	 * 
	 * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
	 * @param container	If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
	 * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View tablayout = inflater.inflate(R.layout.tab_fragment_incomming, null);
		return tablayout;

	}

}