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
import android.util.Log;

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
		ArrayList<JSONObject> answersToAddToDB = new ArrayList<JSONObject>();
		
		Uri uri = BackendContentProvider.CONTENT_ANSWERS;
		Cursor answersCursor = null;
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
			answersToAddToDB.add(answersResultArray.getJSONObject(i));
		}
		
		while (!answersCursor.isAfterLast()) {
			Log.d("sync", "while");
			JSONObject serverAnswer = null;
	
			
			for (int i = 0; i < answersResultArray.length(); i++)
			{
				
				serverAnswer = answersResultArray.getJSONObject(i);
				Log.d("sync", serverAnswer.toString());
				if (getIdFromURI(serverAnswer.getString("Id")) == (answersCursor.getInt(idColumn)))
				{
					answersToAddToDB.remove(i);
					break;

				}
			}
			
			answersCursor.moveToNext();
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
		
		for (int i = 0; i < answersToAddToDB.size(); i++)
		{
			addAnswerToDb(answersToAddToDB.get(i));
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
		Log.d("AnswerSync", jsonAnswer.toString());
		return jsonAnswer;
		
	}
	private void addAnswerToDb(JSONObject answer) throws JSONException, RemoteException
	{
		Log.d("AnswerSync", answer.toString());
		ContentValues values = new ContentValues();
		values.put(AnswersEntry.COLUMN_TIMESTAMP, answer.optString("LastChangedTime"));
		values.put(AnswersEntry.COLUMN_ANSWER, answer.optString("Content"));
		values.put(AnswersEntry.COLUMN_DATE, answer.optString("Date"));
		values.put(AnswersEntry.COLUMN_ANSWERSTATE, answer.optString("answerState"));//TODO
		
		String backendid = answer.optString("Id");
		if (backendid != null)
		{
			values.put(AnswersEntry.COLUMN_BACKEND_ID, getIdFromURI(backendid));
		}
		String answerer = answer.optString("Answerer");
		if (answerer != null)
		{
			values.put(AnswersEntry.COLUMN_ANSWERER_ID, getIdFromURI(answerer));
		}
				
		contentProviderClient.insert(BackendContentProvider.CONTENT_ANSWERS, values);
		
		
	}
	private int getIdFromURI(String uri)
	{
		String[] uriparts = uri.split("/");
		for (int i = 0; i < uriparts.length; i++)
		{
			int result = Integer.getInteger(uriparts[i], -1);
			if (result != -1)
			{
				return result;
			}
		}
		return -1;
	}

	
}
