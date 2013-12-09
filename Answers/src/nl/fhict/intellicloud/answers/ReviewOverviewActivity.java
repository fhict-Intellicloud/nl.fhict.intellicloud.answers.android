package nl.fhict.intellicloud.answers;

import java.util.Date;

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
import android.widget.TextView;

public class ReviewOverviewActivity extends Activity {

	int answerInt;
	
	Answer answer;
	Question question;
	User user1, user2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_overview);
		answerInt = getIntent().getExtras().getInt("Answer");
		
		IAnswerService iAnswerService = new DummyBackend();
		IQuestionService iQuestionService = new DummyBackend();
		IReviewService iReviewService = new DummyBackend();
		
//		user1 = new User(1, "Remco", "Loeff", "", UserType.Customer);
//		user2 = new User(1, "Wipneus", "Pim", "en", UserType.Customer);
//		question = new Question(1, "Na veel speculaties over het sprookje van Hans en Grietje, "
//		+ "ben ik nu wel heel benieuwd hoe dit afloopt. Dus bij deze de vraag hoe loopt het af?", user1, user2, QuestionState.Open, new Date());
//		
//		answer = new Answer(1,"Hans gaat dood en Grietje niet. Happy End", question, user2, AnswerState.UnderReview);
		
		TextView tvTheQuestion = (TextView) findViewById(R.id.tvTheQuestion);
		tvTheQuestion.setText(iQuestionService.GetQuestion(1).getQuestion());
		TextView tvTheAnswer = (TextView) findViewById(R.id.tvTheAnswer);
		tvTheAnswer.setText(iAnswerService.GetAnswer(1).toString());
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
