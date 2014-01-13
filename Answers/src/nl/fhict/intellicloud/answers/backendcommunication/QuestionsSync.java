package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;
import java.util.ArrayList;

import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.QuestionsEntry;

import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.RemoteException;

public class QuestionsSync {
	private Context context;
	private int idColumn;
//	private int timestampColumn;
//	private int questionColumn;
//	private int dateColumn;
//	private int titleColumn;
//	private int answerIdColumn;
//	private int answererIdColumn;
//	private int isPrivateColumn;
//	private int askerIdColumn;
//	private int questionStateColumn;
	private ContentProviderClient contentProviderClient;
	
	public QuestionsSync(Context context, ContentProviderClient contentProviderClient)
	{
		this.context = context;
		this.contentProviderClient = contentProviderClient;
	}
	
	public void syncQuestions(JSONArray questionResultArray) throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		
		//JSONArray questionsToUpload = new JSONArray();
		JSONArray questionsToAddToDB = new JSONArray();
		
		Uri uri = BackendContentProvider.CONTENT_QUESTIONS;
		Cursor questionsCursor;
		try {
			questionsCursor = contentProviderClient.query(uri, null, null, null, null);
		
		
		
			
			
			idColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_BACKEND_ID);
//			timestampColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_TIMESTAMP);
//			questionColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_QUESTION);
//			dateColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_DATE);
//			titleColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_TITLE);
//			answerIdColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_ANSWER_ID);
//			answererIdColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_ANSWERER_ID);
//			isPrivateColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_IS_PRIVATE);
//			askerIdColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_ASKER_ID);
//			questionStateColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_QUESTIONSTATE);
			
			
			
			for (int i = 0; i < questionResultArray.length(); i++)
			{
				
				questionsCursor.moveToFirst();
				boolean questionFoundInDb = false;
				JSONObject serverQuestion = questionResultArray.getJSONObject(i);
				
				while (!questionsCursor.isAfterLast() && !questionFoundInDb) {
				
					if (serverQuestion.getString("Id") == questionsCursor.getString(idColumn))
					{
						questionFoundInDb=true;
						//Check if updated
						
						break;
					}
				}
				if (!questionFoundInDb)
				{
					questionsToAddToDB.put(serverQuestion);
					
				}
				
				
				questionsCursor.moveToNext();
				
			}
			questionsCursor.close();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < questionsToAddToDB.length(); i++)
		{
			addQuestionToDb(questionsToAddToDB.getJSONObject(i));
		}
		
		
		
	}
	private void addQuestionToDb(JSONObject question) throws JSONException, RemoteException
	{
		ContentValues values = new ContentValues();
		values.put(QuestionsEntry.COLUMN_BACKEND_ID, question.optString("Id"));
		values.put(QuestionsEntry.COLUMN_TIMESTAMP, question.optString("LastChangedTime"));
		values.put(QuestionsEntry.COLUMN_QUESTION, question.optString("Content"));
		values.put(QuestionsEntry.COLUMN_DATE, question.optString("Date"));
		values.put(QuestionsEntry.COLUMN_TITLE, question.optString("Title"));
		values.put(QuestionsEntry.COLUMN_IS_PRIVATE, question.optString("IsPrivate"));
		values.put(QuestionsEntry.COLUMN_QUESTIONSTATE, QuestionState.Open.toString()); //No questionState?
		
		JSONObject answer = question.optJSONObject("Answer");
		if (answer != null)
		{
			values.put(QuestionsEntry.COLUMN_ANSWER_ID, answer.getString("Id"));
		}
		JSONObject answerer = question.optJSONObject("Answerer");
		if (answer != null)
		{
			values.put(QuestionsEntry.COLUMN_ANSWERER_ID, answerer.getString("Id"));
		}
		JSONObject asker = question.optJSONObject("Answer");
		if (answer != null)
		{
			values.put(QuestionsEntry.COLUMN_ASKER_ID, asker.getString("Id"));
		}
		
		
		contentProviderClient.insert(BackendContentProvider.CONTENT_QUESTIONS, values);
	}
}
