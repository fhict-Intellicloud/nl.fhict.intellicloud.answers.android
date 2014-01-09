package nl.fhict.intellicloud.answers;

import nl.fhict.intellicloud.R;
import nl.fhict.intellicloud.answers.backendcommunication.DummyBackend;
import nl.fhict.intellicloud.answers.backendcommunication.IReviewService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddReviewActivity extends Activity {
	
	EditText etReviewField;
	IReviewService iReviewService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_review);
		
		iReviewService = new DummyBackend();
		
		Button btnSendReview = (Button) findViewById(R.id.btnSendReview);
		btnSendReview.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				etReviewField = (EditText) findViewById(R.id.etReviewField);
				Review review = new Review(etReviewField.getText().toString(), null, null, null);
				iReviewService.CreateReview(review);
				Intent intent = new Intent(AddReviewActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
}
