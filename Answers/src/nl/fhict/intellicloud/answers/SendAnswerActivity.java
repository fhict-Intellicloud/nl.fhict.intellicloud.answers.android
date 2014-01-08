package nl.fhict.intellicloud.answers;

import nl.fhict.intellicloud.R;
import nl.fhict.intellicloud.R.layout;
import nl.fhict.intellicloud.R.menu;
import nl.fhict.intellicloud.answers.backendcommunication.DummyBackend;
import nl.fhict.intellicloud.answers.backendcommunication.IAnswerService;
import nl.fhict.intellicloud.answers.backendcommunication.IQuestionService;

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

    IQuestionService qService;
    IAnswerService aService;

    int questionInt;

    Answer answer;
    Question question;
    EditText etAnswer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_answer);

        qService = new DummyBackend();
        aService = new DummyBackend();

        questionInt = getIntent().getExtras().getInt("questionInt");
        question = qService.GetQuestion(questionInt);

        TextView tvRequestor = (TextView) findViewById(R.id.tvRequestor);
        tvRequestor.setText(questionInt + question.getAsker().getFirstName() + " " + question.getAsker().getLastName());
        TextView tvTheQuestion = (TextView) findViewById(R.id.tvTheQuestion);
        tvTheQuestion.setText(question.getQuestion());
        Button btnAddAnswer = (Button) findViewById(R.id.btnAddAnswer);
        Button btnRequestReview = (Button) findViewById(R.id.btnRequestReview);
        etAnswer = (EditText) findViewById(R.id.etAnswer);

        btnAddAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnswer(); onBackPressed();
            }
        });
        btnRequestReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { addAnswer(); onBackPressed();
            }
        });
	}

    /**
     * Sends the answer to the backend to be added to the database
     */
    public void addAnswer(){
        String answerText = etAnswer.getText().toString();
        
        answer = new Answer(answerText, question.getAnwserer(), AnswerState.UnderReview);
        aService.CreateAnswer(answer, questionInt);
    }
}
