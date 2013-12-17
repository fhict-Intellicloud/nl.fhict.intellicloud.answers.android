package nl.fhict.intellicloud.answers.backendcommunication.oauth;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import android.os.AsyncTask;

public class AuthenticationManager {
	
	public static final String CLIENT_ID = "918910489517-v6j5fmrvi60sn3pog8dvejvsdm9rr7p7.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "RgK9Kx23RitsfPEJVwA-Nhkh";
	public static final String SCOPE = "openid%20profile%20email";
	
	private JSONParser jsonParser;
	
	private String authorizationCode;
	private AccessToken accessToken;
	
	public AuthenticationManager(String authorizationCode) {
		this.authorizationCode = authorizationCode;
		this.jsonParser = JsonParserFactory.getInstance().newJsonParser();
	}
	
	public String getAccessToken() {
		if(this.accessToken == null)
			this.accessToken = this.requestAccessToken();
		if(!this.validateAccessToken(this.accessToken))
			this.accessToken = this.refreshAccessToken(this.accessToken);
		
		return this.accessToken.getToken();
	}
	
	private boolean validateAccessToken(AccessToken accessToken) {
		String jsonResponse = null;
		try {
			jsonResponse = new ValidateAccessTokenTask().execute(this.accessToken).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map parsedValues = this.jsonParser.parseJson(jsonResponse);
		return !parsedValues.containsKey("error");
	}
	
	private AccessToken refreshAccessToken(AccessToken accessToken) {
		String jsonResponse = null;
		try {
			jsonResponse = new RefreshAccessTokenTask().execute(this.accessToken).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map parsedValues = this.jsonParser.parseJson(jsonResponse);
		return new AccessToken(
				accessToken.id,
				(String) parsedValues.get("token_type"),
				Integer.parseInt((String) parsedValues.get("expires_in")),
				(String) parsedValues.get("access_token"),
				accessToken.refreshToken);
	}
	
	private AccessToken requestAccessToken() {
		String jsonResponse = null;
		try {
			jsonResponse = new RequestAccessTokenTask().execute(this.authorizationCode).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map parsedValues = this.jsonParser.parseJson(jsonResponse);
		return new AccessToken(
				(String) parsedValues.get("id_token"),
				(String) parsedValues.get("token_type"),
				Integer.parseInt((String) parsedValues.get("expires_in")),
				(String) parsedValues.get("access_token"),
				(String) parsedValues.get("refresh_token"));
	}
	
	private class RequestAccessTokenTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			HttpPost httpPost = new HttpPost("https://accounts.google.com/o/oauth2/token");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		    nameValuePairs.add(new BasicNameValuePair("code", params[0]));
		    nameValuePairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
		    nameValuePairs.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
		    nameValuePairs.add(new BasicNameValuePair("redirect_uri", "http://localhost"));
		    nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
		    
		    StringWriter response = new StringWriter();
			try {
				HttpClient httpClient = new DefaultHttpClient();
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				InputStream stream = httpClient.execute(httpPost).getEntity().getContent();
				IOUtils.copy(stream, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return response.toString();
		}
	}
	
	private class RefreshAccessTokenTask extends AsyncTask<AccessToken, Void, String> {
		@Override
		protected String doInBackground(AccessToken... params) {
			HttpPost httpPost = new HttpPost("https://accounts.google.com/o/oauth2/token");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		    nameValuePairs.add(new BasicNameValuePair("refresh_token", params[0].refreshToken));
		    nameValuePairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
		    nameValuePairs.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
		    nameValuePairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
		    
		    StringWriter response = new StringWriter();
			try {
				HttpClient httpClient = new DefaultHttpClient();
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				InputStream stream = httpClient.execute(httpPost).getEntity().getContent();
				IOUtils.copy(stream, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return response.toString();
		}
	}
	
	private class ValidateAccessTokenTask extends AsyncTask<AccessToken, Void, String> {
		@Override
		protected String doInBackground(AccessToken... params) {
			HttpGet httpGet = new HttpGet(
					String.format("https://accounts.google.com/o/oauth2/tokeninfo?access_token=%s", params[0].token));
		    
		    StringWriter response = new StringWriter();
			try {
				HttpClient httpClient = new DefaultHttpClient();
				
				InputStream stream = httpClient.execute(httpGet).getEntity().getContent();
				IOUtils.copy(stream, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return response.toString();
		}
	}
	
	private class AccessToken {
		private String id;
		private String type;
		private int expiresIn;
		private String token;
		private String refreshToken;
		
		public AccessToken(String id, String type, int expiresIn, String token, String refreshToken) {
			this.id = id;
			this.type = type;
			this.expiresIn = expiresIn;
			this.token = token;
			this.refreshToken = refreshToken;
		}
		
		public String getId() {
			return this.id;
		}
		
		public String getType() {
			return this.type;
		}
		
		public int getExpiresIn() {
			return this.expiresIn;
		}
		
		public String getToken() {
			return this.token;
		}
		
		public String getRefreshToken() {
			return this.refreshToken;
		}
	}
}
