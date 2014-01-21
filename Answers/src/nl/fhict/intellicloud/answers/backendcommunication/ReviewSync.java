package nl.fhict.intellicloud.answers.backendcommunication;

import java.io.IOException;
import java.util.ArrayList;

import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.AnswersEntry;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.QuestionsEntry;
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
import android.util.Log;

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


	public ReviewSync(Context context, ContentProviderClient contentProviderClient)
	{
		this.context = context;
		this.contentProviderClient = contentProviderClient;
	}

	public ArrayList<JSONObject> syncReviews(JSONArray reviewsResultArray) throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException, RemoteException
	{

		ArrayList<JSONObject> reviewsToUpload = new ArrayList<JSONObject>();
		ArrayList<JSONObject>  reviewsToAddToDB = new ArrayList<JSONObject> ();

		Uri uri = BackendContentProvider.CONTENT_REVIEWS;
		Cursor reviewsCursor = null;

		reviewsCursor = contentProviderClient.query(uri, null, null, null, null);

		idColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_BACKEND_ID);
		timestampColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_TIMESTAMP);
		answerIdColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_ANSWER_ID);
		reviewerIdColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_REVIEWER_ID);
		dateColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_DATE);
		reviewColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_REVIEW);
		localIdColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_ID);
		reviewStateColumn = reviewsCursor.getColumnIndex(ReviewsEntry.COLUMN_REVIEWSTATE);

		reviewsCursor.moveToFirst();
		
		for (int i = 0; i < reviewsResultArray.length(); i++)
		{
			reviewsToAddToDB.add(reviewsResultArray.getJSONObject(i));
		}

		while (!reviewsCursor.isAfterLast()) {
			Log.d("sync", "while");
			JSONObject serverReview = null;


			for (int i = 0; i < reviewsToAddToDB.size(); i++)
			{
				
				serverReview = reviewsToAddToDB.get(i);
				
				if (SyncHelper.getIdFromURI(serverReview.getString("Id")) == (reviewsCursor.getInt(idColumn)))
				{
					reviewsToAddToDB.remove(i);
					if (SyncHelper.isServerObjectNewer(serverReview, reviewsCursor))
					{
						updateReview(serverReview, reviewsCursor.getInt(localIdColumn));
					}
					break;

				}
			}

			reviewsCursor.moveToNext();
		}

		reviewsCursor.moveToFirst();
		while (!reviewsCursor.isAfterLast())
		{
			if (reviewsCursor.getString(idColumn) == null)
			{
				reviewsToUpload.add(getJsonForCurrentReview(reviewsCursor));

//				String deleteUri = uri + "/" + reviewsCursor.getInt(localIdColumn);
				reviewsCursor.moveToNext();
//				contentProviderClient.delete(Uri.parse(deleteUri), null, null);
			}
			else
			{
				reviewsCursor.moveToNext();
			}
		}
		reviewsCursor.close();

		for (int i = 0; i < reviewsToAddToDB.size(); i++)
		{
			addReviewToDb(reviewsToAddToDB.get(i));
		}

		return reviewsToUpload;

	}
	private ContentValues getReviewContentValues(JSONObject review) throws AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, JSONException, IOException
	{
		ContentValues values = new ContentValues();
		values.put(ReviewsEntry.COLUMN_TIMESTAMP, review.optString("LastChangedTime"));
		//values.put(ReviewsEntry.COLUMN_DATE, review.optString("Date"));
		values.put(ReviewsEntry.COLUMN_REVIEW, review.optString("Review"));
		values.put(ReviewsEntry.COLUMN_REVIEWSTATE, review.optInt("ReviewState"));


		String answer = review.optString("Answer");
		if (answer != null && !answer.equals("null") && answer.length() > 0)
		{
			values.put(ReviewsEntry.COLUMN_ANSWER_ID, SyncHelper.getRealIdForObjectURI(answer, context));
		}
		String reviewer = review.optString("Reviewer");
		if (reviewer != null && !reviewer.equals("null") && reviewer.length() > 0)
		{
			values.put(ReviewsEntry.COLUMN_REVIEWER_ID, SyncHelper.getRealIdForObjectURI(reviewer,context));
			
		}
		String backendid = review.optString("Id");
		if (backendid != null)
		{
			values.put(ReviewsEntry.COLUMN_BACKEND_ID, SyncHelper.getIdFromURI(backendid));
		}
		return values;
	}
	private void updateReview(JSONObject review, int rowId) throws JSONException, RemoteException, AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, IOException
	{
		String updateUri = BackendContentProvider.CONTENT_REVIEWS + "/" + rowId;
		ContentValues values = getReviewContentValues(review);
		contentProviderClient.update(Uri.parse(updateUri), values, null, null);
	}
	private JSONObject getJsonForCurrentReview(Cursor cursor) throws JSONException
	{
		JSONObject jsonAnswer = new JSONObject();
		jsonAnswer.accumulate("employeeId", cursor.getInt(reviewerIdColumn));
		jsonAnswer.accumulate("answerId", cursor.getInt(answerIdColumn));
		jsonAnswer.accumulate("review", cursor.getString(reviewColumn));
		return jsonAnswer;
	}
	private void addReviewToDb(JSONObject review) throws JSONException, RemoteException, AuthenticationException, ParseException, OperationCanceledException, AuthenticatorException, IOException
	{
		ContentValues values = getReviewContentValues(review);
		contentProviderClient.insert(BackendContentProvider.CONTENT_REVIEWS, values);
	}

}
