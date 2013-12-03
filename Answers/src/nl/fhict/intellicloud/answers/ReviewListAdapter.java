package nl.fhict.intellicloud.answers;

import java.util.List;

import nl.fhict.intellicloud.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReviewListAdapter extends BaseAdapter {

	List<Answer> answerList;
	Activity activity;
	
	public ReviewListAdapter(Activity activity, List<Answer> answerList)
	{
		this.activity = activity;
		this.answerList = answerList;
	}
	
	@Override
	public int getCount() {
		return answerList.size();
	}

	@Override
	public Object getItem(int position) {
		return answerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		View rowView = view;
		Answer answer = answerList.get(position);
		if(rowView == null){
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.answer_list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.ivUserPicture = (ImageView) rowView.findViewById(R.id.ivUserPicture);
			viewHolder.tvRequestor = (TextView) rowView.findViewById(R.id.tvRequestor);
			viewHolder.tvDate = (TextView) rowView.findViewById(R.id.tvDate);
			viewHolder.tvQuestion = (TextView) rowView.findViewById(R.id.tvQuestion);
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		//holder.ivUserPicture.setImageDrawable(drawable);
		holder.tvRequestor.setText(answer.getAnswerer().getFirstName() + " " + answer.getAnswerer().getLastName());
		holder.tvDate.setText("HIER KOMT DATUM: 27-11-2013");
		holder.tvQuestion.setText(answer.getQuestion().getQuestion());
		
		return rowView;
	}
	
	static class ViewHolder{
		public ImageView ivUserPicture;
		public TextView tvRequestor;
		public TextView tvDate;
		public TextView tvQuestion;
	}

}
