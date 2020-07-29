package com.example.saki.mytodo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.saki.mytodo.TodoTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saki on 2017/1/16.
 */

public class TodoTaskManager extends DatabaseManager {

	public TodoTaskManager(Context context) {
		super(context);
	}

	public void recreateTable() {
		super.recreateTable();
	}

	public long insert(ContentValues contentValues) {
		return super.insert(TableFields.TABLE, contentValues);
	}

	public long update(ContentValues contentValues, String whereClause, String[] whereArgs) {
		return super.update(TableFields.TABLE, contentValues, whereClause, whereArgs);
	}

	public boolean delete(String whereClause, String[] whereArgs) {
		return super.delete(TableFields.TABLE, whereClause, whereArgs);
	}

	public List<TodoTask> queryAll(int sortBy) {
		List<TodoTask> list = new ArrayList<TodoTask>();

		String query;
		switch (sortBy) {
			case 1:
				query = TableFields.TodoTask.SQL_SORT_BY_CREATE_TIME;
				break;
			case 2:
				query = TableFields.TodoTask.SQL_SORT_BY_LEVEL;
				break;
			default:
				query = TableFields.TodoTask.SQL_SORT_BY_DEFAULT;
				break;
		}
		Cursor cursor = database.rawQuery(query, null);
		if (cursor == null) {

		} else if (!cursor.moveToFirst()) {

		} else {
			int columnId = cursor.getColumnIndex(TableFields.TodoTask.ID);
			int columnRank = cursor.getColumnIndex(TableFields.TodoTask.RANK);
			int columnTitle = cursor.getColumnIndex(TableFields.TodoTask.TITLE);
			int columnContent = cursor.getColumnIndex(TableFields.TodoTask.CONTENT);
			int columnFlagCompleted = cursor.getColumnIndex(TableFields.TodoTask.FLAG_COMPLETED);
			do {
				int id = cursor.getInt(columnId);
				float rank = cursor.getFloat(columnRank);
				String title = cursor.getString(columnTitle);
				String content = cursor.getString(columnContent);
				String flagCompleted = cursor.getString(columnFlagCompleted);

				TodoTask task = new TodoTask();
				task.setId(id);
				task.setRank(rank);
				task.setTitle(title);
				task.setContent(content);
				task.setFlagCompleted(flagCompleted);
				list.add(task);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	public TodoTask queryById(String taskId) {
		TodoTask task = null;

		Cursor cursor = database.rawQuery(TableFields.TodoTask.SQL_QUERY_BY_ID, new String[]{taskId});
		if (cursor == null) {

		} else if (!cursor.moveToFirst()) {

		} else {
			int columnId = cursor.getColumnIndex(TableFields.TodoTask.ID);
			int columnRank = cursor.getColumnIndex(TableFields.TodoTask.RANK);
			int columnTitle = cursor.getColumnIndex(TableFields.TodoTask.TITLE);
			int columnContent = cursor.getColumnIndex(TableFields.TodoTask.CONTENT);
			int columnFlagCompleted = cursor.getColumnIndex(TableFields.TodoTask.FLAG_COMPLETED);

			int id = cursor.getInt(columnId);
			float rank = cursor.getFloat(columnRank);
			String title = cursor.getString(columnTitle);
			String content = cursor.getString(columnContent);
			String flagCompleted = cursor.getString(columnFlagCompleted);

			task = new TodoTask();
			task.setId(id);
			task.setRank(rank);
			task.setTitle(title);
			task.setContent(content);
			task.setFlagCompleted(flagCompleted);
		}
		cursor.close();
		return task;
	}
}
