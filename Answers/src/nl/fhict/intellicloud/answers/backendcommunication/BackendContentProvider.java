package nl.fhict.intellicloud.answers.backendcommunication;



import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.*;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class BackendContentProvider extends ContentProvider {

	// database
	  private LocalStorageSQLiteHelper dbHelper;

	  // used for the UriMatcher 
	  private static final int QUESTIONS = 10;
	  private static final int QUESTIONS_ID = 11;
	  private static final int ANSWERS = 20;
	  private static final int ANSWERS_ID = 21;
	  private static final int FEEDBACK = 30;
	  private static final int FEEDBACK_ID = 31;
	  private static final int REVIEWS = 40;
	  private static final int REVIEWS_ID = 41;
	  private static final int USERS = 50;
	  private static final int USERS_ID = 51;
		
	  
	  private static final String AUTHORITY = "nl.fhict.intellicloud.answers.contentprovider";
	  private static final String URI_DOMAIN = "content://" + AUTHORITY;

	  
	  public static final Uri CONTENT_QUESTIONS = Uri.parse(URI_DOMAIN + "/questions");
	  public static final Uri CONTENT_ANSWERS = Uri.parse(URI_DOMAIN + "/answers");
	  public static final Uri CONTENT_FEEDBACK = Uri.parse(URI_DOMAIN + "/feedback");
	  public static final Uri CONTENT_USERS = Uri.parse(URI_DOMAIN + "/users");
	  public static final Uri CONTENT_REVIEWS = Uri.parse(URI_DOMAIN + "/reviews");

	  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	      + "/todos";
	  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	      + "/answers";

	  private static final UriMatcher uriMatcher;
	  static {
	      uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	      uriMatcher.addURI(AUTHORITY, "questions", QUESTIONS );
	      uriMatcher.addURI(AUTHORITY, "questions/#", QUESTIONS_ID);      
	      uriMatcher.addURI(AUTHORITY, "answers", ANSWERS);
	      uriMatcher.addURI(AUTHORITY, "answers/#", ANSWERS_ID); 
	      uriMatcher.addURI(AUTHORITY, "feedback", FEEDBACK);     
	      uriMatcher.addURI(AUTHORITY, "feedback/#", FEEDBACK_ID); 
	      uriMatcher.addURI(AUTHORITY, "reviews", REVIEWS); 
	      uriMatcher.addURI(AUTHORITY, "reviews/#", REVIEWS_ID); 
	      uriMatcher.addURI(AUTHORITY, "users", USERS); 
	      uriMatcher.addURI(AUTHORITY, "users/#", USERS_ID); 
	  }

	  @Override
	  public boolean onCreate() {
	    dbHelper = new LocalStorageSQLiteHelper(getContext());
	    return false;
	  }

	  @Override
	  public synchronized Cursor query(Uri uri, String[] projection, String selection,
	      String[] selectionArgs, String sortOrder) {

		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		
		int uriType = uriMatcher.match(uri);
		switch (uriType) {
		
		    case QUESTIONS:
		    	queryBuilder.setTables(QuestionsEntry.TABLE_NAME);
		    	
		        break;
		    case QUESTIONS_ID:
		    	queryBuilder.setTables(QuestionsEntry.TABLE_NAME);
		    	queryBuilder.appendWhere(QuestionsEntry.COLUMN_ID + "=" + uri.getLastPathSegment());
		    	break;
		    case ANSWERS:
		    	queryBuilder.setTables(AnswersEntry.TABLE_NAME);
		        break;
		    case ANSWERS_ID:
		    	queryBuilder.setTables(AnswersEntry.TABLE_NAME);
		    	queryBuilder.appendWhere(AnswersEntry.COLUMN_ID + "=" + uri.getLastPathSegment());
		    	break;
		    case USERS:
		    	queryBuilder.setTables(UsersEntry.TABLE_NAME);
		        break;
		    case USERS_ID:
		    	queryBuilder.setTables(UsersEntry.TABLE_NAME);
		    	queryBuilder.appendWhere(UsersEntry.COLUMN_ID + "=" + uri.getLastPathSegment());
		    	break;	        
		    case FEEDBACK:
		    	queryBuilder.setTables(FeedbackEntry.TABLE_NAME);
		        break;
		    case FEEDBACK_ID:
		    	queryBuilder.setTables(FeedbackEntry.TABLE_NAME);
		    	queryBuilder.appendWhere(FeedbackEntry.COLUMN_ID + "=" + uri.getLastPathSegment());
		    	break;
		    case REVIEWS:
		    	queryBuilder.setTables(ReviewsEntry.TABLE_NAME);
		        break;
		    case REVIEWS_ID:
		    	queryBuilder.setTables(ReviewsEntry.TABLE_NAME);
		    	queryBuilder.appendWhere(ReviewsEntry.COLUMN_ID + "=" + uri.getLastPathSegment());
		    	break;
	    	
		    default: throw new SQLException("Failed to delete row with uri: " + uri);
 
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
		    selectionArgs, null, null, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	  }

	  @Override
	  public String getType(Uri uri) {
	    return null;
	  }

	  @Override
	  public synchronized Uri insert(Uri uri, ContentValues values) {
		  Uri _uri = null;
		  SQLiteDatabase db = dbHelper.getWritableDatabase();
		  long insert_id;
		  
		  
		  switch (uriMatcher.match(uri)){
		    case QUESTIONS:
		        insert_id = db.insert(QuestionsEntry.TABLE_NAME, "", values);
		        _uri = createAppendedUriAndNotifyChange(CONTENT_QUESTIONS, insert_id);
		        break;
		    case ANSWERS:
		    	insert_id = db.insert(AnswersEntry.TABLE_NAME, "", values);
		        _uri = createAppendedUriAndNotifyChange(CONTENT_ANSWERS, insert_id);
		        break;
		    case USERS:
		    	insert_id = db.insert(UsersEntry.TABLE_NAME, "", values);
		        _uri = createAppendedUriAndNotifyChange(CONTENT_USERS, insert_id);
		        break;
		    case FEEDBACK:
		    	insert_id = db.insert(FeedbackEntry.TABLE_NAME, "", values);
		        _uri = createAppendedUriAndNotifyChange(CONTENT_FEEDBACK, insert_id);
		        break;
		    case REVIEWS:
		    	insert_id = db.insert(ReviewsEntry.TABLE_NAME, "", values);
		        _uri = createAppendedUriAndNotifyChange(CONTENT_REVIEWS, insert_id);
		        break;
		    	
		    default: throw new SQLException("Failed to insert row into " + uri);
	    }
	    return _uri;       
	  }

	  @Override
	  public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {
	    int uriType = uriMatcher.match(uri);
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	    int rowsDeleted = 0;
	    
	    switch (uriType) {
		    case QUESTIONS:
		    	rowsDeleted = db.delete(QuestionsEntry.TABLE_NAME, selection,
		  	          selectionArgs);
		    	
		        break;
		    case QUESTIONS_ID:
		    	rowsDeleted = deleteRowsById(db, uri, QuestionsEntry.TABLE_NAME, QuestionsEntry.COLUMN_ID, selection, selectionArgs);
		    	break;
		    case ANSWERS:
		    	rowsDeleted = db.delete(AnswersEntry.TABLE_NAME, selection,
			  	          selectionArgs);
		        break;
		    case ANSWERS_ID:
		    	rowsDeleted = deleteRowsById(db, uri, AnswersEntry.TABLE_NAME, AnswersEntry.COLUMN_ID, selection, selectionArgs);
		    	break;
		    case USERS:
		    	rowsDeleted = db.delete(UsersEntry.TABLE_NAME, selection,
			  	          selectionArgs);
		        break;
		    case USERS_ID:
		    	rowsDeleted = deleteRowsById(db, uri, UsersEntry.TABLE_NAME, UsersEntry.COLUMN_ID, selection, selectionArgs);
		    	break;	        
		    case FEEDBACK:
		    	rowsDeleted = db.delete(FeedbackEntry.TABLE_NAME, selection,
			  	          selectionArgs);
		        break;
		    case FEEDBACK_ID:
		    	rowsDeleted = deleteRowsById(db, uri, FeedbackEntry.TABLE_NAME, FeedbackEntry.COLUMN_ID, selection, selectionArgs);
		    	break;
		    case REVIEWS:
		    	rowsDeleted = db.delete(ReviewsEntry.TABLE_NAME, selection,
			  	          selectionArgs);
		        break;
		    case REVIEWS_ID:
		    	rowsDeleted = deleteRowsById(db, uri, ReviewsEntry.TABLE_NAME, ReviewsEntry.COLUMN_ID, selection, selectionArgs);
		    	break;
	    	
		    default: throw new SQLException("Failed to delete row with uri: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	  }
	  private int deleteRowsById(SQLiteDatabase db, Uri uri, String table, String columnIdName, String selection, String[] selectionArgs)
	  {
		  int rowsDeleted;
		  String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsDeleted = db.delete(columnIdName,
	            table + "=" + id, 
	            null);
	      } else {
	        rowsDeleted = db.delete(columnIdName,
	            table + "=" + id 
	            + " and " + selection,
	            selectionArgs);
	      }
	      return rowsDeleted;
	  }
	  @Override
	  public synchronized int update(Uri uri, ContentValues values, String selection,
	      String[] selectionArgs) {

	    int uriType = uriMatcher.match(uri);
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	    int rowsUpdated = 0;
	    switch (uriType) {
		    case QUESTIONS:
		    	rowsUpdated = db.update(QuestionsEntry.TABLE_NAME, values, selection, selectionArgs);
		        break;
		    case QUESTIONS_ID:
		    	rowsUpdated = updateRowsById(db, uri, values, QuestionsEntry.TABLE_NAME, QuestionsEntry.COLUMN_ID, selection, selectionArgs);
		    	break;
		    case ANSWERS:
		    	rowsUpdated = db.update(AnswersEntry.TABLE_NAME, values, selection, selectionArgs);
		        break;
		    case ANSWERS_ID:
		    	rowsUpdated = updateRowsById(db, uri, values, AnswersEntry.TABLE_NAME, AnswersEntry.COLUMN_ID, selection, selectionArgs);
		    	break;
		    case USERS:
		    	rowsUpdated = db.update(UsersEntry.TABLE_NAME, values, selection, selectionArgs);
		        break;
		    case USERS_ID:
		    	rowsUpdated = updateRowsById(db, uri, values, UsersEntry.TABLE_NAME, UsersEntry.COLUMN_ID, selection, selectionArgs);
		    	break;	        
		    case FEEDBACK:
		    	rowsUpdated = db.update(FeedbackEntry.TABLE_NAME, values, selection, selectionArgs);
		        break;
		    case FEEDBACK_ID:
		    	rowsUpdated = updateRowsById(db, uri, values, FeedbackEntry.TABLE_NAME, FeedbackEntry.COLUMN_ID, selection, selectionArgs);
		    	break;
		    case REVIEWS:
		    	rowsUpdated = db.update(ReviewsEntry.TABLE_NAME, values, selection, selectionArgs);
		        break;
		    case REVIEWS_ID:
		    	rowsUpdated = updateRowsById(db, uri, values, ReviewsEntry.TABLE_NAME, ReviewsEntry.COLUMN_ID, selection, selectionArgs);
		    	break;
    	
		    default: throw new SQLException("Failed to update row with uri: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	  }
	 private int updateRowsById(SQLiteDatabase db, Uri uri, ContentValues values, String table, String columnIdName, String selection, String[] selectionArgs)
	 {
		 int rowsUpdated;
		 String id = uri.getLastPathSegment();
		 if (TextUtils.isEmpty(selection)) {
			 rowsUpdated = db.update(table, 
					 values,
					 columnIdName + "=" + id, 
					 null);
		 } 
		 else {
			 rowsUpdated = db.update(table, 
					 values,
					 columnIdName + "=" + id 
					 + " and " 
					 + selection,
					 selectionArgs);
			
		 }
		 return rowsUpdated;
	 }
	 private Uri createAppendedUriAndNotifyChange(Uri originalUri, long entryId)
	 {
		 Uri _uri = null;
		 if (entryId > 0) {
            _uri = ContentUris.withAppendedId(originalUri, entryId);
            getContext().getContentResolver().notifyChange(_uri, null);    
		 }
		 return _uri;
	 }

}
