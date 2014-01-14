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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nl.fhict.intellicloud.answers.*;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.QuestionsEntry;
import nl.fhict.intellicloud.answers.backendcommunication.oauth.AuthenticationManager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

public class ServerAccessor {
	private final String TAG = "SyncAdapter";
	private static final String AUTHORIZATION_HEADER = "AuthorizationToken";
	private static final String PREFERENCES_NAME = "nl.fhict.intellicloud.answers";
	private static final String PREFERENCES_KEY = "AUTHORIZATON_CODE";
	private static final String PREFERENCES_TOKEN = "AUTHORIZATON_TOKEN";
	
	private static final int HTTP_REQUEST_TIMEOUT_MS = 5 * 1000;
	private static final String INTELLICLOUD_BASE_URL = "http://81.204.121.229/intellicloudservice/";
	private static final String URI_GET_QUESTIONS = INTELLICLOUD_BASE_URL + "QuestionService.svc/questions?state=0";
	private static final String URI_GET_ANSWERS = INTELLICLOUD_BASE_URL + "AnswerService.svc/answers?state=0&search=null";
	private static final String URI_POST_ANSWERS = INTELLICLOUD_BASE_URL + "AnswerService.svc/answers";
	private static final String URI_GET_REVIEWS = INTELLICLOUD_BASE_URL + "ReviewService.svc/reviews";
	private static final String URI_POST_REVIEWS = INTELLICLOUD_BASE_URL + "ReviewService.svc/reviews";
	private static final String URI_GET_USERS = INTELLICLOUD_BASE_URL + "UserService.svc/users?after=null";
	private static final String URI_GET_FEEDBACK = INTELLICLOUD_BASE_URL + "FeedbackService.svc/feedback";
	
	
	Context context;
	AccountManager accountManager;
	Account account;
	ContentProviderClient contentProviderClient;
	
	public ServerAccessor(Context context, AccountManager accountManager, ContentProviderClient contentProviderClient, Account account)
	{
		this.context = context;
		this.accountManager = accountManager;
		this.account = account;
		this.contentProviderClient = contentProviderClient;
		
		
		
		
	}
	public void syncQuestions() throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		JSONArray questionResultArray = performNetworkGetRequest(URI_GET_QUESTIONS);
		if (questionResultArray != null)
		{
			//Log.d("ServerAccessor", questionIdArray.toString());
			QuestionsSync questionsSync = new QuestionsSync(context, contentProviderClient);
			questionsSync.syncQuestions(questionResultArray);
		}
	}
	public void syncUsers() throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		JSONArray userResultArray = performNetworkGetRequest(URI_GET_USERS);
		if (userResultArray != null)
		{
			UserSync userSync = new UserSync(context, contentProviderClient);
			userSync.syncUsers(userResultArray);
		}
	}
	public void syncAnswers() throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		JSONArray answerResultArray = performNetworkGetRequest(URI_GET_ANSWERS);
		if (answerResultArray != null)
		{
			AnswerSync answerSync = new AnswerSync(context, contentProviderClient);
			ArrayList<JSONObject> newAnswers = answerSync.syncAnswers(answerResultArray);
			for (JSONObject sendObject : newAnswers)
			{
				performNetworkPostRequest(URI_POST_ANSWERS, sendObject);
			}
		}
		
	}
	public void syncReviews() throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		JSONArray answerResultArray = performNetworkGetRequest(URI_GET_REVIEWS);
		
		ReviewSync reviewSync = new ReviewSync(context, contentProviderClient);
		ArrayList<JSONObject> newAnswers = reviewSync.syncReviews(answerResultArray);
		for (JSONObject sendObject : newAnswers)
		{
			performNetworkPostRequest(URI_POST_REVIEWS, sendObject);
		}
		
	}
	private JSONArray performNetworkPostRequest(String uri, JSONObject params)
			throws JSONException, ParseException, IOException, AuthenticationException, OperationCanceledException, AuthenticatorException
	{
		final JSONArray serverArray;
		StringEntity entity = new StringEntity(params.toString());
		HttpPost request = new HttpPost(uri);
		request.setEntity(entity);
		request.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
        request.addHeader(AUTHORIZATION_HEADER, getBase64AuthorizationHeader());
        
        
        final HttpResponse resp = getHttpClient().execute(request);
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

	private JSONArray performNetworkGetRequest(String uri) 
			throws JSONException, ParseException, IOException, AuthenticationException, OperationCanceledException, AuthenticatorException
	{
		final JSONArray serverArray;
        
		
		HttpGet request = new HttpGet(uri);
		request.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
	    request.addHeader(AUTHORIZATION_HEADER, getBase64AuthorizationHeader());
		
		//String token = accountManager.blockingGetAuthToken(account, TAG, false);
        
        // Send the updated friends data to the server
        Log.i(TAG, "Syncing to: " + uri);
        
        
        Log.d(TAG, request.getFirstHeader(AUTHORIZATION_HEADER).getValue());
        //post.setEntity(entity);
        final HttpResponse resp = getHttpClient().execute(request);
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
	
	 private String getBase64AuthorizationHeader() {
    	SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
//    	AuthenticationManager authManager = AuthenticationManager.getInstance();
//    	authManager.Initialize(preferences.getString(PREFERENCES_KEY, null));
//    	 String json = String.format(
//                 "{\"issuer\":\"accounts.google.com\",\"access_token\":\"ya29.1.AADtN_WEtaKUWeOTaTGqKMkb-DyssDLSJntWtZK6u0LCmS-8HvBG7If57fXFaqg\"}"); 
    	Log.d(TAG, "authToken = " + preferences.getString(PREFERENCES_KEY, null));
    	
        String json = String.format(
                        "{" +
                        "        \"issuer\":\"accounts.google.com\"," +
                        "        \"access_token\":\"%s\"" +
                        "}", preferences.getString(PREFERENCES_TOKEN, null));
        Log.d("AccountManager", "token = \n" + json);
        byte[] encodedbytes = Base64.encode(json.getBytes(), Base64.DEFAULT);
        
        String encoded = new String(encodedbytes);
       
        encoded = encoded.replace("\n", "");
        Log.d("AccountManager", "encoded = " + encoded);
        return encoded;
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
