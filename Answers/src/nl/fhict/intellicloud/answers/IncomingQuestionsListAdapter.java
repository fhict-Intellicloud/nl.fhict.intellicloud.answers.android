package nl.fhict.intellicloud.answers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import nl.fhict.intellicloud.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IncomingQuestionsListAdapter extends ArrayAdapter<Question> {
	 private final Context context;
	 private final ArrayList<Question> values;

	  public IncomingQuestionsListAdapter(Context context, ArrayList<Question> values) {
	    super(context, R.layout.listview_item_incoming, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.listview_item_incoming, parent, false);
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    Question question = values.get(position);
	    
	    TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
	    firstLine.setText(question.getQuestion());
	    TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
	    
	    if(question.getDate() == null){
	    	secondLine.setText("06-12-2013");
	    	//secondLine.setText(R.string.unknown_date);
	    }
	    else
	    {
	    	secondLine.setText(sdf.format(question.getDate()));
	    }
	    

	    return rowView;
	  }
}
