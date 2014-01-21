package nl.fhict.intellicloud.answers;

import java.io.IOException;

import org.apache.http.auth.AuthenticationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nl.fhict.intellicloud.answers.backendcommunication.SyncHelper;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;

public class GetUserIdTask extends AsyncTask<String, String, Integer> {
	private static final String INTELLICLOUD_NEW_BASE_URL = "http://81.204.121.229/intellicloudservicenew/";
	private static final String URI_GET_USER = INTELLICLOUD_NEW_BASE_URL + "UserService.svc/users";
	private IUserFoundObserver observer;
	private Context context;
	
	
	public GetUserIdTask(Context context, IUserFoundObserver observer)
	{
		this.context = context;
		this.observer = observer;
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		JSONArray result;
		try {
			result = SyncHelper.performNetworkGetRequest(URI_GET_USER, context);
			JSONObject resultObject = result.getJSONObject(0);
			return SyncHelper.getIdFromURI(resultObject.getString("Id"));
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}

	@Override
	protected void onPostExecute(Integer result) {
		observer.loggedInUserFound(result);
		super.onPostExecute(result);
	}

}
