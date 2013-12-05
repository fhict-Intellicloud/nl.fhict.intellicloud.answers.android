package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import nl.fhict.intellicloud.answers.Answer;
import nl.fhict.intellicloud.answers.AnswerState;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudAnswersDbContract.AnswersEntry;

public class AnswerDataSource implements IAnswerService {
// Database fields
		private SQLiteDatabase database;
		private LocalStorageSQLiteHelper dbHelper;
		private String[] allColumns = { AnswersEntry.COLUMN_ID, AnswersEntry.COLUMN_ANSWER, AnswersEntry.COLUMN_QUESTION, AnswersEntry.COLUMN_ANSWERSTATE };
		
		public AnswerDataSource(Context context) {
			dbHelper = new LocalStorageSQLiteHelper(context);
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
			long insertId = database.insert(AnswersEntry.TABLE_NAME, null,
			    values);
			close();
			
			
		}

		@Override
		public Answer GetAnswer(int id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ArrayList<Answer> GetAnswers() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ArrayList<Answer> GetAnswers(int employeeId,
				AnswerState answerState) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void UpdateAnswer(Answer answer) {
			// TODO Auto-generated method stub
			
		}
}
