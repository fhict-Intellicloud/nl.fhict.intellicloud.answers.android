package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import nl.fhict.intellicloud.answers.Answer;
import nl.fhict.intellicloud.answers.AnswerState;
import nl.fhict.intellicloud.answers.Question;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.*;


public class AnswerDataSource implements IAnswerService {
// Database fields
		private SQLiteDatabase database;
		private LocalStorageSQLiteHelper dbHelper;
		private String[] allColumns = { AnswersEntry.COLUMN_ID, 
										AnswersEntry.COLUMN_BACKEND_ID,
										AnswersEntry.COLUMN_ANSWER, 
										AnswersEntry.COLUMN_ANSWERER_ID,
										AnswersEntry.COLUMN_ANSWERSTATE,
										AnswersEntry.COLUMN_DATE,
										AnswersEntry.COLUMN_QUESTION_ID};
		
		
		
		
		public AnswerDataSource(Context context) {
			dbHelper = new LocalStorageSQLiteHelper(context);
			//
		}
		
		private void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}
		
		private void close() {
			dbHelper.close();
		}

		@Override
		public void CreateAnswer(Answer answer, int questionId) {
			ContentValues values = new ContentValues();
			Date currentDate = new Date();
			values.put(AnswersEntry.COLUMN_ANSWER, answer.getAnswer());
			values.put(AnswersEntry.COLUMN_ANSWERSTATE, answer.getAnwserState().toString());
			if (answer.getAnswerer() != null)
			{
				values.put(AnswersEntry.COLUMN_ANSWERER_ID, answer.getAnswerer().getId());
			}
			values.put(AnswersEntry.COLUMN_DATE, currentDate.getTime());
			values.put(AnswersEntry.COLUMN_QUESTION_ID, questionId);
			
			open();
			database.insert(AnswersEntry.TABLE_NAME, null,
			    values);
			
			close();

		}

		@Override
		public Answer GetAnswer(int id) {
			open();
			Answer answer = null;
			Cursor cursor = database.query(AnswersEntry.TABLE_NAME, allColumns, AnswersEntry.COLUMN_BACKEND_ID + " = " + id, null, null, null, null);
			if (cursor.moveToFirst())
			{
				
				answer = getNextAnswerFromCursor(cursor);
				
			}
			cursor.close();
			close();
			return answer;
		}
		
		@Override
		public Answer GetAnswerUsingQuestion(int questionId) {
			open();
			Answer answer = null;
			Cursor cursor = database.query(AnswersEntry.TABLE_NAME, allColumns, AnswersEntry.COLUMN_QUESTION_ID + " = " + questionId, null, null, null, null);
			if (cursor.moveToFirst())
			{
				
				answer = getNextAnswerFromCursor(cursor);
				
			}
			cursor.close();
			close();
			return answer;
			
		}
		@Override
		public ArrayList<Answer> GetAnswers() {
			return GetAnswers(-1, null);
		}

		@Override
		public ArrayList<Answer> GetAnswers(int employeeId, AnswerState answerState) {
		
			String answerFilter = null;
		
			if (answerState != null)
			{
				answerFilter += AnswersEntry.COLUMN_ANSWERSTATE + " = " + answerState.toString();
			}
			if (employeeId >= 0)
			{
				if (answerFilter.length() > 0)
				{
					answerFilter += " AND ";
				}
				answerFilter += AnswersEntry.COLUMN_ANSWERER_ID + " = " + employeeId;
			}
			ArrayList<Answer> filteredAnswers = new ArrayList<Answer>();
			
			open();
			Cursor cursor = database.query(AnswersEntry.TABLE_NAME, allColumns, answerFilter, null, null, null, null);
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				filteredAnswers.add(getNextAnswerFromCursor(cursor));		
				cursor.moveToNext();
			}
			cursor.close();
			close();
			return filteredAnswers;
			
			
		}

		@Override
		public void UpdateAnswer(Answer answer) {
			
			ContentValues values = new ContentValues();
			values.put(AnswersEntry.COLUMN_ANSWERSTATE, answer.getAnwserState().toString());
			values.put(AnswersEntry.COLUMN_ANSWERER_ID, answer.getAnswerer().getId());
			values.put(AnswersEntry.COLUMN_ANSWER, answer.getAnswer());
		
			open();
			database.update(AnswersEntry.TABLE_NAME, values, AnswersEntry.COLUMN_BACKEND_ID + " = " + answer.getId(), null);
			close();
			
		}
		private Answer getNextAnswerFromCursor(Cursor cursor)
		{
			//Question questionForAnswer = questionDataSource.GetQuestion(cursor.getInt(2));
			
			Answer foundAnswer = new Answer(cursor.getString(2), 
											UserDataSource.GetUser(cursor.getInt(3), database), 
											AnswerState.valueOf(cursor.getString(4)));
			foundAnswer.setId(cursor.getInt(1));
			
			
			return foundAnswer;
		}

}
