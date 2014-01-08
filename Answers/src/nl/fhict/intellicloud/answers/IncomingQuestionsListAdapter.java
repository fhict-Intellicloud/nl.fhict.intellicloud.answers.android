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
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class IncomingQuestionsListAdapter extends ArrayAdapter<Question> implements Filterable{
    private final Context context;
    private ArrayList<Question> values;
    private final ArrayList<Question> allQuestions;

    Filter questionFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Question> tempList = new ArrayList<Question>();
            
            //constraint is the L-piece that needs to fit right to finish two lines of tetris
            //allQuestions are the multiple options the L-piece can fit in
            if(constraint != null && allQuestions!=null) {
                int length=allQuestions.size();
                int i=0;
                while(i<length){
                    Question item = allQuestions.get(i);
                    //Does the L-piece fit in this spot?
                    if(item.getQuestion().toLowerCase().contains(constraint.toString().toLowerCase()))
                        tempList.add(item);
                    //Other possible filter option:
                    //else if(item.getAsker().getFullName().contains(constraint))
                    //    tempList.add(item);

                    i++;
                }
                //Important, the publishResults method is very picky,
                //it only wants the finest of FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            //Just setting the new list isn't enough,
            //nothing will change if we don't say that there's work to be done.
            values = (ArrayList<Question>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    public IncomingQuestionsListAdapter(Context context, ArrayList<Question> values) {
        super(context, R.layout.listview_item_incoming, values);
        this.context = context;
        this.values = values;
        allQuestions = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.listview_item_incoming, parent, false);

	    Question question = values.get(position);

	    ImageView questionStageImage = (ImageView) rowView.findViewById(R.id.ivIncommingQuestionState);
	    questionStageImage.setImageResource(getImageId(question.getQuestionState()));
	    TextView userLine = (TextView) rowView.findViewById(R.id.txtUser);
	    if(question.getAsker() != null) {
	    	userLine.setText(question.getAsker().getFullName());
	    }
	    else
	    {
	    	userLine.setText(R.string.unknownUser);
	    }
	    TextView timeLine = (TextView) rowView.findViewById(R.id.txtTimeAgo);
	    timeLine.setText(setTime(question.getDate())); 
	    TextView questionLine = (TextView) rowView.findViewById(R.id.txtQuestion);
	    questionLine.setText(question.getQuestion());
	    questionLine.setMaxWidth(rowView.getWidth()-questionStageImage.getWidth());
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
				if(days >1) {
					return Long.toString(days) + " " + getContext().getResources().getString(R.string.daysAgo);
				}
				return Long.toString(days) + " " + getContext().getResources().getString(R.string.dayAgo);
			}
			if(hours > 0){
				if(hours > 1){
					return Long.toString(hours) + " " + getContext().getResources().getString(R.string.hoursAgo);
				}
				return Long.toString(hours) + " " + getContext().getResources().getString(R.string.hourAgo);
			}
			if(minutes > 0){
				if(minutes > 1){
					return Long.toString(minutes) + " " + getContext().getResources().getString(R.string.minutesAgo);
				}
				return Long.toString(minutes) + " " + getContext().getResources().getString(R.string.minuteAgo);
			}
			return getContext().getResources().getString(R.string.justPosted);
	  }

    @Override
    public Filter getFilter(){
        return questionFilter;
    }

    //Needs to be overridden to prevent index out of bounds exceptions from popping up
    @Override
    public int getCount() {
        return values.size();
    }
}
