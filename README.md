# My-Todo #

## Introduction ##
This is a simple Android to-do list app which I write in order to learn Android development. Although it is kind of simple, I still think it's a good sample to get to understand the fundamental concepts like activity, intent, and the uses of some UI controls, such as TextView, EditText, ListView, CheckBox, and so on.

## About My Code ##
* ***Needed softwares or tools***
  * [Android Studio](https://developer.android.com/studio/index.html) - The official Android IDE.
  * [Genymotion](https://www.genymotion.com/features/) - If you do not have an Android device, this will be a good emulator.
  * [Stetho](http://facebook.github.io/stetho/) - A debug bridge for Android applications which I use for SQLite databse inspection.
* ***Data field***

		_id integer primary key autoincrement,
		rank real default 0,
		title varchar(30) default,
		content varchar(200) default '',
		flag_completed char(1) default 'N',
		create_time datetime,
		complete_time datetime
* ***Functions***
	* `Add` 
	  * easy_add() - Add a task by setting a title.
	  * add() - Add a task by setting more detailed information.
	* `Delete`
	  * Delete a task.
	* `Check`
	  * Mark a finished task.
	* `Sort`
	  * Sort tasks by created time.
	  * Sort tasks by level.
	  * Sort tasks by default.
