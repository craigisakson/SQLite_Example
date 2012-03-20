package com.runninghusky.spacetracker.sqlite.example;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * The Class DataHelper.
 */
public class DataHelper {

	/** The Constant DATABASE_NAME. */
	private static final String DATABASE_NAME = "sqlite_example.db";

	/** The Constant DATABASE_VERSION. */
	private static final int DATABASE_VERSION = 1;

	/** The Constant TABLE_DATA. */
	private static final String TABLE_DATA = "data";

	/** The context context. */
	private Context context;

	/** The database db. */
	private SQLiteDatabase db;

	/** The insert data. */
	private SQLiteStatement insertData;

	/** The Constant INSERT_DATA. */
	private static final String INSERT_DATA = "insert into " + TABLE_DATA
			+ "(data) " + "values (?)";

	/**
	 * Instantiates a new data helper.
	 * 
	 * @param context the context
	 */
	public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
		this.insertData = this.db.compileStatement(INSERT_DATA);
	}

	/**
	 * Insert data.
	 * 
	 * @param data to save
	 * @return the number of inserted
	 */
	public long insertData(String data) {
		this.insertData.bindString(1, data);
		return this.insertData.executeInsert();
	}

	/**
	 * Update data.
	 * 
	 * @param data to save
	 * @param id of the record
	 */
	public void updateData(String data, long id) {
		ContentValues vals = new ContentValues();
		vals.put("data", data);
		this.db.update(TABLE_DATA, vals, "id=" + id, null);
	}

	/**
	 * Delete data deletes all data.
	 */
	public void deleteData() {
		this.db.delete(TABLE_DATA, null, null);
	}

	/**
	 * Delete a single record of data.
	 * 
	 * @param id of the record
	 */
	public void deleteSingleData(long id) {
		this.db.delete(TABLE_DATA, "id = " + id, null);
	}

	/**
	 * Select all data.
	 * 
	 * @return the list of Items
	 */
	public List<Item> selectAllData() {
		List<Item> list = new ArrayList<Item>();
		Cursor cursor = this.db.rawQuery("SELECT * FROM " + TABLE_DATA, null);
		if (cursor.moveToFirst()) {
			do {
				Item i = new Item();
				i.setId(cursor.getLong(0));
				i.setData(cursor.getString(1));
				list.add(i);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	/**
	 * Closes the database.
	 */
	public void close() {
		this.db.close();
	}

	/**
	 * The Class OpenHelper.
	 */
	private static class OpenHelper extends SQLiteOpenHelper {

		/**
		 * Instantiates a new open helper.
		 * 
		 * @param context the context
		 */
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * On create called when DB is created.
		 * 
		 * @param the database
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_DATA
					+ "(id INTEGER PRIMARY KEY, data TEXT) ");
		}

		/**
		 * On upgrade called when DB is upgraded.
		 * 
		 * @param the database
		 * @param the old version number
		 * @param the new version number
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Example",
					"Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
			onCreate(db);
		}
	}
}