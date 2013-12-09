package nl.fhict.intellicloud.answers.backendcommunication;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

public class BackendSyncAdapter extends AbstractThreadedSyncAdapter {
	    
	    // Global variables
	    // Define a variable to contain a content resolver instance
	    ContentResolver mContentResolver;
	    /**
	     * Set up the sync adapter
	     */
	    public BackendSyncAdapter(Context context, boolean autoInitialize) {
	        super(context, autoInitialize);
	        /*
	         * If your app uses a content resolver, get an instance of it
	         * from the incoming Context
	         */
	        mContentResolver = context.getContentResolver();
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
	        mContentResolver = context.getContentResolver();
	        
	    }

		@Override
		public void onPerformSync(Account arg0, Bundle arg1, String arg2,
				ContentProviderClient arg3, SyncResult arg4) {
			// TODO Auto-generated method stub
			
		}
}
