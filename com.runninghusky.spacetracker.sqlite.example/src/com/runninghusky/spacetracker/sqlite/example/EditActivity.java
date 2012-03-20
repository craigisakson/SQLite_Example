package com.runninghusky.spacetracker.sqlite.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The Class EditActivity.
 */
public class EditActivity extends Activity {
	
	/** The Item i. */
	private Item i;
	
	/** The EditText data. */
	private EditText mData;
	
	/** The Button save. */
	private Button mSave;
	
	/** The Context ctx. */
	private Context ctx = this;
	
	/** The Datahelper dh. */
	private DataHelper dh;

	/**
	 * Prevents the activity from restarted on orientation change
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Called when the activity is first created.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		i = (Item) getIntent().getSerializableExtra("Item");

		mData = (EditText) findViewById(R.id.EditTextData);
		mData.setText(i.getData());
		mSave = (Button) findViewById(R.id.ButtonSave);
		mSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (String.valueOf(mData).length() > 0) {
					saveData();
				} else {
					Toast.makeText(ctx, "Please enter some data",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * Save data saves the data and launches the start activity.
	 */
	private void saveData() {
		this.dh = new DataHelper(this);
		this.dh.updateData(String.valueOf(mData.getText()), i.getId());
		this.dh.close();
		Toast
				.makeText(ctx, "Changes saved successfully...",
						Toast.LENGTH_SHORT).show();

		Intent myIntent = new Intent(EditActivity.this, StartActivity.class);
		EditActivity.this.startActivity(myIntent);
		finish();
	}

	/**
	 * onKeyDown will check when the back button is pressed and cancel the opperation
	 * 
	 * @param keyCode the code of the key pressed
	 * @param event the event
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Toast.makeText(ctx, "Changes discarded...", Toast.LENGTH_SHORT)
					.show();
			Intent myIntent = new Intent(EditActivity.this, StartActivity.class);
			EditActivity.this.startActivity(myIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
