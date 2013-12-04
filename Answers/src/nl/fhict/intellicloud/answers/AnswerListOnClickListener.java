package nl.fhict.intellicloud.answers;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class AnswerListOnClickListener implements OnItemClickListener {
	
	private List<Answer> list;
	private Activity activity;
	
	public AnswerListOnClickListener(Activity activity, List<Answer> list){
		this.activity = activity;
		this.list = list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Answer answer = list.get(position);
		Intent intent = new Intent(activity.getApplicationContext(), ReviewOverviewActivity.class);
		intent.putExtra("Answer", 1);
		activity.startActivity(intent);
	}
}
