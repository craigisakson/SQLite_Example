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

public class DataHelper {
	private static final String DATABASE_NAME = "sqlite_example.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_DATA = "data";
	private Context context;
	private SQLiteDatabase db;
	private SQLiteStatement insertData;
	private static final String INSERT_DATA = "insert into " + TABLE_DATA
			+ "(data) " + "values (?)";

	public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
		this.insertData = this.db.compileStatement(INSERT_DATA);
	}

	public long insertData(String data) {
		this.insertData.bindString(1, data);
		return this.insertData.executeInsert();
	}

	public void updateData(String data, long id) {
		ContentValues vals = new ContentValues();
		vals.put("data", data);
		this.db.update(TABLE_DATA, vals, "id=" + id, null);
	}

	public void deleteData() {
		this.db.delete(TABLE_DATA, null, null);
	}

	public void deleteSingleData(long id) {
		this.db.delete(TABLE_DATA, "id = " + id, null);
	}

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

	// ---closes the database---
	public void close() {
		this.db.close();
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		private static final String INSERT_DATA = "insert into " + TABLE_DATA
				+ "(id, data) " + "values (?, ?)";

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_DATA
					+ "(id INTEGER PRIMARY KEY, data TEXT) ");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Example",
					"Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
			onCreate(db);
		}
	}
}