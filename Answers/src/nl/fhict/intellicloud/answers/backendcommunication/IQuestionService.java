package nl.fhict.intellicloud.answers.backendcommunication;

import java.util.ArrayList;

import nl.fhict.intellicloud.answers.*;
/**
 * IQuestionService.java
 * 
 * @author Bart van Drongelen
 *
 * Interface defining communication between backend and the application for functionality related to Questions.
 *
 */
public interface IQuestionService {
	/**
	 * Gets a specific {@link Question} that belongs to the requested ID. 
	 * @param id The id of the answer to retrieve (note: should this be a string?)
	 * @return The requested Question object, or null if nothing is found.
	 */
	Question GetQuestion(int id);
	/**
	 * Gets all {@link Question} objects currently available
	 * @return An {@link ArrayList} with all available questions;
	 */
	ArrayList<Question> GetQuestions();
	/**
	 * Gets all available {@link Question} objects that correspond to the set filters
	 * @param employeeid The id of the employee to filter for, or -1 for all employees
	 * @return An {@link ArrayList} with all questions which match the filters
	 */
	ArrayList<Question> GetQuestions(int employeeId);
}
