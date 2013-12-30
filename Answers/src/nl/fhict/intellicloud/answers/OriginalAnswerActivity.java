package nl.fhict.intellicloud.answers;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OriginalAnswerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_original_answer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.original_answer, menu);
		return true;
	}

}
