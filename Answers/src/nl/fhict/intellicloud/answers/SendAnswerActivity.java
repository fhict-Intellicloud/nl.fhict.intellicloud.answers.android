package nl.fhict.intellicloud.answers;

import nl.fhict.intellicloud.answers.backendcommunication.AnswerDataSource;
import nl.fhict.intellicloud.answers.backendcommunication.IAnswerService;
import nl.fhict.intellicloud.answers.backendcommunication.IQuestionService;
import nl.fhict.intellicloud.answers.backendcommunication.IUserService;
import nl.fhict.intellicloud.answers.backendcommunication.QuestionDataSource;
import nl.fhict.intellicloud.answers.backendcommunication.UserDataSource;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SendAnswerActivity extends Activity {
	private final String PREFERENCES_NAME = "nl.fhict.intellicloud.answers";
	private final String PREFERENCES_USER_DB_ID = "USER_ID";
    IQuestionService qService;
    IAnswerService aService;
    IUserService uService;

    int questionInt;

    Answer answer;
    Question question;
    EditText etAnswer;
    
    private User getAnswerer()
    {
    	uService = new UserDataSource(this);
    	SharedPreferences prefs = this.getSharedPreferences(PREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
    	int id = prefs.getInt(PREFERENCES_USER_DB_ID, -1);
    	if (id > -1)
    	{
    		return uService.GetUser(id);
    	}
    	return null;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_answer);
		getActionBar().setDisplayHomeAsUpEnabled(true);

        qService = new QuestionDataSource(this);
        aService = new AnswerDataSource(this);

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
        
        answer = new Answer(answerText, getAnswerer(), AnswerState.UnderReview);
        aService.CreateAnswer(answer, questionInt);
    }
    
    protected void askFeedback(){
    	//TODO
        String answerText = etAnswer.getText().toString();
        answer = new Answer(answerText,  getAnswerer(), AnswerState.UnderReview);
        question = new Question(question.getId(), question.getQuestion(), question.getAsker(), getAnswerer(), QuestionState.UpForFeedback, null);
        question.setAnswer(answer);
        qService.UpdateQuestion(question);
        aService.CreateAnswer(answer, questionInt);
    }
}
