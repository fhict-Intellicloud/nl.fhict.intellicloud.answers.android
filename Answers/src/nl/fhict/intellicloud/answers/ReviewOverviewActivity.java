package nl.fhict.intellicloud.answers;

import nl.fhict.intellicloud.R;
import nl.fhict.intellicloud.answers.backendcommunication.DummyBackend;
import nl.fhict.intellicloud.answers.backendcommunication.IAnswerService;
import nl.fhict.intellicloud.answers.backendcommunication.IQuestionService;
import nl.fhict.intellicloud.answers.backendcommunication.IReviewService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewOverviewActivity extends Activity {

	int reviewInt;
	
	Answer answer;
	Question question;
	User user1, user2;
	
	EditText etReviewField;
	IReviewService iReviewService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_overview);
		reviewInt = getIntent().getExtras().getInt("reviewInt");
		
		//IAnswerService iAnswerService = new DummyBackend();
		IQuestionService iQuestionService = new DummyBackend();
		//IReviewService iReviewService = new DummyBackend();
		iReviewService = new DummyBackend();
		
		question = iQuestionService.GetQuestion(reviewInt);
		answer = question.getAnswer();
		
		TextView tvQuestion = (TextView) findViewById(R.id.tvQuestion);
		tvQuestion.setText(question.getQuestion());
		TextView tvQuestionDetail = (TextView) findViewById(R.id.tvQuestionDetail);
		tvQuestionDetail.setText(question.getQuestion());
		
		TextView tvAnswer = (TextView) findViewById(R.id.tvAnswer);
		tvAnswer.setText(answer.getAnswer());
		
		Button btnDeclineAnswer = (Button) findViewById(R.id.btnDeclineAnswer);
		btnDeclineAnswer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(ReviewOverviewActivity.this, AddReviewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		Button btnAcceptAnswer = (Button) findViewById(R.id.btnAcceptAnswer);
		btnAcceptAnswer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(ReviewOverviewActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.answer_reviews, menu);
		return true;
	}
}
