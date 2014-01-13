package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;

import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.QuestionsEntry;
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

public class UserSync {

	private Context context;
	private int idColumn;
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
		JSONArray usersToAddToDB = new JSONArray();

		Uri uri = BackendContentProvider.CONTENT_USERS;
		Cursor usersCursor;
		try {
			usersCursor = contentProviderClient.query(uri, null, null, null, null);





			idColumn = usersCursor.getColumnIndex(UsersEntry.COLUMN_BACKEND_ID);
			//				timestampColumn = questionsCursor.getColumnIndex(QuestionsEntry.COLUMN_TIMESTAMP);

			for (int i = 0; i < userResultArray.length(); i++)
			{

				usersCursor.moveToFirst();
				boolean userFoundInDb = false;
				JSONObject serverUser = userResultArray.getJSONObject(i);

				while (!usersCursor.isAfterLast() && !userFoundInDb) {

					if (serverUser.getString("Id") == usersCursor.getString(idColumn))
					{
						userFoundInDb=true;
						//Check if updated

						break;
					}
				}
				if (!userFoundInDb)
				{
					usersToAddToDB.put(serverUser);
				}
				usersCursor.moveToNext();

			}
			usersCursor.close();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < usersToAddToDB.length(); i++)
		{
			addUserToDb(usersToAddToDB.getJSONObject(i));
		}



	}
	private void addUserToDb(JSONObject user) throws JSONException, RemoteException
	{
		ContentValues values = new ContentValues();
		values.put(UsersEntry.COLUMN_BACKEND_ID, user.optString("Id"));
		values.put(UsersEntry.COLUMN_FIRSTNAME, user.optString("FirstName"));
		values.put(UsersEntry.COLUMN_LASTNAME, user.optString("LastName"));
		values.put(UsersEntry.COLUMN_INFIX, user.optString("Infix"));
		values.put(UsersEntry.COLUMN_TIMESTAMP, user.optString("CreationTime"));
		values.put(UsersEntry.COLUMN_USERTYPE, user.optString("UserType"));
		

		contentProviderClient.insert(BackendContentProvider.CONTENT_QUESTIONS, values);
	}
}


