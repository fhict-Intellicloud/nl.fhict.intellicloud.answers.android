package nl.fhict.intellicloud.answers;


import nl.fhict.intellicloud.answers.backendcommunication.DummyBackend;
import nl.fhict.intellicloud.answers.backendcommunication.IAnswerService;

import java.util.List;

import nl.fhict.intellicloud.answers.backendcommunication.AnswerDataSource;
import nl.fhict.intellicloud.answers.backendcommunication.IQuestionService;
import nl.fhict.intellicloud.answers.backendcommunication.IReviewService;
import nl.fhict.intellicloud.answers.backendcommunication.QuestionDataSource;
import nl.fhict.intellicloud.answers.backendcommunication.ReviewDataSource;
import android.app.Activity;
import android.content.Context;
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
	Context context;
	
	Answer answer;
	Question question;
	User user1, user2;
	
	EditText etReviewField;
	IReviewService iReviewService;
	IAnswerService iAnswerService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_overview);
		reviewInt = getIntent().getExtras().getInt("reviewInt");
		this.context = this;

		IQuestionService iQuestionService = new QuestionDataSource(getApplicationContext());
		final IReviewService iReviewService = new ReviewDataSource(getApplicationContext());
		iAnswerService = new AnswerDataSource(getApplicationContext());
		question = iQuestionService.GetQuestion(reviewInt);
		answer = iAnswerService.GetAnswerUsingQuestion(reviewInt);
		
		TextView tvQuestion = (TextView) findViewById(R.id.tvQuestion);
		tvQuestion.setText(question.getQuestion());
		TextView tvQuestionDetail = (TextView) findViewById(R.id.tvQuestionDetail);
		tvQuestionDetail.setText(question.getQuestion());
		
		TextView tvAnswer = (TextView) findViewById(R.id.tvAnswer);
		if (answer != null)
		{
			tvAnswer.setText(answer.getAnswer());
		}
		
		Button btnDeclineAnswer = (Button) findViewById(R.id.btnDeclineAnswer);
		btnDeclineAnswer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(context, AddReviewActivity.class);
				
				ListView lvReviews = (ListView) findViewById(R.id.lvReviews);
				List<Review> reviews = iReviewService.GetReviews(answer.getId());
				if(reviews != null && reviews.size() > 0){
					//TODO: ADD Reviews in list
					//lvReviews.setAdapter();					
				}
				
				intent.putExtra("reviewInt", reviewInt);
				startActivity(intent);
				finish();
			}
		});
		
		Button btnAcceptAnswer = (Button) findViewById(R.id.btnAcceptAnswer);
		btnAcceptAnswer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				answer.setAnswerState(AnswerState.Ready);
				iAnswerService.UpdateAnswer(answer);
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
