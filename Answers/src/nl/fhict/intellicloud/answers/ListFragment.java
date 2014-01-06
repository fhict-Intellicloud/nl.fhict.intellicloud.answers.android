package nl.fhict.intellicloud.answers;

import java.util.ArrayList;

import nl.fhict.intellicloud.R;
import nl.fhict.intellicloud.answers.backendcommunication.DummyBackend;
import nl.fhict.intellicloud.answers.backendcommunication.IQuestionService;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragment extends Fragment {

    public static final String ARG_FILTER_NUMBER = "filter_number";
    private IQuestionService questionService;
    private QuestionListOnClickListener qListOnClickListener;
    IncomingQuestionsListAdapter iqla;

    public ListFragment() {
    	questionService = new DummyBackend();
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
        EditText inputSearch = (EditText)getActivity().findViewById(R.id.etInputSearch);
	    
	    
	    int i = getArguments().getInt(ARG_FILTER_NUMBER);
	    
	    ArrayList<Question> list = createListWithFilter(questionService.GetQuestions(), i);
	    iqla = new IncomingQuestionsListAdapter(getView().getContext(), list);
	    lv.setAdapter(iqla);
        qListOnClickListener = new QuestionListOnClickListener(getActivity(), list);
        lv.setOnItemClickListener(qListOnClickListener);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ListFragment.this.iqla.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
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
}