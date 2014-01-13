package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;
import java.util.ArrayList;

import nl.fhict.intellicloud.answers.AnswerState;
import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.AnswersEntry;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.QuestionsEntry;

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

public class AnswerSync {
	private ContentProviderClient contentProviderClient;
	private Context context;
	
	private int idColumn;
	private int localIdColumn;
	private int timestampColumn;
	private int answerColumn;
	private int answerStateColumn;
	private int dateColumn;
	private int answerIdColumn;
	private int answererIdColumn;
	private int isPrivateColumn;
	private int askerIdColumn;
	private int questionStateColumn;
	
	public AnswerSync(Context context, ContentProviderClient contentProviderClient)
	{
		this.context = context;
		this.contentProviderClient = contentProviderClient;
	}
	
	public ArrayList<JSONObject> syncAnswers(JSONArray answersResultArray) throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		
		ArrayList<JSONObject> answersToUpload = new ArrayList<JSONObject>();
		JSONArray answersToAddToDB = new JSONArray();
		
		Uri uri = BackendContentProvider.CONTENT_ANSWERS;
		Cursor answersCursor = null;
		try {
			answersCursor = contentProviderClient.query(uri, null, null, null, null);
			
			idColumn = answersCursor.getColumnIndex(AnswersEntry.COLUMN_BACKEND_ID);
			timestampColumn = answersCursor.getColumnIndex(AnswersEntry.COLUMN_TIMESTAMP);
			answerColumn = answersCursor.getColumnIndex(AnswersEntry.COLUMN_ANSWER);
			answerStateColumn = answersCursor.getColumnIndex(AnswersEntry.COLUMN_ANSWERSTATE);
			dateColumn = answersCursor.getColumnIndex(AnswersEntry.COLUMN_DATE);
			answererIdColumn = answersCursor.getColumnIndex(AnswersEntry.COLUMN_ANSWERER_ID);
			localIdColumn = answersCursor.getColumnIndex(AnswersEntry.COLUMN_ID);
			
			for (int i = 0; i < answersResultArray.length(); i++)
			{
				
				answersCursor.moveToFirst();
				boolean answerFoundInDb = false;
				JSONObject serverAnswer = answersResultArray.getJSONObject(i);
				
				while (!answersCursor.isAfterLast() && !answerFoundInDb) {
				
					if (serverAnswer.getString("Id") == answersCursor.getString(idColumn))
					{
						answerFoundInDb=true;
						//Check if updated
						
						break;
					}
				}
				if (!answerFoundInDb)
				{
					answersToAddToDB.put(serverAnswer);
					
				}
				
				
				answersCursor.moveToNext();
				
			}
			
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		answersCursor.moveToFirst();
		while (!answersCursor.isAfterLast())
		{
			if (answersCursor.getString(idColumn) == null)
			{
				answersToUpload.add(getJsonForCurrentAnswer(answersCursor));
				
				String deleteUri = uri + "/" + answersCursor.getInt(localIdColumn);
				answersCursor.moveToNext();
				contentProviderClient.delete(Uri.parse(deleteUri), null, null);
			}
			else
			{
				answersCursor.moveToNext();
			}
		}
		answersCursor.close();
		
		for (int i = 0; i < answersToAddToDB.length(); i++)
		{
			addAnswerToDb(answersToAddToDB.getJSONObject(i));
		}
		return answersToUpload;
		
	}
	private JSONObject getJsonForCurrentAnswer(Cursor cursor) throws JSONException
	{
		JSONObject jsonAnswer = new JSONObject();
		jsonAnswer.accumulate("questionId", cursor.getInt(idColumn));
		jsonAnswer.accumulate("answer", cursor.getString(answerColumn));
		jsonAnswer.accumulate("answererId", cursor.getInt(answererIdColumn));
		jsonAnswer.accumulate("answerState", cursor.getString(answerStateColumn));
		return jsonAnswer;
	}
	private void addAnswerToDb(JSONObject answer) throws JSONException, RemoteException
	{
		ContentValues values = new ContentValues();
		values.put(AnswersEntry.COLUMN_BACKEND_ID, answer.optString("Id"));
		values.put(AnswersEntry.COLUMN_TIMESTAMP, answer.optString("LastChangedTime"));
		values.put(AnswersEntry.COLUMN_ANSWER, answer.optString("Content"));
		values.put(AnswersEntry.COLUMN_DATE, answer.optString("Date"));
		values.put(AnswersEntry.COLUMN_ANSWERSTATE, answer.optString("answerState"));//TODO
		
		
		JSONObject answerer = answer.optJSONObject("Answerer");
		if (answer != null)
		{
			values.put(AnswersEntry.COLUMN_ANSWERER_ID, answerer.getString("Id"));
		}
				
		contentProviderClient.insert(BackendContentProvider.CONTENT_ANSWERS, values);
	}
}
