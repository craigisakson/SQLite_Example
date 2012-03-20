package com.runninghusky.spacetracker.sqlite.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The Class StartActivity.
 */
public class StartActivity extends Activity {
	
	/** The m list. */
	private SimpleAdapter mList;
	
	/** The dh. */
	private DataHelper dh;
	
	/** The m data. */
	private EditText mData;
	
	/** The m list view. */
	private ListView mListView;
	
	/** The m save. */
	private Button mSave;
	
	/** The ctx. */
	private Context ctx = this;
	
	/** The list. */
	private List<Item> list;
	
	/** The Constant CONTEXT_EDIT. */
	protected static final int CONTEXT_EDIT = 1;
	
	/** The Constant CONTEXT_DELETE. */
	protected static final int CONTEXT_DELETE = 2;

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
		setContentView(R.layout.main);

		mData = (EditText) findViewById(R.id.EditTextData);
		mListView = (ListView) findViewById(R.id.ListViewRecords);
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

		getData();
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	private void getData() {
		list = new ArrayList<Item>();
		this.dh = new DataHelper(this);
		list = this.dh.selectAllData();
		this.dh.close();
		if (list.size() > 0) {
			ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();
			for (Item i : list) {
				map.put("id", "Record Id:  " + Long.toString(i.getId()));
				map.put("data", i.getData());
				mylist.add(map);
				map = new HashMap<String, String>();
			}

			mList = new SimpleAdapter(this, mylist, R.layout.row, new String[] {
					"id", "data" }, new int[] { R.id.TextViewId,
					R.id.TextViewData });
			mListView.setAdapter(mList);

			/* Add Context-Menu listener to the ListView. */
			mListView
					.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
						// @Override
						public void onCreateContextMenu(ContextMenu menu,
								View v, ContextMenu.ContextMenuInfo menuInfo) {

							menu.add(0, CONTEXT_EDIT, 0, "Edit");
							menu.add(0, CONTEXT_DELETE, 1, "Delete");
						}
					});
			mListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					v.showContextMenu();
				}
			});

		}
	}

	/**
	 * Runs when a context item is selected
	 *
	 * @param item the menu item
	 * @return boolean
	 */
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXT_EDIT:
			sendEditIntent(list.get(menuInfo.position));
			break;
		case CONTEXT_DELETE:
			deleteRecord(list.get(menuInfo.position));
			break;
		default:
			return super.onContextItemSelected(item);
		}

		return true;
	}

	/**
	 * Save data.
	 */
	private void saveData() {
		this.dh = new DataHelper(this);
		long l = this.dh.insertData(String.valueOf(mData.getText()));
		this.dh.close();
		if (l == 0) {
			Toast.makeText(ctx, "An error occurred while entering the data...",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(ctx, "Record added...", Toast.LENGTH_SHORT).show();
			getData();
		}
	}

	/**
	 * Delete all.
	 */
	private void deleteAll() {
		this.dh = new DataHelper(this);
		this.dh.deleteData();
		this.dh.close();
		Toast.makeText(ctx, "Record/s deleted...", Toast.LENGTH_SHORT).show();
		Intent myIntent = new Intent(StartActivity.this, StartActivity.class);
		StartActivity.this.startActivity(myIntent);
		finish();
	}

	/**
	 * Send edit intent.
	 *
	 * @param i the i
	 */
	private void sendEditIntent(Item i) {
		Intent myIntent = new Intent(StartActivity.this, EditActivity.class);
		Log.d("sqlite", "i:  " + i.getData());
		myIntent.putExtra("Item", i);
		StartActivity.this.startActivity(myIntent);
		finish();
	}

	/**
	 * Delete record.
	 *
	 * @param i the i
	 */
	private void deleteRecord(Item i) {
		this.dh = new DataHelper(this);
		this.dh.deleteSingleData(i.getId());
		this.dh.close();
		getData();
	}

	/**
	 * Runs when a options menu is created
	 *
	 * @param menu the menu
	 * @return boolean
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * Runs when a menu item is selected
	 *
	 * @param item the menu item
	 * @return boolean
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MenuOptionDelete:
			deleteAll();
			break;
		}
		return true;
	}
}