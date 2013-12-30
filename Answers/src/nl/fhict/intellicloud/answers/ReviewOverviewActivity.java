package nl.fhict.intellicloud.answers;


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
import android.widget.ListView;
import android.widget.TextView;

public class ReviewOverviewActivity extends Activity {

	int reviewInt;
	
	Answer answer;
	Question question;
	User user1, user2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_overview);
		reviewInt = getIntent().getExtras().getInt("reviewInt");
		
		//IAnswerService iAnswerService = new DummyBackend();
		IQuestionService iQuestionService = new DummyBackend();
		//IReviewService iReviewService = new DummyBackend();
		
		question = iQuestionService.GetQuestion(reviewInt);
		answer = question.getAnswer();
		
		TextView tvTheQuestion = (TextView) findViewById(R.id.tvTheQuestion);
		tvTheQuestion.setText(question.getQuestion());
		TextView tvTheAnswer = (TextView) findViewById(R.id.tvTheAnswer);
		tvTheAnswer.setText(answer.getAnswer());
		tvTheAnswer.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReviewOverviewActivity.this, OriginalAnswerActivity.class);
				startActivity(intent);
			}
		});
		
		TextView tvAccepAnswer = (TextView) findViewById(R.id.tvAcceptAnswer);
		tvAccepAnswer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//TODO
				Intent intent = new Intent(ReviewOverviewActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		TextView tvDeclineAnswer = (TextView) findViewById(R.id.tvDeclineAnswer);
		tvDeclineAnswer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//TODO
				Intent intent = new Intent(ReviewOverviewActivity.this, AddReviewActivity.class);
				startActivity(intent);
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
