package nl.fhict.intellicloud.answers.backendcommunication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackendSyncService extends Service {
	    // Storage for an instance of the sync adapter
	    private static BackendSyncAdapter syncAdapter = null;
	    // Object to use as a thread-safe lock
	    private static final Object sSyncAdapterLock = new Object();
	    
	    public BackendSyncService()
	    {
	    	super();
	    }
	    
	    /*
	     * Instantiate the sync adapter object.
	     */
	    @Override
	    public void onCreate() {
	        /*
	         * Create the sync adapter as a singleton.
	         * Set the sync adapter as syncable
	         * Disallow parallel syncs
	         */
	        synchronized (sSyncAdapterLock) {
	            if (syncAdapter == null) {
	            	Log.d("SyncService", "Sync service started");
	                syncAdapter = new BackendSyncAdapter(getApplicationContext(), true);
	            }
	        }
	    }
	    /**
	     * Return an object that allows the system to invoke
	     * the sync adapter.
	     *
	     */
	    @Override
	    public IBinder onBind(Intent intent) {
	        /*
	         * Get the object that allows external processes
	         * to call onPerformSync(). The object is created
	         * in the base class code when the SyncAdapter
	         * constructors call super()
	         */
	        return syncAdapter.getSyncAdapterBinder();
    }
}

