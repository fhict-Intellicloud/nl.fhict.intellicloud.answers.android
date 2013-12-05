package nl.fhict.intellicloud.answers.backendcommunication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudAnswersDbContract.*;

public class LocalStorageSQLiteHelper extends SQLiteOpenHelper {
	  

	  private static final String DATABASE_NAME = "intellicloud_answers.db";
	  
	  //Current version: initial database version
	  //Please edit only if database structure has changed.
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statements
	  

	  public LocalStorageSQLiteHelper(Context context) {
		  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase db) {
	
	        db.execSQL(CreateStatements.CREATE_TABLE_ANSWERS);
	        db.execSQL(CreateStatements.CREATE_TABLE_QUESTIONS);
	        db.execSQL(CreateStatements.CREATE_TABLE_USERS);
	        db.execSQL(CreateStatements.CREATE_TABLE_REVIEWS);
	        // db.execSQL(CreateStatements.CREATE_TABLE_FEEDBACK); //TODO: determine if necessary
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		    Log.w(LocalStorageSQLiteHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME); 
		    db.execSQL("DROP TABLE IF EXISTS " + QuestionsEntry.TABLE_NAME);
		    db.execSQL("DROP TABLE IF EXISTS " + AnswersEntry.TABLE_NAME);
		    db.execSQL("DROP TABLE IF EXISTS " + UsersEntry.TABLE_NAME);  
		    //db.execSQL("DROP TABLE IF EXISTS " + FeedbackEntry.TABLE_NAME);   
		    
		    onCreate(db);
	  }


}
