package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import nl.fhict.intellicloud.answers.*;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;
import android.net.ParseException;
import android.util.Log;

public class ServerAccessor {
	private final String TAG = "SyncAdapter";
	private static final String AUTHORIZATION_HEADER = "AuthorizationToken";
	private static final int HTTP_REQUEST_TIMEOUT_MS = 5 * 1000;
	private static final String INTELLICLOUD_BASE_URL = "http://81.204.121.229/IntelliCloudService/";
	private static final String URI_GET_QUESTIONS = INTELLICLOUD_BASE_URL + "questions/";
	private static final String URI_GET_ANSWERS = INTELLICLOUD_BASE_URL + "answers/";
	private static final String URI_GET_REVIEWS = INTELLICLOUD_BASE_URL + "reviews/";
	private static final String URI_GET_USERS = INTELLICLOUD_BASE_URL + "users/";
	private static final String URI_GET_FEEDBACK = INTELLICLOUD_BASE_URL + "feedback/";
	
	
	Context context;
	AccountManager accountManager;
	Account account;
	ContentProviderClient contentProviderClient;
	
	public ServerAccessor(Context context, AccountManager accountManager, ContentProviderClient contentProviderClient, Account account)
	{
		this.context = context;
		this.accountManager = accountManager;
		
	}
	public void syncQuestions() throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException
	{
		JSONArray questionIdArray = performNetworkRequest(URI_GET_QUESTIONS, null);
		
		
	}

	private JSONArray performNetworkRequest(String uri, ArrayList<NameValuePair> params) 
			throws JSONException, ParseException, IOException, AuthenticationException, OperationCanceledException, AuthenticatorException
	{
		final JSONArray serverArray;
        
        HttpEntity entity = new UrlEncodedFormEntity(params);
        String token = accountManager.blockingGetAuthToken(account, null, false);
        final HttpPost post = new HttpPost(uri);
        
        // Send the updated friends data to the server
        Log.i(TAG, "Syncing to: " + uri);
        
        post.addHeader(entity.getContentType());
        post.addHeader(AUTHORIZATION_HEADER, token);
        post.setEntity(entity);
        final HttpResponse resp = getHttpClient().execute(post);
        final String response = EntityUtils.toString(resp.getEntity());
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // Our request to the server was successful - so we assume
            // that they accepted all the changes we sent up, and
            // that the response includes the contacts that we need
            // to update on our side...
            serverArray = new JSONArray(response);
            Log.d(TAG, response);
            
        } else {
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                Log.e(TAG, "Authentication exception in sending request");
                throw new AuthenticationException();
            } else {
                Log.e(TAG, "Server error in sending request: " + resp.getStatusLine());
                throw new IOException();
            }
        }

		return serverArray;
	}
	private static HttpClient getHttpClient() {
        HttpClient httpClient = new DefaultHttpClient();
        final HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        return httpClient;
    }
}
