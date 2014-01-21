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

	private static final String INTELLICLOUD_BASE_URL = "http://81.204.121.229/intellicloudservice/";
	private static final String INTELLICLOUD_NEW_BASE_URL = "http://81.204.121.229/intellicloudservicenew/";
	private static final String URI_GET_QUESTIONS = INTELLICLOUD_NEW_BASE_URL + "QuestionService.svc/questions?state=0";
	private static final String URI_GET_ANSWERS = INTELLICLOUD_NEW_BASE_URL + "AnswerService.svc/answers?state=0&search=null";
	private static final String URI_POST_ANSWERS = INTELLICLOUD_NEW_BASE_URL + "AnswerService.svc/answers";
	private static final String URI_GET_REVIEWS = INTELLICLOUD_NEW_BASE_URL + "ReviewService.svc/reviews";
	private static final String URI_POST_REVIEWS = INTELLICLOUD_NEW_BASE_URL + "ReviewService.svc/reviews";
	private static final String URI_GET_USERS = INTELLICLOUD_NEW_BASE_URL + "UserService.svc/users?after=2000-01-01T19:20:30";
	private static final String URI_GET_FEEDBACK = INTELLICLOUD_NEW_BASE_URL + "FeedbackService.svc/feedback";
	
	
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
		JSONArray questionResultArray = SyncHelper.performNetworkGetRequest(URI_GET_QUESTIONS, context);
		if (questionResultArray != null)
		{
			//Log.d("ServerAccessor", questionIdArray.toString());
			QuestionsSync questionsSync = new QuestionsSync(context, contentProviderClient);
			questionsSync.syncQuestions(questionResultArray);
		}
	}
	public void syncUsers() throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		JSONArray userResultArray = SyncHelper.performNetworkGetRequest(URI_GET_USERS, context);
		if (userResultArray != null)
		{
			UserSync userSync = new UserSync(context, contentProviderClient);
			userSync.syncUsers(userResultArray);
		}
	}
	public void syncAnswers() throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		JSONArray answerResultArray = SyncHelper.performNetworkGetRequest(URI_GET_ANSWERS, context);
		if (answerResultArray != null)
		{
			AnswerSync answerSync = new AnswerSync(context, contentProviderClient);
			ArrayList<JSONObject> newAnswers = answerSync.syncAnswers(answerResultArray);
			for (JSONObject sendObject : newAnswers)
			{
				SyncHelper.performNetworkPostRequest(URI_POST_ANSWERS, sendObject, context);
			}
		}
		
	}
	public void syncReviews() throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{
		JSONArray answerResultArray = SyncHelper.performNetworkGetRequest(URI_GET_REVIEWS, context);
		
		ReviewSync reviewSync = new ReviewSync(context, contentProviderClient);
		ArrayList<JSONObject> newAnswers = reviewSync.syncReviews(answerResultArray);
		for (JSONObject sendObject : newAnswers)
		{
			SyncHelper.performNetworkPostRequest(URI_POST_REVIEWS, sendObject, context);
		}
		
	}
	
}
