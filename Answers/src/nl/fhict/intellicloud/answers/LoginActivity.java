package nl.fhict.intellicloud.answers;

import com.google.android.gms.common.SignInButton;

import nl.fhict.intellicloud.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONTokener;

public class LoginActivity extends Activity implements android.view.View.OnClickListener {

	private final String CLIENT_ID = "918910489517-v6j5fmrvi60sn3pog8dvejvsdm9rr7p7.apps.googleusercontent.com";
	private final String CLIENT_SECRET = "RgK9Kx23RitsfPEJVwA-Nhkh";
	private final String SCOPE = "openid%20profile%20email";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		SignInButton button = (SignInButton) findViewById(R.id.sign_in_button);
		button.setOnClickListener(this);
		button.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_LIGHT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
        	setContentView(this.loadAuthorizationWebView());
        }
    }
	
	private WebView loadAuthorizationWebView() {
		WebView webView = new WebView(this);
    	
    	webView.getSettings().setJavaScriptEnabled(true);
    	webView.loadUrl(String.format(
				 "https://accounts.google.com/o/oauth2/auth?client_id=%s&response_type=code&scope=%s&redirect_uri=http://localhost",
				 CLIENT_ID, SCOPE));
    	
    	final Pattern pattern = Pattern.compile("http://localhost/\\?code=([^\\s&=]*)&authuser=([^\\s&=]*)&prompt=([^\\s&=]*)&session_state=([^\\s&=]*)");
    	webView.setWebViewClient(new WebViewClient() {
   			@Override
   			public boolean shouldOverrideUrlLoading(WebView view, String url) {
   				Matcher matcher = pattern.matcher(url);
   				
   				if(matcher.matches())
   					authorizationCodeReceived(matcher.group(1));
   				
   				return super.shouldOverrideUrlLoading(view, url);
   			}});
    	
    	return webView;
	}
	
	private void authorizationCodeReceived(String authorizationCode) {
		setContentView(R.layout.activity_login);
		
		new GetAccessTokenTask().execute(authorizationCode);
	}
	
	private class GetAccessTokenTask extends AsyncTask<String, Void, Void>
	{
		@Override
		protected Void doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("https://accounts.google.com/o/oauth2/token");
			
			try {
			    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			    nameValuePairs.add(new BasicNameValuePair("code", params[0]));
			    nameValuePairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
			    nameValuePairs.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
			    nameValuePairs.add(new BasicNameValuePair("redirect_uri", "http://localhost"));
			    nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
			    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			    // Execute HTTP Post Request
			    HttpResponse response = httpClient.execute(httpPost);
			    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			    StringBuilder data = new StringBuilder();
			    
			    String line = null;
			    while ((line = reader.readLine()) != null)
			    	data.append(line + "\r\n");
			    
			    // TODO: Parse JSON and create Access Token and return.

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}
}
