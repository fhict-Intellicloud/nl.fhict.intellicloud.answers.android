package nl.fhict.intellicloud.answers;

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
		getActionBar().setDisplayHomeAsUpEnabled(true);

        qService = new DummyBackend();
        aService = new DummyBackend();

        questionInt = getIntent().getExtras().getInt("questionInt");
        question = qService.GetQuestion(questionInt);


        TextView tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvQuestion.setText(question.getQuestion());
        TextView tvQuestionDetail = (TextView) findViewById(R.id.tvQuestionDetail);
        tvQuestionDetail.setText(question.getQuestion());

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
            public void onClick(View v) {
                askFeedback(); onBackPressed();

            }
        });
	}

    /**
     * Sends the answer to the backend to be added to the database
     */
    protected void addAnswer(){
        String answerText = etAnswer.getText().toString();
        
        answer = new Answer(answerText, question.getAnwserer(), AnswerState.UnderReview);
        aService.CreateAnswer(answer, questionInt);
    }
    
    protected void askFeedback(){
    	//TODO
        String answerText = etAnswer.getText().toString();
        answer = new Answer(answerText, question.getAnwserer(), AnswerState.UnderReview);
        question = new Question(question.getId(), question.getQuestion(), question.getAsker(), null, QuestionState.UpForFeedback, null);
        qService.UpdateQuestion(question);
        aService.CreateAnswer(answer, questionInt);
    }
}
