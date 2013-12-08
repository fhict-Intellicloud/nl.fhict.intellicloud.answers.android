package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import nl.fhict.intellicloud.answers.Answer;
import nl.fhict.intellicloud.answers.Review;
import nl.fhict.intellicloud.answers.ReviewState;
import nl.fhict.intellicloud.answers.User;
import nl.fhict.intellicloud.answers.UserType;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.*;


public class ReviewDataSource implements IReviewService {
	private SQLiteDatabase database;
	private LocalStorageSQLiteHelper dbHelper;
	private final String[] allColumns = { ReviewsEntry.COLUMN_ID, 
										ReviewsEntry.COLUMN_REVIEW,
										ReviewsEntry.COLUMN_BACKEND_ID,
										ReviewsEntry.COLUMN_REVIEWER_ID,
										ReviewsEntry.COLUMN_REVIEWSTATE,
										ReviewsEntry.COLUMN_ANSWER
										};
	private IAnswerService answerDataSource;
											
	
	
	public ReviewDataSource(Context context) {
		dbHelper = new LocalStorageSQLiteHelper(context);
		answerDataSource = new AnswerDataSource(context);
	}
	
	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	private void close() {
		dbHelper.close();
	}
	@Override
	public void CreateReview(Review review) {
		ContentValues values = new ContentValues();
			
		values.put(ReviewsEntry.COLUMN_REVIEW, review.getReview());
		values.put(ReviewsEntry.COLUMN_REVIEWER_ID, review.getReviewer().getId());
		values.put(ReviewsEntry.COLUMN_REVIEWSTATE, review.getReviewState().toString());
		values.put(ReviewsEntry.COLUMN_ANSWER, review.getAnswer().getId());
			
		open();
		database.insert(ReviewsEntry.TABLE_NAME, null, values);
		close();
			

	}

	@Override
	public ArrayList<Review> GetReviews(int answerId) {
		String answerFilter = null;
		ArrayList<Review> filteredReviews = new ArrayList<Review>();
		
		answerFilter = ReviewsEntry.COLUMN_ANSWER + " = " + answerId;
				
		
		open();
		Cursor cursor = database.query(ReviewsEntry.TABLE_NAME, allColumns, answerFilter, null, null, null, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			Review review = getNextReviewFromCursor(cursor);
			filteredReviews.add(review);
			
			cursor.moveToNext();
		}
		close();
		return filteredReviews;
	}

	@Override
	public void UpdateReview(Review review) {
		ContentValues values = new ContentValues();
		values.put(ReviewsEntry.COLUMN_REVIEWSTATE, review.getReviewState().toString());
	
		open();
		database.update(ReviewsEntry.TABLE_NAME, values, ReviewsEntry.COLUMN_ID + " = " + review.getId(), null);
		close();

	}
	
	private Review getNextReviewFromCursor(Cursor cursor)
	{
		User reviewer = getUser(cursor.getInt(2));
		Answer answerForReview = answerDataSource.GetAnswer(cursor.getInt(4));
		ReviewState state = ReviewState.valueOf(cursor.getString(3));
		Review review = new Review(cursor.getInt(0),
								cursor.getString(1),
								answerForReview,
								reviewer,
								state);
								
		return review;			
								
				
	}
	private User getUser(int id)
	{
		User user = null;
		Cursor cursor = database.query(UsersEntry.TABLE_NAME, UsersEntry.ALL_COLUMNS, UsersEntry.COLUMN_ID + " = " + id , null, null, null, null);
		if (cursor.moveToFirst())
		{
			user = new User(cursor.getInt(0), 
					cursor.getString(1), 
					cursor.getString(2), 
					cursor.getString(3), 
					UserType.valueOf(cursor.getString(4)));
		}
		cursor.close();
		
		return user;
	}

}
