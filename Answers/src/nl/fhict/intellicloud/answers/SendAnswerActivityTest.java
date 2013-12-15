package nl.fhict.intellicloud.answers;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import junit.framework.Assert;

import nl.fhict.intellicloud.R;
import nl.fhict.intellicloud.answers.backendcommunication.DummyBackend;
import nl.fhict.intellicloud.answers.backendcommunication.IAnswerService;
import nl.fhict.intellicloud.answers.backendcommunication.IQuestionService;

public class SendAnswerActivityTest extends ActivityInstrumentationTestCase2<SendAnswerActivity>{

    public SendAnswerActivityTest(Class<SendAnswerActivity> activityClass) {
        super(activityClass);
    }

    /**
     * Test Submitting an answer
     * @throws Throwable
     */
    public void testAddAnswer() throws Throwable {
        final IQuestionService qService = new DummyBackend();
        final IAnswerService aService = new DummyBackend();
        final Question question = qService.GetQuestion(1);
        final String answerText = "This is the test answer";
        final Answer expected = new Answer(answerText, question.getAnwserer(), AnswerState.UnderReview);

        // Create an intent
        Intent openIntent = new Intent(getActivity().getApplicationContext(), SendAnswerActivity.class);
        openIntent.putExtra("questionInt", question.getId());
        // Add the intent to the activity
        setActivityIntent(openIntent);
        // Start the activity
        Activity activity = getActivity();

        final EditText etAnswer = (EditText) activity.findViewById(R.id.etAnswer);
        activity.runOnUiThread( new Runnable() {
            @Override
            public void run() {
                etAnswer.setText(answerText);
                getActivity().addAnswer();

                Answer actual = aService.GetAnswer(question.getId());
                Assert.assertEquals(expected, actual);
            }
        });
    }

}
