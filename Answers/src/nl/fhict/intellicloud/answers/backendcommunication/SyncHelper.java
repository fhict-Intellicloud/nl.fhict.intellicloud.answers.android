package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;

import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.QuestionsEntry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
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

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

public class SyncHelper {
	private static final String AUTHORIZATION_HEADER = "AuthorizationToken";
	private static final String PREFERENCES_NAME = "nl.fhict.intellicloud.answers";
	private static final String PREFERENCES_KEY = "AUTHORIZATON_CODE";
	private static final String PREFERENCES_TOKEN = "AUTHORIZATON_TOKEN";
	private static final String INTELLICLOUD_NEW_BASE_URL = "http://81.204.121.229/intellicloudservicenew/";
	
	private static final int HTTP_REQUEST_TIMEOUT_MS = 5 * 1000;
	private static final String TAG = "NetworkCommunication";
	public static Long getUnixMillisecondsFromJsonDate(String jsonDate)
	{
		if (jsonDate != null && jsonDate.length() > 0 && !jsonDate.equals("null"))
		{
			String[] intermediateString = jsonDate.split("\\(");
			String[] isolatedDate = intermediateString[1].split("\\+");
			Long dateMilliSeconds = Long.parseLong(isolatedDate[0]);
			Long dateTimeZone = Long.parseLong(isolatedDate[1].substring(0, 2));
			Long milliSecondsDifference = dateTimeZone * 3600000;
			return dateMilliSeconds + milliSecondsDifference;
		}
		return 0L;
	}
	public static int getIdFromURI(String uri)
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
	public static Boolean isServerObjectNewer(JSONObject serverObject, Cursor localObjectCursor)
	{
		int timeStampColumn = localObjectCursor.getColumnIndex(QuestionsEntry.COLUMN_TIMESTAMP);
		String serverTimeStampString = serverObject.optString("LastChangedTime", null);
		
		if (serverTimeStampString != null)
		{
			long localTimeStamp = localObjectCursor.getLong(timeStampColumn);
			long serverTimeStamp = getUnixMillisecondsFromJsonDate(serverObject.optString("LastChangedTime"));
			if (serverTimeStamp > localTimeStamp)
			{
				return true;
			}
			
		}
		return false;
	}
	public static JSONArray performNetworkPostRequest(String uri, JSONObject params, Context context)
			throws JSONException, ParseException, IOException, AuthenticationException, OperationCanceledException, AuthenticatorException
	{
		final JSONArray serverArray;
		StringEntity entity = new StringEntity(params.toString());
		HttpPost request = new HttpPost(uri);
		request.setEntity(entity);
		request.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
        request.addHeader(AUTHORIZATION_HEADER, getBase64AuthorizationHeader(context));
        
        
        final HttpResponse resp = getHttpClient().execute(request);
        final String response = EntityUtils.toString(resp.getEntity());
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
           
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

	public static JSONArray performNetworkGetRequest(String uri, Context context) 
			throws JSONException, ParseException, IOException, AuthenticationException, OperationCanceledException, AuthenticatorException
	{
		final JSONArray serverArray;
        
		
		HttpGet request = new HttpGet(uri);
		request.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
	    request.addHeader(AUTHORIZATION_HEADER, getBase64AuthorizationHeader(context));
		
		//String token = accountManager.blockingGetAuthToken(account, TAG, false);
        
        // Send the updated friends data to the server
        Log.i(TAG, "Syncing to: " + uri);
        
        
        Log.d(TAG, request.getFirstHeader(AUTHORIZATION_HEADER).getValue());
        //post.setEntity(entity);
        final HttpResponse resp = getHttpClient().execute(request);
        String response = EntityUtils.toString(resp.getEntity());
        
        //If response is not a JsonArray, make it one
        if (response.charAt(0) == '{')
        {
        	response = "[" + response + "]"; 
        }
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
           
            serverArray = new JSONArray(response);
            //Log.d(TAG, response);
            
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
	
	 private static String getBase64AuthorizationHeader(Context context) {
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
	public static int getRealIdForObjectURI(String uri, Context context) throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException
	{
		String requestUrl = INTELLICLOUD_NEW_BASE_URL + uri;
		JSONArray result = performNetworkGetRequest(requestUrl, context);
		JSONObject firstObject = result.getJSONObject(0);
		return getIdFromURI(firstObject.getString("Id"));
	}

}
