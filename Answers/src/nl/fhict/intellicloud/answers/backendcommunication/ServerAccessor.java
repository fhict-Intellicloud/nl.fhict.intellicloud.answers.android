package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;

import nl.fhict.intellicloud.answers.*;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ServerAccessor {
	private final String TAG = "SyncAdapter";
	Context context;
	String token;
	public ServerAccessor(Context context, String accountToken)
	{
		this.context = context;
		this.token = accountToken;
	}
	
	public  getRemoteQuestions()
	{
		return null;
	}
	public void updateRemoteQuestions(ArrayList<Question> questions)
	{
		
	}
	public ArrayList<Answer> getRemoteAnswers()
	{
		return null;
	}
	public void updateRemoteAnswers(ArrayList<Answer> answers)
	{
		
	}
	public ArrayList<Review> getRemoteReviews()
	{
		return null;
	}
	public void updateRemoteReviews(ArrayList<Review> reviews)
	{
		
	}
	public ArrayList<Feedback> getRemoteFeedback()
	{
		return null;
	}
	public void updateRemoteFeedback(ArrayList<Feedback> feedback)
	{
		
	}
	public ArrayList<Question> getRemoteUsers()
	{
		return null;
	}
	
	public JSONArray getResultFromHttpRequest()
	{
		/*
		Log.i(TAG, "Beginning network synchronization");
        try {
            //final URL location = new URL(FEED_URL);
            InputStream stream = null;

            try {
                Log.i(TAG, "Streaming data from network:");
               // stream = downloadUrl(location);
                //updateLocalFeedData(stream, syncResult);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (MalformedURLException e) {
            Log.wtf(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }
        Log.i(TAG, "Network synchronization complete");*/
		return null;
	}
}
