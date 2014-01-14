package nl.fhict.intellicloud.answers;


import nl.fhict.intellicloud.answers.backendcommunication.AnswerDataSource;
import nl.fhict.intellicloud.answers.backendcommunication.DummyBackend;
import nl.fhict.intellicloud.answers.backendcommunication.IAnswerService;
import nl.fhict.intellicloud.answers.backendcommunication.IQuestionService;
import nl.fhict.intellicloud.answers.backendcommunication.QuestionDataSource;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class RejectedAnswerActivity extends Activity {

	int questionInt;
	IQuestionService qService;
    IAnswerService aService;
    
    Question question;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rejected_answer);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		questionInt = getIntent().getExtras().getInt("questionInt");
		qService = new QuestionDataSource(this);
		aService = new AnswerDataSource(this);
		
		question = qService.GetQuestion(questionInt);
		
		TextView tvRejectQuestion = (TextView)findViewById(R.id.tvRejectQuestion);
		tvRejectQuestion.setText(question.getTitle());
		TextView tvRejectQuestionDetail = (TextView)findViewById(R.id.tvRejectedQuestionDetail);
		tvRejectQuestionDetail.setText(question.getQuestion());
		TextView tvRejectedQuestionAnswer = (TextView)findViewById(R.id.tvRejectedQuestionAnswer);
		tvRejectedQuestionAnswer.setText(question.getAnswer().getAnswer());
		TextView tvRejectedQuestionFeedback = (TextView)findViewById(R.id.tvRejectedQuestionFeedback);
		tvRejectedQuestionFeedback.setText("test");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

}
