package nl.fhict.intellicloud.answers;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class QuestionListOnClickListener implements OnItemClickListener {
	
	private List<Question> list;
	private Activity activity;
	
	public QuestionListOnClickListener(Activity activity, List<Question> list){
		this.activity = activity;
		this.list = list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Question question = list.get(position);

        /**
         * Switch to make an intent for the right activity
         */
        switch (question.getQuestionState()){
            case Open:
                Intent openIntent = new Intent(activity.getApplicationContext(), SendAnswerActivity.class);
                openIntent.putExtra("questionInt", question.getId());
                activity.startActivity(openIntent);
                break;
            case UpForAnswer:
                break;
            case UpForFeedback:
                Intent feedbackIntent = new Intent(activity.getApplicationContext(), ReviewOverviewActivity.class);
                feedbackIntent.putExtra("reviewInt", question.getId());
                activity.startActivity(feedbackIntent);
                break;
            case Closed:
                break;
        }
	}
}
