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
import android.os.RemoteException;
import android.util.Log;

public class BackendSyncAdapter extends AbstractThreadedSyncAdapter {
		private static final String TAG = "SyncAdapter";
		
	    
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
			Log.d(TAG, "Starting sync...");
			ServerAccessor accessor = new ServerAccessor(getContext(), accountManager, contentProviderClient, account);
			try {
				accessor.syncAnswers();
				accessor.syncQuestions();
				accessor.syncReviews();
				accessor.syncUsers();
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
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				
			}
		}	
		
}
