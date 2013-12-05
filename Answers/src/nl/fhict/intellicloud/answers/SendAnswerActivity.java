package nl.fhict.intellicloud.answers;

import nl.fhict.intellicloud.R;
import nl.fhict.intellicloud.R.layout;
import nl.fhict.intellicloud.R.menu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class SendAnswerActivity extends Activity {

    int answerInt;

    Answer answer;
    Question question;
    User user1, user2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_answer);

        answerInt = getIntent().getExtras().getInt("Answer");
        user1 = new User(1, "Henk", "Zwalp", "en", UserType.Customer);
        user2 = new User(1, "Bob", "Alicen", "en", UserType.Customer);
        question = new Question(1, "Hoe moet ik een vraag stellen?", user1, user2, QuestionState.Open, new Date());

        TextView tvRequestor = (TextView) findViewById(R.id.tvRequestor);
        tvRequestor.setText(answerInt + question.getAsker().getFirstName() + " " + question.getAsker().getLastName());
        TextView tvTheQuestion = (TextView) findViewById(R.id.tvTheQuestion);
        tvTheQuestion.setText(question.getQuestion());
        Button btnAddAnswer = (Button) findViewById(R.id.btnAddAnswer);

        btnAddAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etAnswer = (EditText) findViewById(R.id.etAnswer);
                answer = new Answer(answerInt,etAnswer.getText().toString(), question, user2, AnswerState.UnderReview);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_answer, menu);
		return true;
	}

}
