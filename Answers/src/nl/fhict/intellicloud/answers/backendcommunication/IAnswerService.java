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
	void CreateAnswer(Answer answer);
	/**
	 * Gets a specific {@link Answer} that belongs to the requested ID. 
	 * @param id The id of the answer to retrieve (note: should this be a string?)
	 * @return The requested Answer object, or null if nothing is found.
	 */
	Answer GetAnswer(int id);
	/**
	 * Gets all {@link Answer} objects currently available
	 * @return An {@link ArrayList} with all available answers
	 */
	ArrayList<Answer> getAnswers();
	/**
	 * Gets all available {@link Answer} objects that correspond to the set filters
	 * @param employeeid The id of the employee to filter for, or -1 for all employees
	 * @param answerState The {@link AnswerState} to filter for, or null to seek for all.
	 * @return An {@link ArrayList} with all answers which match the filters
	 */
	ArrayList<Answer> getAnswers(int employeeid, AnswerState answerState);
	/**
	 * Updates an {@link Answer} with a new {@link AnswerState}
	 * @param answer The Answer to change the state for.
	 */
	void UpdateAnswer(Answer answer);
	
}
