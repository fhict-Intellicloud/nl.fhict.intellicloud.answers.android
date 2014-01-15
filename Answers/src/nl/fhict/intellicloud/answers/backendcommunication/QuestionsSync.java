package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;
import java.util.ArrayList;

import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.AnswersEntry;
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
import android.util.Log;

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
		ArrayList<JSONObject> questionsToAddToDB = new ArrayList<JSONObject>();
		//Log.d("sync", questionResultArray.toString());
		Uri uri = BackendContentProvider.CONTENT_QUESTIONS;
		
		Cursor questionsCursor;
		
		questionsCursor = contentProviderClient.query(uri, null, null, null, null);
		Log.d("sync", "uri");
		idColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_BACKEND_ID);
		int localIdColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_BACKEND_ID);
		//			timestampColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_TIMESTAMP);
		//			questionColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_QUESTION);
		//			dateColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_DATE);
		//			titleColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_TITLE);
		//			answerIdColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_ANSWER_ID);
		//			answererIdColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_ANSWERER_ID);
		//			isPrivateColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_IS_PRIVATE);
		//			askerIdColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_ASKER_ID);
		//			questionStateColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_QUESTIONSTATE);

		questionsCursor.moveToFirst();
		
		for (int i = 0; i < questionResultArray.length(); i++)
		{
			questionsToAddToDB.add(questionResultArray.getJSONObject(i));
		}
		
		while (!questionsCursor.isAfterLast()) {
			
			JSONObject serverQuestion = null;
			boolean questionFoundInResult = false;

			for (int i = 0; i < questionsToAddToDB.size(); i++)
			{
				
				serverQuestion = questionsToAddToDB.get(i);
				
				if (getIdFromURI(serverQuestion.getString("Id")) == (questionsCursor.getInt(idColumn)))
				{
					questionFoundInResult = true;
					questionsToAddToDB.remove(i);
					break;

				}
			}
			if (!questionFoundInResult)
			{
				String deleteUri = uri + "/" + questionsCursor.getInt(localIdColumn);
				questionsCursor.moveToNext();
				contentProviderClient.delete(Uri.parse(deleteUri), null, null);
				
			}
			questionsCursor.moveToNext();
		}

		questionsCursor.close();


		for (int i = 0; i < questionsToAddToDB.size(); i++)
		{
			addQuestionToDb(questionsToAddToDB.get(i));
		}



	}
	private void addQuestionToDb(JSONObject question) throws JSONException, RemoteException
	{

		ContentValues values = new ContentValues();
		values.put(QuestionsEntry.COLUMN_TIMESTAMP, question.optString("LastChangedTime"));
		values.put(QuestionsEntry.COLUMN_QUESTION, question.optString("Content"));
		values.put(QuestionsEntry.COLUMN_DATE, DateHelper.getUnixMillisecondsFromJsonDate(question.optString("CreationTime")));
		
		values.put(QuestionsEntry.COLUMN_TITLE, question.optString("Title"));
		values.put(QuestionsEntry.COLUMN_IS_PRIVATE, question.optString("IsPrivate"));
		values.put(QuestionsEntry.COLUMN_QUESTIONSTATE, getQuestionState(question.optInt("QuestionState")).toString());

		String answer = question.optString("Answer");
		if (answer != null)
		{
			values.put(QuestionsEntry.COLUMN_ANSWER_ID, getIdFromURI(answer));
		}
		String answerer = question.optString("Answerer");
		if (answer != null)
		{
			values.put(QuestionsEntry.COLUMN_ANSWERER_ID, getIdFromURI(answerer));
		}
		String asker = question.optString("User");
		if (answer != null)
		{
			values.put(QuestionsEntry.COLUMN_ASKER_ID, getIdFromURI(asker));
		}
		String backendid = question.optString("Id");
		
		if (backendid != null)
		{
			values.put(QuestionsEntry.COLUMN_BACKEND_ID, getIdFromURI(backendid));
		}


		contentProviderClient.insert(BackendContentProvider.CONTENT_QUESTIONS, values);
	}
	private int getIdFromURI(String uri)
	{
		String[] uriparts = uri.split("/");
		for (int i = 0; i < uriparts.length; i++)
		{
			if(uriparts[i].matches("-?\\d+"))//Regex that checks if the string is a number
			{
				int result = Integer.parseInt(uriparts[i]);
				return result;
			}

		}
		return -1;
	}
	private QuestionState getQuestionState(int serverState)
	{
		QuestionState foundState = QuestionState.Open;
		switch (serverState) {
		case 0:
			foundState = QuestionState.Open;
			break;
		case 1:
			foundState = QuestionState.UpForAnswer;
			break;
		case 2:
			foundState = QuestionState.UpForFeedback;
			break;
		case 3:
			foundState = QuestionState.Closed;
			break;
		default:
			break;
		}
		return foundState;
	}
}

