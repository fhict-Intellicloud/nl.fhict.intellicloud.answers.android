package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;
import java.util.Date;

import nl.fhict.intellicloud.answers.Question;
import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.User;
import nl.fhict.intellicloud.answers.UserType;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudAnswersDbContract.*;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class QuestionDataSource implements IQuestionService {
	private SQLiteDatabase database;
	private LocalStorageSQLiteHelper dbHelper;
	private final String[] allColumns = { QuestionsEntry.COLUMN_ID, 
									QuestionsEntry.COLUMN_ANSWERER, 
									QuestionsEntry.COLUMN_ASKER, 
									QuestionsEntry.COLUMN_DATE, 
									QuestionsEntry.COLUMN_QUESTION, 
									QuestionsEntry.COLUMN_QUESTIONSTATE};
	
	
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
		open();
		Question question = null;
		Cursor cursor = database.query(QuestionsEntry.TABLE_NAME, allColumns, QuestionsEntry.COLUMN_ID + " = " + id, null, null, null, null);
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
		String employeeFilter = null;
		if (employeeId >= 0)
		{
			employeeFilter = QuestionsEntry.COLUMN_ANSWERER + " = " + employeeId;
		}
		ArrayList<Question> filteredQuestions = new ArrayList<Question>();
		open();
		Cursor cursor = database.query(QuestionsEntry.TABLE_NAME, allColumns, employeeFilter, null, null, null, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			filteredQuestions.add(getNextQuestionFromCursor(cursor));
			cursor.moveToNext();
		}
		close();
		return filteredQuestions;
	}

	private User getUser(int id)
	{
		User user = null;
		Cursor cursor = database.query(UsersEntry.TABLE_NAME, UsersEntry.ALL_COLUMNS, UsersEntry.COLUMN_ID + " = " + id , null, null, null, null);
		if (cursor.moveToFirst())
		{
			user = new User(cursor.getInt(0), 
					cursor.getString(1), 
					cursor.getString(2), 
					cursor.getString(3), 
					UserType.valueOf(cursor.getString(4)));
		}
		cursor.close();
		
		return user;
	}
	private Question getNextQuestionFromCursor(Cursor cursor)
	{
		Long unixMilliSeconds = cursor.getLong(3)*1000;
		Question question = new Question(cursor.getInt(0), 
								cursor.getString(4), 
								getUser(cursor.getInt(2)), 
								getUser(cursor.getInt(1)),
								QuestionState.valueOf(cursor.getString(5)), 
								new Date(unixMilliSeconds));
		return question;
	}
}
