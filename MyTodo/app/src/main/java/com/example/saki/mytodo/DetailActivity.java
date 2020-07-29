package com.example.saki.mytodo;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.saki.mytodo.db.TableFields;
import com.example.saki.mytodo.db.TodoTaskManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Saki on 2017/1/16.
 */

public class DetailActivity extends AppCompatActivity {

	private static final String TAG = "DetailActivity";
	public static final String TASK_ID = "TASK_ID";

	private EditText editTitle;
	private EditText editContent;
	private RatingBar ratingBar;

	private TodoTaskManager todoTaskManager;
	private boolean flagForUpdate = false;
	private String taskId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initComponents();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		todoTaskManager.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_cancel:
				finish();
				Toast.makeText(this, "No change.", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.action_save:
				save();
				Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void initComponents() {
		editTitle = (EditText) findViewById(R.id.edit_title);
		editContent = (EditText) findViewById(R.id.edit_content);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		todoTaskManager = new TodoTaskManager(this);

		taskId = getIntent().getStringExtra(TASK_ID);
		if(taskId != null){
			TodoTask todoTask = todoTaskManager.queryById(taskId);
			if(todoTask != null){
				flagForUpdate = true;
				editTitle.setText(todoTask.getTitle());
				editTitle.setSelection(todoTask.getTitle().length());
				editContent.setText(todoTask.getContent());
				editContent.setSelection(todoTask.getContent().length());
				ratingBar.setRating(todoTask.getRank());
			}
		}
	}

	private void save() {
		String title = editTitle.getText().toString();
		String content = editContent.getText().toString();
		float rank = ratingBar.getRating();
		if (title == null || title.trim().equals("")) {
			Toast.makeText(this, "The title cannot be empty or too long.", Toast.LENGTH_SHORT).show();
		}
		else {
			ContentValues contentValues = new ContentValues();
			contentValues.put(TableFields.TodoTask.TITLE, title);
			contentValues.put(TableFields.TodoTask.CONTENT, content);
			contentValues.put(TableFields.TodoTask.RANK, rank);
			if(flagForUpdate) {
				todoTaskManager.update(contentValues, TableFields.TodoTask.ID + " = ?", new String[]{taskId});
			}
			else {
				contentValues.put(TableFields.TodoTask.CREATE_TIME, String.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())));
				todoTaskManager.insert(contentValues);
			}
			finish();
		}
	}
}
