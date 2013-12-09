package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;

import nl.fhict.intellicloud.answers.*;

/**
 * IAnswerService.java
 * 
 * @author Bart van Drongelen
 *
 * Interface defining communication between backend and the application for functionality related to Answers.
 *
 */
public interface IAnswerService {
	/**
	 * Adds an answer to the database and sends it to be processed.
	 * @param answer The answer to add.
	 */
	void CreateAnswer(Answer answer, int questionId);
	/**
	 * Gets a specific {@link Answer} that belongs to the requested ID. 
	 * @param id The id of the answer to retrieve 
	 * @return The requested Answer object, or null if nothing is found.
	 */
	Answer GetAnswer(int id);
	/**
	 * Gets a specific {@link Answer} that belongs to the requested {@link Question} ID. 
	 * @param id The id of the question to retrieve the answer for
	 * @return The requested Answer object, or null if nothing is found.
	 */
	Answer GetAnswerUsingQuestion(int questionId);
	/**
	 * Gets all {@link Answer} objects currently available
	 * @return An {@link ArrayList} with all available answers
	 */
	ArrayList<Answer> GetAnswers();
	/**
	 * Gets all available {@link Answer} objects that correspond to the set filters
	 * @param employeeid The id of the employee to filter for, or -1 for all employees
	 * @param answerState The {@link AnswerState} to filter for, or null to seek for all.
	 * @return An {@link ArrayList} with all answers which match the filters
	 */
	ArrayList<Answer> GetAnswers(int employeeId, AnswerState answerState);
	/**
	 * Updates an {@link Answer}
	 * @param answer The Answer to change the state for.
	 */
	void UpdateAnswer(Answer answer);
	
	
	
}
