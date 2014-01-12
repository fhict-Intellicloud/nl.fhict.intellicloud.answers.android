package nl.fhict.intellicloud.answers.unit.tests;

import java.util.ArrayList;
import java.util.Date;
import nl.fhict.intellicloud.answers.Question;
import nl.fhict.intellicloud.answers.QuestionState;
import nl.fhict.intellicloud.answers.User;
import nl.fhict.intellicloud.answers.UserType;
import nl.fhict.intellicloud.answers.backendcommunication.IntellicloudDbContract.QuestionsEntry;
import nl.fhict.intellicloud.answers.backendcommunication.LocalStorageSQLiteHelper;
import nl.fhict.intellicloud.answers.backendcommunication.QuestionDataSource;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

/**
 * A class to test the functionality of the QuestionDataSource class
 * @author Teun
 *
 */
public class QuestionDataSourceTest extends AndroidTestCase {
	
	//The data source we are testing
	private QuestionDataSource qds;
	
	//The test questions to insert into the test database 
	private Question q1;
	private Question q2;
	
	//The helper to add the question to the database
	private LocalStorageSQLiteHelper dbHelper;
	
	/**
	 * The setUp method is called before each test to have the same setup every time
	 */
	@Override
	public void setUp() throws Exception {
		//We use a test context to create a test database with the test_ prefix
		RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
		qds = new QuestionDataSource(context);
		
		//We initialize the helper to add questions to the database because there's no create question method
		dbHelper = new LocalStorageSQLiteHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		User asker = new User(2, "Test", "User", "end", UserType.Customer);
		User answerer = new User(2, "Test", "User", "end", UserType.Employee);
		
		q1 = new Question(1, "This is a question", asker, answerer, QuestionState.Open, new Date());
		q2 = new Question(2, "This is a another question", asker, answerer, QuestionState.Open, new Date());
		
		ContentValues values = new ContentValues();
		values.put(QuestionsEntry.COLUMN_ID, q1.getId());
		values.put(QuestionsEntry.COLUMN_QUESTION, q1.getQuestion());
		values.put(QuestionsEntry.COLUMN_ASKER_ID, q1.getAsker().getId());
		values.put(QuestionsEntry.COLUMN_ANSWERER_ID, q1.getAnwserer().getId());
		values.put(QuestionsEntry.COLUMN_QUESTIONSTATE, q1.getQuestionState().toString());
		values.put(QuestionsEntry.COLUMN_DATE, q1.getDate().toString());
		
		db.insert(QuestionsEntry.TABLE_NAME, null, values);
		
		values = new ContentValues();
		values.put(QuestionsEntry.COLUMN_ID, q2.getId());
		values.put(QuestionsEntry.COLUMN_QUESTION, q2.getQuestion());
		values.put(QuestionsEntry.COLUMN_ASKER_ID, q2.getAsker().getId());
		values.put(QuestionsEntry.COLUMN_ANSWERER_ID, q2.getAnwserer().getId());
		values.put(QuestionsEntry.COLUMN_QUESTIONSTATE, q2.getQuestionState().toString());
		values.put(QuestionsEntry.COLUMN_DATE, q2.getDate().toString());
		
		db.insert(QuestionsEntry.TABLE_NAME, null, values);
		
		dbHelper.close();
	}
	
	/**
	 * Test to check the functionality of the getQuestionById method
	 */
	public void testGetQuestionById() {
		Question resultQuestion = qds.GetQuestion(q1.getId());
		
		assertEquals(q1.getQuestion(), resultQuestion.getQuestion());
		assertEquals(q1.getQuestionState(), resultQuestion.getQuestionState());
	}
	
	/**
	 * Test the functionality of the  getQuestions method
	 */
	public void testGetQuestions() {
		ArrayList<Question> resultQuestion = qds.GetQuestions();
		
		assertEquals(2, resultQuestion.size());
	}
	
	/**
	 * Gets called at the end of each test to clean up for the next test
	 */
	@Override
	public void tearDown() throws Exception {
        super.tearDown();
    }

}
