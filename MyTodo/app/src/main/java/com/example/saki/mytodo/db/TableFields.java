package com.example.saki.mytodo.db;

public class TableFields {

	public static final String DB_NAME = "com.example.saki.mytodo.db";
	public static final String TABLE = "tasks";

	public class TodoTask {

		public static final String SQL_SORT_BY_LEVEL =
				"select * from " + TABLE + " order by rank desc, complete_time desc, create_time desc";

		public static final String SQL_SORT_BY_CREATE_TIME =
				"select * from " + TABLE + " order by create_time desc";

		public static final String SQL_SORT_BY_DEFAULT =
				"select * from " + TABLE + " order by flag_completed, complete_time desc, create_time desc";

		public static final String SQL_QUERY_BY_ID =
				"select * from " + TABLE + " where _id = ?";

		public static final String SQL_DROP_TASKS = "drop table if exists " + TABLE;
		public static final String SQL_CREATE_TASKS = "create table if not exists " + TABLE
				+ " ( _id integer primary key autoincrement,"
				+ "   rank real default 0,"
				+ "   title varchar(30) default \"\","
				+ "   content varchar(200) default \"\","
				+ "   flag_completed char(1) default \"N\","
				+ "   create_time datetime,"
				+ "   complete_time datetime"
				+ " )";

		public static final String ID = "_id";
		public static final String RANK = "rank";
		public static final String TITLE = "title";
		public static final String CONTENT = "content";
		public static final String FLAG_COMPLETED = "flag_completed";
		public static final String CREATE_TIME = "create_time";
		public static final String COMPLETE_TIME = "complete_time";
	}
}
