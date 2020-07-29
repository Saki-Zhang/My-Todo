package com.example.saki.mytodo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Saki on 2017/1/14.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, TableFields.DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(TableFields.TodoTask.SQL_CREATE_TASKS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		if (i1 > i) {
			sqLiteDatabase.execSQL(TableFields.TodoTask.SQL_DROP_TASKS);
			sqLiteDatabase.execSQL(TableFields.TodoTask.SQL_CREATE_TASKS);
		}
	}
}
