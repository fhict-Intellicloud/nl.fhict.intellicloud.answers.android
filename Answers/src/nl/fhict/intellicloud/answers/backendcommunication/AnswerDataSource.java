package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import nl.fhict.intellicloud.answers.Answer;
import nl.fhict.intellicloud.answers.AnswerState;
import nl.fhict.intellicloud.answers.Question;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudAnswersDbContract.*;


public class AnswerDataSource implements IAnswerService {
// Database fields
		private SQLiteDatabase database;
		private LocalStorageSQLiteHelper dbHelper;
		private String[] allColumns = { AnswersEntry.COLUMN_ID, 
										AnswersEntry.COLUMN_ANSWER, 
										AnswersEntry.COLUMN_QUESTION, 
										AnswersEntry.COLUMN_ANSWERSTATE };
		
		private IQuestionService questionDataSource = null;
		
		
		public AnswerDataSource(Context context) {
			dbHelper = new LocalStorageSQLiteHelper(context);
			questionDataSource = new QuestionDataSource(context);
		}
		
		private void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}
		
		private void close() {
			dbHelper.close();
		}

		@Override
		public void CreateAnswer(Answer answer) {
			ContentValues values = new ContentValues();
			
			values.put(AnswersEntry.COLUMN_ANSWER, answer.getAnswer());
			values.put(AnswersEntry.COLUMN_ANSWERSTATE, answer.getAnwserState().toString());
			values.put(AnswersEntry.COLUMN_QUESTION, answer.getQuestion().getId());
			
			open();
			database.insert(AnswersEntry.TABLE_NAME, null,
			    values);
			close();
			
			
		}

		@Override
		public Answer GetAnswer(int id) {
			open();
			Answer answer = null;
			Cursor cursor = database.query(AnswersEntry.TABLE_NAME, allColumns, AnswersEntry.COLUMN_ID + " = " + id, null, null, null, null);
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
		
			String answerStateFilter = null;
			
			if (answerState != null)
			{
				answerStateFilter = AnswersEntry.COLUMN_ANSWERSTATE + " = " + answerState.toString();
			}
			ArrayList<Answer> filteredAnswers = new ArrayList<Answer>();
			
			open();
			Cursor cursor = database.query(AnswersEntry.TABLE_NAME, allColumns, answerStateFilter, null, null, null, null);
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				Answer answer = getNextAnswerFromCursor(cursor);
				if (employeeId < 0 || answer.getAnswerer().getId() == employeeId)
				{
					filteredAnswers.add(getNextAnswerFromCursor(cursor));
				}
				cursor.moveToNext();
			}
			close();
			return filteredAnswers;
			
			
		}

		@Override
		public void UpdateAnswer(Answer answer) {
			
			ContentValues values = new ContentValues();
			values.put(AnswersEntry.COLUMN_ANSWERSTATE, answer.getAnwserState().toString());
		
			open();
			database.update(AnswersEntry.TABLE_NAME, values, AnswersEntry.COLUMN_ID + " = " + answer.getId(), null);
			close();
			
		}
		private Answer getNextAnswerFromCursor(Cursor cursor)
		{
			Question questionForAnswer = questionDataSource.GetQuestion(cursor.getInt(2));
			
			Answer foundAnswer = new Answer(cursor.getInt(0), 
											cursor.getString(1), 
											questionForAnswer, 
											questionForAnswer.getAnwserer(), 
											AnswerState.valueOf(cursor.getString(3)));			
			
			return foundAnswer;
		}
}