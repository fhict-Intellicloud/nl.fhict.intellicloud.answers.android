package nl.fhict.intellicloud.answers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.fhict.intellicloud.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author Joris & Remco
 * 
 * This fragment contains a list with all answers that requested reviewing.
 *
 */
public class TabFragmentReviewQuestions extends Fragment {
	ListView lvAnswer;
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

		View tablayout = inflater.inflate(R.layout.tab_fragment_review, null);
		List<Answer> list = new ArrayList<Answer>();
		User user1 = new User(1, "Remco", "Loeff", "", UserType.Employee);
		User user2 = new User(2, "Hans", "Grietje", "En", UserType.Customer);
		Question q = new Question(21, "HOI?", user1, user2, QuestionState.Open, new Date());
		Answer anwser = new Answer(1,"HELL YEAH",q, user1, AnswerState.UnderReview);
		
		list.add(anwser);
		list.add(anwser);
		list.add(anwser);
		
		ReviewListAdapter adapter = new ReviewListAdapter(this.getActivity(), list);
		lvAnswer = (ListView) tablayout.findViewById(R.id.lvReviewAnswer);
		lvAnswer.setAdapter(adapter);
		AnswerListOnClickListener listClickListener = new AnswerListOnClickListener(getActivity(), list);
		
		lvAnswer.setOnItemClickListener(listClickListener);
		
		return tablayout;

	}
}
