package com.example.saki.mytodo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saki.mytodo.db.DatabaseManager;
import com.example.saki.mytodo.db.TableFields;
import com.example.saki.mytodo.db.TodoTaskManager;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int sortBy = 0;
    private ListView lvTasks;
    private TaskAdapter taskAdapter;
    private TodoTaskManager todoTaskManager;
    private List<TodoTask> tasks = new ArrayList<TodoTask>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sort(sortBy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        todoTaskManager.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_clear:
                remove();
                return true;
            case R.id.sort_by_default:
                sortBy = 0;
                sort(sortBy);
                return true;
            case R.id.sort_by_create_time:
                sortBy = 1;
                sort(sortBy);
                return true;
            case R.id.sort_by_level:
                sortBy = 2;
                sort(sortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sort(int i) {
        tasks = todoTaskManager.queryAll(i);
        taskAdapter.notifyDataSetChanged();
    }

    public void remove() {
        new android.app.AlertDialog.Builder(MainActivity.this)
                .setTitle("Remove the database")
                .setMessage("Clear all the tasks?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todoTaskManager.recreateTable();
                        sort(0);
                    }
                }).setNegativeButton("No", null)
                .show();
    }

    private void initComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.action_easy_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(MainActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = String.valueOf(taskEditText.getText());
                                if (title == null || title.trim().equals("")) {
                                    Toast.makeText(MainActivity.this, "The title cannot be empty or too long.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(TableFields.TodoTask.TITLE, title);
                                    contentValues.put(TableFields.TodoTask.CREATE_TIME, String.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())));
                                    todoTaskManager.insert(contentValues);
                                    sort(sortBy);
                                    Toast.makeText(MainActivity.this, "Saved.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        todoTaskManager = new TodoTaskManager(this);
        tasks = todoTaskManager.queryAll(sortBy);

        taskAdapter = new TaskAdapter();
        lvTasks = (ListView) findViewById(R.id.list_todo);
        lvTasks.setAdapter(taskAdapter);
    }

    static class ViewHolder {
        CheckBox taskFlag;
        TextView taskTitle;
        Button taskDelete;
    }

    class TaskAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public TaskAdapter() {
            layoutInflater = LayoutInflater.from(MainActivity.this);
        }

        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Object getItem(int pos) {
            return tasks.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return tasks.get(pos).getId();
        }

        @Override
        public View getView(final int pos, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_todo, null);
                viewHolder = new ViewHolder();
                viewHolder.taskFlag = (CheckBox) convertView.findViewById(R.id.task_flag);
                viewHolder.taskTitle = (TextView) convertView.findViewById(R.id.task_title);
                viewHolder.taskDelete = (Button) convertView.findViewById(R.id.task_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.taskFlag.setChecked(tasks.get(pos).getFlagCompleted().equals("Y"));
            viewHolder.taskFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b == true) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(TableFields.TodoTask.FLAG_COMPLETED, "Y");
                        contentValues.put(TableFields.TodoTask.COMPLETE_TIME, String.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())));
                        todoTaskManager.update(contentValues, TableFields.TodoTask.ID + " = ?", new String[] {String.valueOf(getItemId(pos))});
                    }
                    else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(TableFields.TodoTask.FLAG_COMPLETED, "N");
                        contentValues.put(TableFields.TodoTask.COMPLETE_TIME, "");
                        todoTaskManager.update(contentValues, TableFields.TodoTask.ID + " = ?", new String[] {String.valueOf(getItemId(pos))});
                    }
                    sort(sortBy);
                }
            });
            viewHolder.taskTitle.setText(tasks.get(pos).getTitle());
            viewHolder.taskTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String taskId = String.valueOf(getItemId(pos));
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra(DetailActivity.TASK_ID, taskId);
                    startActivity(intent);
                }
            });
            viewHolder.taskDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new android.app.AlertDialog.Builder(MainActivity.this).setTitle("Delete Task")
                            .setMessage("Delete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (todoTaskManager.delete("_id = ?", new String[]{String.valueOf(getItemId(pos))})) {
                                        sort(sortBy);
                                    }
                                }
                            }).setNegativeButton("No", null).show();
                }
            });
            return convertView;
        }
    }
}
