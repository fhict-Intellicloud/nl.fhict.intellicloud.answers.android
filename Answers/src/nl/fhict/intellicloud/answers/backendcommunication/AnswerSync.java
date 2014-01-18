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

	private int questionIdColumn;

	
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
		questionIdColumn = answersCursor.getColumnIndex(AnswersEntry.COLUMN_QUESTION_ID);
		
		answersCursor.moveToFirst();
		for (int i = 0; i < answersResultArray.length(); i++)
		{
			answersToAddToDB.add(answersResultArray.getJSONObject(i));
		}
		
		while (!answersCursor.isAfterLast()) {
			Log.d("sync", "while");
			JSONObject serverAnswer = null;
	
			
			for (int i = 0; i < answersToAddToDB.size(); i++)
			{
				
				serverAnswer = answersToAddToDB.get(i);
				//Log.d("sync", serverAnswer.toString());
				if (SyncHelper.getIdFromURI(serverAnswer.getString("Id")) == (answersCursor.getInt(idColumn)))
				{
					answersToAddToDB.remove(i);
					if (SyncHelper.isServerObjectNewer(serverAnswer, answersCursor))
					{
						updateAnswer(serverAnswer, answersCursor.getInt(localIdColumn));
					}
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
		int answerState = getIdForAnswerState(AnswerState.valueOf(cursor.getString(answerStateColumn)));
		
		jsonAnswer.accumulate("questionId",  cursor.getInt(questionIdColumn));
		jsonAnswer.accumulate("answer", cursor.getString(answerColumn));
		jsonAnswer.accumulate("answererId", cursor.getInt(answererIdColumn));
		jsonAnswer.accumulate("answerState", answerState);
		Log.d("AnswerSync", jsonAnswer.toString());
		return jsonAnswer;
		
	}
	private ContentValues getAnswerContentValues(JSONObject answer)
	{
		ContentValues values = new ContentValues();
		values.put(AnswersEntry.COLUMN_TIMESTAMP, answer.optString("LastChangedTime"));
		values.put(AnswersEntry.COLUMN_ANSWER, answer.optString("Content"));
		values.put(AnswersEntry.COLUMN_DATE, SyncHelper.getUnixMillisecondsFromJsonDate(answer.optString("Date")));
		values.put(AnswersEntry.COLUMN_ANSWERSTATE, getAnswerStateForId(answer.optInt("answerState")).toString());//TODO
		
		String backendid = answer.optString("Id");
		if (backendid != null)
		{
			values.put(AnswersEntry.COLUMN_BACKEND_ID, SyncHelper.getIdFromURI(backendid));
		}
		String answerer = answer.optString("Answerer");
		if (answerer != null)
		{
			values.put(AnswersEntry.COLUMN_ANSWERER_ID, SyncHelper.getIdFromURI(answerer));
		}
		return values;
	}
	private void updateAnswer(JSONObject answer, int rowId) throws JSONException, RemoteException
	{
		String updateUri = BackendContentProvider.CONTENT_ANSWERS + "/" + rowId;
		ContentValues values = getAnswerContentValues(answer);
		contentProviderClient.update(Uri.parse(updateUri), values, null, null);
	}
	private void addAnswerToDb(JSONObject answer) throws JSONException, RemoteException
	{
		ContentValues values = getAnswerContentValues(answer);	
		contentProviderClient.insert(BackendContentProvider.CONTENT_ANSWERS, values);
	}
	private static int getIdForAnswerState(AnswerState state)
	{
		switch (state) {
		case UnderReview:
			return 0;
		case Ready:
			return 1;
		default:
			return 0;
		}
	}
	private static AnswerState getAnswerStateForId(int id)
	{
		switch (id) {
		case 0:
			return AnswerState.UnderReview;
		case 1:
			return AnswerState.Ready;
		default:
			return AnswerState.UnderReview;
		}
	}
}
