package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;
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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;

public class BackendSyncAdapter extends AbstractThreadedSyncAdapter {
		private static final String TAG = "SyncAdapter";
		private static final String AUTHORIZATION_HEADER = "AuthorizationToken";
		private static final int HTTP_REQUEST_TIMEOUT_MS = 5 * 1000;
		
	    
	    // Global variables
	    // Define a variable to contain a content resolver instance
	    ContentResolver contentResolver;
	    AccountManager accountManager;
	    /**
	     * Set up the sync adapter
	     */
	    public BackendSyncAdapter(Context context, boolean autoInitialize) {
	        super(context, autoInitialize);
	        /*
	         * If your app uses a content resolver, get an instance of it
	         * from the incoming Context
	         */
	        contentResolver = context.getContentResolver();
	        accountManager = AccountManager.get(context);
	    }
	    
	    /**
	     * Set up the sync adapter. This form of the
	     * constructor maintains compatibility with Android 3.0
	     * and later platform versions
	     */
	    public BackendSyncAdapter(
	            Context context,
	            boolean autoInitialize,
	            boolean allowParallelSyncs) {
	        super(context, autoInitialize, allowParallelSyncs);
	        /*
	         * If your app uses a content resolver, get an instance of it
	         * from the incoming Context
	         */
	        contentResolver = context.getContentResolver();
	        accountManager = AccountManager.get(context);
	        
	        
	    }

		@Override
		public void onPerformSync(Account account, Bundle bundle, String arg2,
				ContentProviderClient contentProviderClient, SyncResult result) {
			
			//Get auth token //TODO
			
			ServerAccessor accessor = new ServerAccessor(getContext(), authtoken);
			
			
			
			
		}
		private void syncQuestions(ContentProviderClient contentProviderClient, Account account)
		{
			
		}
		private JSONArray performNetworkRequest(String uri, Account account, ArrayList<NameValuePair> params) 
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
	                Log.e(TAG, "Authentication exception in sending dirty contacts");
	                throw new AuthenticationException();
	            } else {
	                Log.e(TAG, "Server error in sending dirty contacts: " + resp.getStatusLine());
	                throw new IOException();
	            }
	        }

			return serverArray;
		}
		public static HttpClient getHttpClient() {
	        HttpClient httpClient = new DefaultHttpClient();
	        final HttpParams params = httpClient.getParams();
	        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
	        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
	        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
	        return httpClient;
	    }
		
}
