package nl.fhict.intellicloud.answers.backendcommunication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

public class BackendSyncAdapter extends AbstractThreadedSyncAdapter {
	    
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
			//String authToken = accountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

			
			
		}
}
