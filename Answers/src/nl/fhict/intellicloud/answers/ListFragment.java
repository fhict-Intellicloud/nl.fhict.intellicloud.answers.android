package nl.fhict.intellicloud.answers;

import java.util.ArrayList;


import nl.fhict.intellicloud.answers.backendcommunication.*;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private ListView lv;
    IncomingQuestionsListAdapter iqla;
    ArrayList<Question> list;
    ContentObserver backendContentObserver;

    public ListFragment() {
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        int i = getArguments().getInt(ARG_FILTER_NUMBER);
        String filter = getResources().getStringArray(R.array.filter_array)[i];
        questionService = new QuestionDataSource(getActivity());
        getActivity().setTitle(filter);
        return rootView;
    }
    private void refreshList()
    {
    	
    	 int i = getArguments().getInt(ARG_FILTER_NUMBER);
    	 
    	 FilterList listFilter = new FilterList();
    	 list = listFilter.createListWithFilter(questionService.GetQuestions(), i);
    	 iqla.clear();
    	 iqla.addAll(list);
    	 iqla.notifyDataSetChanged();
    	
    	 
    	 
    }
    
    
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
				
		lv = (ListView)getActivity().findViewById(R.id.lvIncomingQuestions);
	    
	    int i = getArguments().getInt(ARG_FILTER_NUMBER);
	    
	    FilterList listFilter = new FilterList();
	    list = listFilter.createListWithFilter(questionService.GetQuestions(), i);
	    iqla = new IncomingQuestionsListAdapter(getView().getContext(), list);

	    lv.setAdapter(iqla);
        qListOnClickListener = new QuestionListOnClickListener(getActivity(), list);
        lv.setOnItemClickListener(qListOnClickListener);
        backendContentObserver = new ContentObserver(null) 
        {
        	public void onChange(boolean selfChange) {   
        		getActivity().runOnUiThread(new Runnable() {
            	    @Override
            	    public void run() {
            	        refreshList();
            	    }
            	});
            }
        	
        };
 
		getActivity().getContentResolver().registerContentObserver(BackendContentProvider.CONTENT_QUESTIONS, true, backendContentObserver);


	}
	

    /**
     * Sets the filter of the Question array
     * @param searchText The new search query
     */
    public void setSearchFilter(String searchText){
        iqla.getFilter().filter(searchText);
    }

	@Override
	public void onDestroy() {
		getActivity().getApplicationContext().getContentResolver().unregisterContentObserver(backendContentObserver);
		super.onDestroy();
	}
}