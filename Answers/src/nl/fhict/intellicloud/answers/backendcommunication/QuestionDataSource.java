package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;
import java.util.Date;

import nl.fhict.intellicloud.answers.Question;
import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.User;
import nl.fhict.intellicloud.answers.UserType;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class QuestionDataSource implements IQuestionService {
	private SQLiteDatabase database;
	private LocalStorageSQLiteHelper dbHelper;
	private Context context;
	IAnswerService answerService;
	private final String[] allColumns = { QuestionsEntry.COLUMN_ID, 
									QuestionsEntry.COLUMN_BACKEND_ID,
									QuestionsEntry.COLUMN_ANSWERER_ID,
									QuestionsEntry.COLUMN_ASKER_ID, 
									QuestionsEntry.COLUMN_DATE, 
									QuestionsEntry.COLUMN_QUESTION, 
									QuestionsEntry.COLUMN_QUESTIONSTATE,
									QuestionsEntry.COLUMN_IS_PRIVATE,
									QuestionsEntry.COLUMN_TITLE,
									QuestionsEntry.COLUMN_ANSWER_ID};
	
	
	public QuestionDataSource(Context context) {
		dbHelper = new LocalStorageSQLiteHelper(context);
		this.context = context;
		
	}
	
	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	private void close() {
		dbHelper.close();
	}

	@Override
	public Question GetQuestion(int id) {
		answerService = new AnswerDataSource(context);
		open();
		Question question = null;
		Cursor cursor = database.query(QuestionsEntry.TABLE_NAME, allColumns, QuestionsEntry.COLUMN_BACKEND_ID + " = " + id, null, null, null, null);
		if (cursor.moveToFirst())
		{
			
			question = getNextQuestionFromCursor(cursor);
			
		}
		cursor.close();
		close();
		return question;
	}

	@Override
	public ArrayList<Question> GetQuestions() {
		return GetQuestions(-1);
	}

	@Override
	public ArrayList<Question> GetQuestions(int employeeId) {
		answerService = new AnswerDataSource(context);
		String employeeFilter = null;
		if (employeeId >= 0)
		{
			employeeFilter = QuestionsEntry.COLUMN_ANSWERER_ID + " = " + employeeId;
		}
		ArrayList<Question> filteredQuestions = new ArrayList<Question>();
		open();
		Cursor cursor = database.query(QuestionsEntry.TABLE_NAME, allColumns, employeeFilter, null, null, null, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			filteredQuestions.add(getNextQuestionFromCursor(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		close();
		return filteredQuestions;
	}

	
	private Question getNextQuestionFromCursor(Cursor cursor)
	{
		
		Long unixMilliSeconds = cursor.getLong(4)*1000;
		Question question = new Question(cursor.getInt(1), 
								cursor.getString(5), 
								UserDataSource.GetUser(cursor.getInt(3), database), 
								UserDataSource.GetUser(cursor.getInt(2), database),
								QuestionState.valueOf(cursor.getString(6)), 
								new Date(unixMilliSeconds));
		question.setIsPrivate(cursor.getInt(7) > 0);
		question.setTitle(cursor.getString(8));
		question.setAnswer(answerService.GetAnswer(cursor.getInt(9)));
		return question;
	}

	@Override
	public void UpdateQuestion(Question question) {
		ContentValues values = new ContentValues();
		values.put(QuestionsEntry.COLUMN_QUESTIONSTATE, question.getQuestionState().toString());
		values.put(QuestionsEntry.COLUMN_ANSWER_ID, question.getAnswer().getId());

	
		open();
		database.update(QuestionsEntry.TABLE_NAME, values, QuestionsEntry.COLUMN_BACKEND_ID + " = " + question.getId(), null);
		close();
		
	}

	@Override
	public Question GetQuestionUsingAnswer(int answerId) {
		open();
		Question question = null;
		Cursor cursor = database.query(QuestionsEntry.TABLE_NAME, allColumns, QuestionsEntry.COLUMN_ANSWER_ID + " = " + answerId, null, null, null, null);
		if (cursor.moveToFirst())
		{
			
			question = getNextQuestionFromCursor(cursor);
			
		}
		cursor.close();
		close();
		return question;
	}
	
}
