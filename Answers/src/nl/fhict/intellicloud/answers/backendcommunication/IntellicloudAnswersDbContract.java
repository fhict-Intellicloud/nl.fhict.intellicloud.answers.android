package nl.fhict.intellicloud.answers.backendcommunication;


import android.provider.BaseColumns;

/**
 * IntellicloudAnswersDbContract.java
 * 
 * Database contract for creating local syncable database
 * There is no need to instantiate this class directly, please use inner classes
 * 
 * @author BartvanDrongelen
 *
 */
public class IntellicloudAnswersDbContract {
	public IntellicloudAnswersDbContract () {}
	
	public static abstract class QuestionsEntry implements BaseColumns {
		public static final String TABLE_NAME = "questions";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_ASKER = "asker";
		public static final String COLUMN_ANSWERER = "answerer";
		public static final String COLUMN_QUESTION = "question";
		public static final String COLUMN_QUESTIONSTATE = "question_state";
		public static final String COLUMN_DATE = "date";

	}
	public static abstract class AnswersEntry implements BaseColumns {
		public static final String TABLE_NAME = "answers";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_ANSWER = "answer";
		public static final String COLUMN_QUESTION = "question";
		public static final String COLUMN_ANSWERSTATE = "answer_state";
		
	}
	public static abstract class ReviewsEntry implements BaseColumns {
		public static final String TABLE_NAME = "reviews";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_REVIEW = "review";
		public static final String COLUMN_REVIEWER = "review";
		public static final String COLUMN_ANSWER = "answer";
		public static final String COLUMN_REVIEWSTATE = "review_state";
	
		
	}
	public static abstract class FeedbackEntry implements BaseColumns {
		public static final String TABLE_NAME = "feedback";
		public static final String COLUMN_ID = "id";
		
	}
	public static abstract class UsersEntry implements BaseColumns {
		public static final String TABLE_NAME = "feedback";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_FIRSTNAME = "first_name";
		public static final String COLUMN_LASTNAME = "last_name";
		public static final String COLUMN_INFIX = "infix";
		public static final String COLUMN_USERTYPE = "user_type";
		
		public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_FIRSTNAME, COLUMN_LASTNAME, COLUMN_INFIX, COLUMN_USERTYPE};
		
	}
	public static class CreateStatements{
		public static final String CREATE_TABLE_QUESTIONS = "create table "
				  + QuestionsEntry.TABLE_NAME + 
				"(" + QuestionsEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " 
				+ QuestionsEntry.COLUMN_QUESTION + " TEXT, "
				+ QuestionsEntry.COLUMN_ASKER + " INTEGER, "
				+ QuestionsEntry.COLUMN_ANSWERER + " INTEGER, "
				+ QuestionsEntry.COLUMN_DATE + " DATETIME, "
				+ QuestionsEntry.COLUMN_QUESTIONSTATE + " TEXT NOT NULL"
				+ " );";
		  public static final String CREATE_TABLE_ANSWERS = "create table "
		      + AnswersEntry.TABLE_NAME + 
	      		"(" + AnswersEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " 
	      		+ AnswersEntry.COLUMN_ANSWER + " TEXT, "
	      		+ AnswersEntry.COLUMN_QUESTION + " INTEGER, "
	      		+ AnswersEntry.COLUMN_ANSWERSTATE + " TEXT NOT NULL"
	      		+ " );";
		  public static final String CREATE_TABLE_REVIEWS = "create table "
		      + ReviewsEntry.TABLE_NAME + 
	      		"(" + ReviewsEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " 
	      		+ ReviewsEntry.COLUMN_REVIEW + " TEXT, "
	      		+ ReviewsEntry.COLUMN_REVIEWER + " INTEGER, "
	      		+ ReviewsEntry.COLUMN_ANSWER + " INTEGER, "
	      		+ ReviewsEntry.COLUMN_REVIEWSTATE + " TEXT NOT NULL"
	      		+ " );";
		  public static final String CREATE_TABLE_USERS = "create table "
		      + UsersEntry.TABLE_NAME + 
	      		"(" + UsersEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " 
	      		+ UsersEntry.COLUMN_FIRSTNAME + " TEXT, "
	      		+ UsersEntry.COLUMN_LASTNAME + " TEXT, "
	      		+ UsersEntry.COLUMN_INFIX + " TEXT, "
	      		+ UsersEntry.COLUMN_USERTYPE + " TEXT NOT NULL"
	      		+ " );";
		 

	}
}
