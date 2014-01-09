package nl.fhict.intellicloud.answers.backendcommunication;

import nl.fhict.intellicloud.answers.backendcommunication.oauth.AuthenticationManager;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;


public class Authenticator extends AbstractAccountAuthenticator {
	private static final String PREFERENCES_NAME = "nl.fhict.intellicloud.answers";
	private static final String PREFERENCES_KEY = "AUTHORIZATON_CODE";
	private Context context;
	
    // Simple constructor
    public Authenticator(Context context) {
    	super(context);
    	this.context = context;
        
    }
    // Editing properties is not supported
    @Override
    public Bundle editProperties(
            AccountAuthenticatorResponse r, String s) {
        throw new UnsupportedOperationException();
    }
    // Don't add additional accounts
    @Override
    public Bundle addAccount(
            AccountAuthenticatorResponse r,
            String s,
            String s2,
            String[] strings,
            Bundle bundle) throws NetworkErrorException {
        return null;
    }
    // Ignore attempts to confirm credentials
    @Override
    public Bundle confirmCredentials(
            AccountAuthenticatorResponse r,
            Account account,
            Bundle bundle) throws NetworkErrorException {
        return null;
    }
    @Override
    public Bundle getAuthToken(
            AccountAuthenticatorResponse r,
            Account account,
            String s,
            Bundle bundle) throws NetworkErrorException {
    	
    	
    	final Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        result.putString(AccountManager.KEY_AUTHTOKEN, getBase64AuthorizationHeader());
        return result;
    }
    // Getting a label for the auth token is not supported
    @Override
    public String getAuthTokenLabel(String s) {
        return getBase64AuthorizationHeader();
    }
    // Updating user credentials is not supported
    @Override
    public Bundle updateCredentials(
            AccountAuthenticatorResponse r,
            Account account,
            String s, Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }
    // Checking features for the account is not supported
    @Override
    public Bundle hasFeatures(
        AccountAuthenticatorResponse r,
        Account account, String[] strings) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }
    private String getBase64AuthorizationHeader() {
    	SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    	AuthenticationManager authManager = AuthenticationManager.getInstance();
    	authManager.Initialize(preferences.getString(PREFERENCES_KEY, null));
    	
    	
        String json = String.format(
                        "{" +
                        "        \"issuer\":\"accounts.google.com\"," +
                        "        \"access_token\":\"%s\"" +
                        "}", AuthenticationManager.getInstance().getAccessToken()); 
        Log.d("AccountManager", "token = \n" + json);
        byte[] encodedbytes = Base64.encode(json.getBytes(), Base64.DEFAULT);
        return new String(encodedbytes);
    }
}
