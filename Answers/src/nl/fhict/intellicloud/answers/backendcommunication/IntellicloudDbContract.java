package nl.fhict.intellicloud.answers.backendcommunication;


import java.util.Date;

import nl.fhict.intellicloud.answers.Answer;
import nl.fhict.intellicloud.answers.FeedbackState;
import nl.fhict.intellicloud.answers.FeedbackType;
import nl.fhict.intellicloud.answers.Question;
import nl.fhict.intellicloud.answers.User;
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
public class IntellicloudDbContract {
	public IntellicloudDbContract () {}
	
	public static abstract class QuestionsEntry implements BaseColumns {
		public static final String TABLE_NAME = "questions";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_BACKEND_ID = "backend_id";
		public static final String COLUMN_ASKER_ID = "asker_id";
		public static final String COLUMN_ANSWERER_ID = "answerer_id";
		public static final String COLUMN_QUESTION = "question";
		public static final String COLUMN_QUESTIONSTATE = "question_state";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_IS_PRIVATE = "is_private";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_ANSWER_ID = "answer";

	}
	public static abstract class AnswersEntry implements BaseColumns {
		public static final String TABLE_NAME = "answers";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_BACKEND_ID = "backend_id";
		public static final String COLUMN_ANSWER = "answer";
		public static final String COLUMN_ANSWERER_ID = "answerer_id";
//		public static final String COLUMN_QUESTION_ID = "question";
		public static final String COLUMN_ANSWERSTATE = "answer_state";
		public static final String COLUMN_DATE = "date";
		
		
		
	}
	public static abstract class ReviewsEntry implements BaseColumns {
		public static final String TABLE_NAME = "reviews";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_BACKEND_ID = "backend_id";
		public static final String COLUMN_REVIEW = "review";
		public static final String COLUMN_REVIEWER_ID = "reviewer_id";
		public static final String COLUMN_ANSWER_ID = "answer_id";
		public static final String COLUMN_REVIEWSTATE = "review_state";
		public static final String COLUMN_DATE = "date";
		
	}
	public static abstract class FeedbackEntry implements BaseColumns {
		public static final String TABLE_NAME = "feedback";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_BACKEND_ID = "backend_id";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_FEEDBACK = "feedback";
		public static final String COLUMN_USER_ID = "user_id";
		public static final String COLUMN_QUESTION_ID = "question_id";
		public static final String COLUNN_ANSWER_ID = "answer_id";
		public static final String COLUMN_FEEDBACK_STATE = "feedback_state";
		public static final String COLUMN_FEEDBACK_TYPE = "feedback_type";
		
	}
	public static abstract class UsersEntry implements BaseColumns {
		public static final String TABLE_NAME = "feedback";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_BACKEND_ID = "backend_id";
		public static final String COLUMN_FIRSTNAME = "first_name";
		public static final String COLUMN_LASTNAME = "last_name";
		public static final String COLUMN_INFIX = "infix";
		public static final String COLUMN_USERTYPE = "user_type";
		
		public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_BACKEND_ID, COLUMN_FIRSTNAME, COLUMN_LASTNAME, COLUMN_INFIX, COLUMN_USERTYPE};
		
	}
	public static class CreateStatements{
		public static final String CREATE_TABLE_QUESTIONS = "create table "
				  + QuestionsEntry.TABLE_NAME + 
				"(" + QuestionsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ QuestionsEntry.COLUMN_BACKEND_ID + " INTEGER, "
				+ QuestionsEntry.COLUMN_QUESTION + " TEXT, "
				+ QuestionsEntry.COLUMN_ASKER_ID + " INTEGER, "
				+ QuestionsEntry.COLUMN_ANSWERER_ID + " INTEGER, "
				+ QuestionsEntry.COLUMN_ANSWER_ID + " INTEGER, "
				+ QuestionsEntry.COLUMN_DATE + " DATETIME, "
				+ QuestionsEntry.COLUMN_IS_PRIVATE + " INTEGER,"
	      		+ QuestionsEntry.COLUMN_TITLE + " TEXT, "
				+ QuestionsEntry.COLUMN_QUESTIONSTATE + " TEXT NOT NULL"
				+ " );";
		  public static final String CREATE_TABLE_ANSWERS = "create table "
		      + AnswersEntry.TABLE_NAME + 
	      		"(" + AnswersEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	      		+ AnswersEntry.COLUMN_BACKEND_ID + " INTEGER, "
	      		+ AnswersEntry.COLUMN_ANSWER + " TEXT, "
	      		+ AnswersEntry.COLUMN_ANSWERER_ID + "INTEGER, "
//	      		+ AnswersEntry.COLUMN_QUESTION_ID + " INTEGER, "
	      		+ AnswersEntry.COLUMN_ANSWERSTATE + " TEXT NOT NULL,"
	      		
	      		+ " );";
		  public static final String CREATE_TABLE_REVIEWS = "create table "
		      + ReviewsEntry.TABLE_NAME + 
	      		"(" + ReviewsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
	      		+ ReviewsEntry.COLUMN_BACKEND_ID + " INTEGER,"
	      		+ ReviewsEntry.COLUMN_REVIEW + " TEXT, "
	      		+ ReviewsEntry.COLUMN_REVIEWER_ID + " INTEGER, "
	      		+ ReviewsEntry.COLUMN_ANSWER_ID + " INTEGER, "
	      		+ ReviewsEntry.COLUMN_REVIEWSTATE + " TEXT NOT NULL"
	      		+ " );";
		  public static final String CREATE_TABLE_USERS = "create table "
		      + UsersEntry.TABLE_NAME + 
	      		"(" + UsersEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
	      		+ UsersEntry.COLUMN_BACKEND_ID + " INTEGER,"
	      		+ UsersEntry.COLUMN_FIRSTNAME + " TEXT, "
	      		+ UsersEntry.COLUMN_LASTNAME + " TEXT, "
	      		+ UsersEntry.COLUMN_INFIX + " TEXT, "
	      		+ UsersEntry.COLUMN_USERTYPE + " TEXT NOT NULL"
	      		+ " );";
		  public static final String CREATE_TABLE_FEEDBACK = "create table "
			      + FeedbackEntry.TABLE_NAME + 
		      		"(" + FeedbackEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
		      		+ FeedbackEntry.COLUMN_BACKEND_ID + " INTEGER, "
		      		+ FeedbackEntry.COLUMN_FEEDBACK + " TEXT, "
		      		+ FeedbackEntry.COLUMN_FEEDBACK_STATE + " TEXT,"
		      		+ FeedbackEntry.COLUMN_FEEDBACK_TYPE + " TEXT,"
		      		+ FeedbackEntry.COLUMN_QUESTION_ID + " INTEGER,"
		      		+ FeedbackEntry.COLUNN_ANSWER_ID + " INTEGER,"
		      		+ FeedbackEntry.COLUMN_USER_ID + " INTEGER,"
		      		
		      		+ " );";
			 
		 

	}
}
