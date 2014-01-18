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
				
				if (SyncHelper.getIdFromURI(serverQuestion.getString("Id")) == (questionsCursor.getInt(idColumn)))
				{
					questionFoundInResult = true;
					questionsToAddToDB.remove(i);
					
					
					if (SyncHelper.isServerObjectNewer(serverQuestion, questionsCursor))
					{
						updateQuestion(serverQuestion, questionsCursor.getInt(localIdColumn));
					}
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
	private ContentValues getQuestionContentValues(JSONObject question)
	{
		ContentValues values = new ContentValues();
		values.put(QuestionsEntry.COLUMN_TIMESTAMP, SyncHelper.getUnixMillisecondsFromJsonDate(question.optString("LastChangedTime")));
		values.put(QuestionsEntry.COLUMN_QUESTION, question.optString("Content"));
		values.put(QuestionsEntry.COLUMN_DATE, SyncHelper.getUnixMillisecondsFromJsonDate(question.optString("CreationTime")));
		
		values.put(QuestionsEntry.COLUMN_TITLE, question.optString("Title"));
		values.put(QuestionsEntry.COLUMN_IS_PRIVATE, question.optString("IsPrivate"));
		values.put(QuestionsEntry.COLUMN_QUESTIONSTATE, getQuestionState(question.optInt("QuestionState")).toString());

		String answer = question.optString("Answer");
		if (answer != null)
		{
			values.put(QuestionsEntry.COLUMN_ANSWER_ID, SyncHelper.getIdFromURI(answer));
		}
		String answerer = question.optString("Answerer");
		if (answer != null)
		{
			values.put(QuestionsEntry.COLUMN_ANSWERER_ID, SyncHelper.getIdFromURI(answerer));
		}
		String asker = question.optString("User");
		if (answer != null)
		{
			values.put(QuestionsEntry.COLUMN_ASKER_ID, SyncHelper.getIdFromURI(asker));
		}
		String backendid = question.optString("Id");
		
		if (backendid != null)
		{
			values.put(QuestionsEntry.COLUMN_BACKEND_ID, SyncHelper.getIdFromURI(backendid));
		}
		return values;

	}
	private void updateQuestion(JSONObject question, int rowId) throws JSONException, RemoteException
	{
		String updateUri = BackendContentProvider.CONTENT_QUESTIONS + "/" + rowId;
		ContentValues values = getQuestionContentValues(question);
		contentProviderClient.update(Uri.parse(updateUri), values, null, null);
	}
	private void addQuestionToDb(JSONObject question) throws JSONException, RemoteException
	{
		ContentValues values = getQuestionContentValues(question);
		contentProviderClient.insert(BackendContentProvider.CONTENT_QUESTIONS, values);
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

