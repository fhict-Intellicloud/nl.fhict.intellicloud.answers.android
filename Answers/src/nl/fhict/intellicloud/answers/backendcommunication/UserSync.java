package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;
import java.util.ArrayList;

import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.UserType;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.QuestionsEntry;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.ReviewsEntry;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.UsersEntry;

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

public class UserSync {

	private Context context;
	private int idColumn;
	private int localIdColumn;
	//		private int timestampColumn;
	//		private int questionColumn;
	//		private int dateColumn;
	//		private int titleColumn;
	//		private int answerIdColumn;
	//		private int answererIdColumn;
	//		private int isPrivateColumn;
	//		private int askerIdColumn;
	//		private int questionStateColumn;
	private ContentProviderClient contentProviderClient;

	public UserSync(Context context, ContentProviderClient contentProviderClient)
	{
		this.context = context;
		this.contentProviderClient = contentProviderClient;
	}

	public void syncUsers(JSONArray userResultArray) throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		ArrayList<JSONObject> usersToAddToDB = new ArrayList<JSONObject>();

		Uri uri = BackendContentProvider.CONTENT_USERS;
		Cursor usersCursor;
		
		usersCursor = contentProviderClient.query(uri, null, null, null, null);

		idColumn = usersCursor.getColumnIndex(UsersEntry.COLUMN_BACKEND_ID);
		localIdColumn = usersCursor.getColumnIndex(UsersEntry.COLUMN_ID);
		//				timestampColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_TIMESTAMP);
		
		usersCursor.moveToFirst();
		for (int i = 0; i < userResultArray.length(); i++)
		{
			usersToAddToDB.add(userResultArray.getJSONObject(i));
		}
		
		while (!usersCursor.isAfterLast()) {
	
			JSONObject serverUser = null;
			boolean userFoundInResult = false;
			
			for (int i = 0; i < usersToAddToDB.size(); i++)
			{
				
				serverUser = usersToAddToDB.get(i);

				if (getIdFromURI(serverUser.getString("Id")) == (usersCursor.getInt(idColumn)))
				{
					userFoundInResult = true;
					usersToAddToDB.remove(i);
					break;

				}
			}
			if (!userFoundInResult)
			{
				String deleteUri = uri + "/" + usersCursor.getInt(localIdColumn);
				usersCursor.moveToNext();
				contentProviderClient.delete(Uri.parse(deleteUri), null, null);
				
			}
			usersCursor.moveToNext();
		}

		usersCursor.close();


		for (int i = 0; i < usersToAddToDB.size(); i++)
		{
			addUserToDb(usersToAddToDB.get(i));
		}


		
	}
	private void addUserToDb(JSONObject user) throws JSONException, RemoteException
	{
		ContentValues values = new ContentValues();
		Log.d("user", user.toString());
		values.put(UsersEntry.COLUMN_FIRSTNAME, user.optString("FirstName"));
		values.put(UsersEntry.COLUMN_LASTNAME, user.optString("LastName"));
		values.put(UsersEntry.COLUMN_INFIX, user.optString("Infix"));
		values.put(UsersEntry.COLUMN_TIMESTAMP, DateHelper.getUnixMillisecondsFromJsonDate(user.optString("CreationTime")));
		values.put(UsersEntry.COLUMN_USERTYPE,UserType.Employee.toString());
		
		String backendid = user.optString("Id");
		if (backendid != null)
		{
			values.put(UsersEntry.COLUMN_BACKEND_ID, getIdFromURI(backendid));
		}
		contentProviderClient.insert(BackendContentProvider.CONTENT_USERS, values);
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
}


