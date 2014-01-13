package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;
import java.util.ArrayList;

import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.AnswersEntry;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.ReviewsEntry;

import org.apache.http.auth.AuthenticationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.RemoteException;

public class ReviewSync {
		private ContentProviderClient contentProviderClient;
		private Context context;
		
		private int idColumn;
		private int localIdColumn;
		private int timestampColumn;
		private int answerIdColumn;
		private int reviewerIdColumn;
		private int dateColumn;
		private int reviewColumn;
		private int reviewStateColumn;
		private int askerIdColumn;
		private int questionStateColumn;
		
		public ReviewSync(Context context, ContentProviderClient contentProviderClient)
		{
			this.context = context;
			this.contentProviderClient = contentProviderClient;
		}
		
		public ArrayList<JSONObject> syncReviews(JSONArray reviewsResultArray) throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
		{
			
			ArrayList<JSONObject> reviewsToUpload = new ArrayList<JSONObject>();
			JSONArray reviewsToAddToDB = new JSONArray();
			
			Uri uri = BackendContentProvider.CONTENT_REVIEWS;
			Cursor reviewsCursor = null;
			try {
				reviewsCursor = contentProviderClient.query(uri, null, null, null, null);
				
				idColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_BACKEND_ID);
				timestampColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_TIMESTAMP);
				answerIdColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_ANSWER_ID);
				reviewerIdColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_REVIEWER_ID);
				dateColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_DATE);
				reviewColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_REVIEW);
				localIdColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_ID);
				reviewStateColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_REVIEWSTATE);
				
				for (int i = 0; i < reviewsResultArray.length(); i++)
				{
					
					reviewsCursor.moveToFirst();
					boolean reviewFoundInDb = false;
					JSONObject serverReview = reviewsResultArray.getJSONObject(i);
					
					while (!reviewsCursor.isAfterLast() && !reviewFoundInDb) {
					
						if (serverReview.getString("Id") == reviewsCursor.getString(idColumn))
						{
							reviewFoundInDb=true;
							//Check if updated
							
							break;
						}
					}
					if (!reviewFoundInDb)
					{
						reviewsToAddToDB.put(serverReview);
						
					}
					
					
					reviewsCursor.moveToNext();
					
				}
				
				
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			reviewsCursor.moveToFirst();
			while (!reviewsCursor.isAfterLast())
			{
				if (reviewsCursor.getString(idColumn) == null)
				{
					reviewsToUpload.add(getJsonForCurrentReview(reviewsCursor));
					
					String deleteUri = uri + "/" + reviewsCursor.getInt(localIdColumn);
					reviewsCursor.moveToNext();
					contentProviderClient.delete(Uri.parse(deleteUri), null, null);
				}
				else
				{
					reviewsCursor.moveToNext();
				}
			}
			reviewsCursor.close();
			
			for (int i = 0; i < reviewsToAddToDB.length(); i++)
			{
				addReviewToDb(reviewsToAddToDB.getJSONObject(i));
			}
			return reviewsToUpload;
			
		}
		private JSONObject getJsonForCurrentReview(Cursor cursor) throws JSONException
		{
			JSONObject jsonAnswer = new JSONObject();
			jsonAnswer.accumulate("employeeId", cursor.getInt(reviewerIdColumn));
			jsonAnswer.accumulate("answerId", cursor.getInt(answerIdColumn));
			jsonAnswer.accumulate("review", cursor.getString(reviewColumn));
			return jsonAnswer;
		}
		private void addReviewToDb(JSONObject review) throws JSONException, RemoteException
		{
			ContentValues values = new ContentValues();
			values.put(ReviewsEntry.COLUMN_BACKEND_ID, review.optString("Id"));
			values.put(ReviewsEntry.COLUMN_TIMESTAMP, review.optString("LastChangedTime"));
			values.put(ReviewsEntry.COLUMN_DATE, review.optString("Date"));
			values.put(ReviewsEntry.COLUMN_REVIEW, review.optString("Review"));
			values.put(ReviewsEntry.COLUMN_REVIEWSTATE, review.optInt("ReviewState"));
			
			
			JSONObject answer = review.optJSONObject("Answer");
			if (answer != null)
			{
				values.put(ReviewsEntry.COLUMN_ANSWER_ID, answer.getString("Id"));
			}
			JSONObject reviewer = review.optJSONObject("Reviewer");
			if (reviewer != null)
			{
				values.put(ReviewsEntry.COLUMN_REVIEWER_ID, answer.getString("Id"));
			}
					
			contentProviderClient.insert(BackendContentProvider.CONTENT_REVIEWS, values);
		}
	
}
