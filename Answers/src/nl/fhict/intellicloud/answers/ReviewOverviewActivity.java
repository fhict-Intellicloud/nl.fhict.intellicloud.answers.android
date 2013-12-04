package nl.fhict.intellicloud.answers;

import java.util.Date;

import nl.fhict.intellicloud.R;
import nl.fhict.intellicloud.R.layout;
import nl.fhict.intellicloud.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
		
		user1 = new User(1, "Remco", "Loeff", "", UserType.Customer);
		user2 = new User(1, "Wipneus", "Pim", "en", UserType.Customer);
		question = new Question(1, "Hoe loopt het sprooje af van Hans & Grietje?", user1, user2, QuestionState.Open, new Date());
		
		answer = new Answer(1,"Hans gaat dood en Grietje niet. Happy End", question, user2, AnswerState.UnderReview);
		
		TextView tvRequestor = (TextView) findViewById(R.id.tvRequestor);
		tvRequestor.setText(answerInt + answer.getAnswerer().getFirstName() + " " + answer.getAnswerer().getLastName());
		TextView tvTheQuestion = (TextView) findViewById(R.id.tvTheQuestion);
		tvTheQuestion.setText(answer.getQuestion().getQuestion());
		TextView tvTheAnswer = (TextView) findViewById(R.id.tvTheAnswer);
		tvTheAnswer.setText(answer.getAnswer());
		tvTheAnswer.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReviewOverviewActivity.this, OriginalAnswerActivity.class);
				startActivity(intent);
			}
		});
		
		Button btnAddReview = (Button) findViewById(R.id.btnAddNewReview);
		btnAddReview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
