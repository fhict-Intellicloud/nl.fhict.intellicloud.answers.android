package nl.fhict.intellicloud.answers;

import nl.fhict.intellicloud.R;
import nl.fhict.intellicloud.answers.backendcommunication.oauth.AuthenticationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.graphics.*;
import java.util.regex.*;

public class AuthorizationActivity extends Activity {

	public static final String AUTHORIZATION_CODE = "AUTHORIZATION_CODE";
	
	private final Pattern callbackPattern = Pattern.compile("http://localhost/\\?code=([^\\s&=]*)&authuser=([^\\s&=]*)&prompt=([^\\s&=]*)&session_state=([^\\s&=]*)");
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_authorization);
		
		this.progressBar = (ProgressBar) findViewById(R.id.webViewProgressBar);
		WebView webView = (WebView) findViewById(R.id.webViewAuthorization);
		webView.getSettings().setJavaScriptEnabled(true);
		
		this.loadAuthorization(webView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authorization, menu);
		return true;
	}
	
	private void loadAuthorization(WebView webView) {
    	webView.loadUrl(String.format(
			 "https://accounts.google.com/o/oauth2/auth?client_id=%s&response_type=code&scope=%s&redirect_uri=http://localhost",
			 AuthenticationManager.CLIENT_ID, 
			 AuthenticationManager.SCOPE));
    	
    	webView.setWebViewClient(new WebViewClient() {
    		@Override
    		public void onPageStarted(WebView view, String url, Bitmap favicon) {
    			progressBar.setVisibility(View.VISIBLE);
    		}
    		
   			@Override
   			public boolean shouldOverrideUrlLoading(WebView view, String url) {
   				view.loadUrl(url);
   				
   				Matcher matcher = callbackPattern.matcher(url);
   				if(matcher.matches())
   					authorizationCodeReceived(matcher.group(1));
   				
   				return true;
   			}
   			
   			@Override
   			public void onPageFinished(WebView view, String url) {
   				super.onPageFinished(view, url);
   				progressBar.setVisibility(View.INVISIBLE);
   			}});
	}
	
	private void authorizationCodeReceived(String authorizationCode) {
		Intent result = new Intent();
		result.putExtra(AUTHORIZATION_CODE, authorizationCode);
		
		setResult(Activity.RESULT_OK, result);
		this.finish();
	}
}
