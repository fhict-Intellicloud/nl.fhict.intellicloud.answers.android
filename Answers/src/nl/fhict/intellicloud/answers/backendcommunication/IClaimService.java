package nl.fhict.intellicloud.answers.backendcommunication;

public interface IClaimService {
	void claimQuestion(int questionId) ;
	void releaseClaimQuestion(int questionId);
	boolean isQuestionClaimed(int questionId);
}
