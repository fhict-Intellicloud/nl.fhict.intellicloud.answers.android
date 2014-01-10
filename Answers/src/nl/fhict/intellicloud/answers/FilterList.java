package nl.fhict.intellicloud.answers;

import java.util.ArrayList;

public class FilterList {

	public ArrayList<Question> createListWithFilter(ArrayList<Question> listToFilter, int filterId) {
		ArrayList<Question> list = new ArrayList<Question>();
		switch(filterId) {
		case 0:
			list = listToFilter;
			break;
		case 1:
			for(Question q : listToFilter){
				if(q.getQuestionState().equals(QuestionState.Open)){
					list.add(q);
				}
			}
			break;
		case 2:
			for(Question q : listToFilter){
				if(q.getQuestionState().equals(QuestionState.Closed)){
					list.add(q);
				}
			}
			break;
		case 3:
			for(Question q : listToFilter){
				if(q.getQuestionState().equals(QuestionState.UpForAnswer)){
					list.add(q);
				}
			}
			break;
		case 4:
			for(Question q : listToFilter){
				if(q.getQuestionState().equals(QuestionState.UpForFeedback)){
					list.add(q);
				}
			}
			break;
		}
		return list;
	}       
}
