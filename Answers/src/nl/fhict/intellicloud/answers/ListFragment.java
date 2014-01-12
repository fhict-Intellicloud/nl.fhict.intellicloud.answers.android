package nl.fhict.intellicloud.answers;

import java.util.ArrayList;


import nl.fhict.intellicloud.answers.backendcommunication.DummyBackend;
import nl.fhict.intellicloud.answers.backendcommunication.IQuestionService;
import nl.fhict.intellicloud.answers.backendcommunication.QuestionDataSource;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class ListFragment extends Fragment {

    public static final String ARG_FILTER_NUMBER = "filter_number";
    private IQuestionService questionService;
    private QuestionListOnClickListener qListOnClickListener;
    IncomingQuestionsListAdapter iqla;
    ArrayList<Question> list;

    public ListFragment() {
    	questionService = new QuestionDataSource(getActivity().getApplicationContext());
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
	    
	    FilterList listFilter = new FilterList();
	    list = listFilter.createListWithFilter(questionService.GetQuestions(), i);
	    iqla = new IncomingQuestionsListAdapter(getView().getContext(), list);

	    lv.setAdapter(iqla);
        qListOnClickListener = new QuestionListOnClickListener(getActivity(), list);
        lv.setOnItemClickListener(qListOnClickListener);

	}


    /**
     * Sets the filter of the Question array
     * @param searchText The new search query
     */
    public void setSearchFilter(String searchText){
        iqla.getFilter().filter(searchText);
    }
}