package nl.fhict.intellicloud.answers;

import java.text.SimpleDateFormat;
import nl.fhict.intellicloud.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
	    
	    Question question = values.get(position);
	    
	    ImageView questionStageImage = (ImageView) rowView.findViewById(R.id.incommingQuestionStateImage);
	    questionStageImage.setImageResource(getImageId(question.getQuestionState()));
	    TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
	    firstLine.setText(question.getQuestion());
	    TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
	    secondLine.setText(setTime(question.getDate()));   

	    return rowView;
	  }
	  
	  private int getImageId(QuestionState state){
		  
		  switch(state){
		  	case Open:
		  		return R.drawable.search_icon_green;
		case Closed:		  
				return R.drawable.rejected_icon_green;
			case UpForAnswer:
				return R.drawable.hourglass_icon_green;
			case UpForFeedback:
				return R.drawable.glass_icon_green;
			  }
		return -1;
		  
	  }
	  private String setTime(Date creationDate) {
		if(creationDate == null){
			  return getContext().getResources().getString(R.string.unknown_date);
		}
		Date currentDate = new Date();
      	long diffInMillisec = currentDate.getTime() - creationDate.getTime();
			long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);
			diffInSec/= 60;
			long minutes =diffInSec % 60;
			diffInSec /= 60;
			long hours = diffInSec % 24;
			diffInSec /= 24;
			long days = diffInSec;
			
			if(days > 0){
				return Long.toString(days) + " " + getContext().getResources().getString(R.string.daysAgo);
			}
			if(hours > 0){
				return Long.toString(hours) + " " + getContext().getResources().getString(R.string.hoursAgo);
			}
			if(minutes > 0){
				return Long.toString(minutes) + " " + getContext().getResources().getString(R.string.minutesAgo);
			}
			return getContext().getResources().getString(R.string.justPosted);
     }
}
