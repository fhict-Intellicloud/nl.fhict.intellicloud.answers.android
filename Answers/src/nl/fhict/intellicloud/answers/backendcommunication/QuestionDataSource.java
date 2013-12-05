package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;

import nl.fhict.intellicloud.answers.Question;
import nl.fhict.intellicloud.answers.User;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudAnswersDbContract.*;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class QuestionDataSource implements IQuestionService {
	private SQLiteDatabase database;
	private LocalStorageSQLiteHelper dbHelper;
	private String[] allColumns = { QuestionsEntry.COLUMN_ID, QuestionsEntry.COLUMN_ANSWERER, QuestionsEntry.COLUMN_ASKER, QuestionsEntry.COLUMN_DATE, QuestionsEntry.COLUMN_QUESTION, QuestionsEntry.COLUMN_QUESTIONSTATE};
	
	public QuestionDataSource(Context context) {
		dbHelper = new LocalStorageSQLiteHelper(context);
	}
	
	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	private void close() {
		dbHelper.close();
	}

	@Override
	public Question GetQuestion(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Question> GetQuestions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Question> GetQuestions(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	private User getUser()
	{
		return null;
	}
}
