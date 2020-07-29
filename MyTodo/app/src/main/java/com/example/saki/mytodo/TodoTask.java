package com.example.saki.mytodo;

/**
 * Created by Saki on 2017/1/13.
 */

public class TodoTask {

	private int id;
	private float rank;
	private String title;
	private String content;
	private String flagCompleted;
	private String completedTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getRank() {
		return rank;
	}

	public void setRank(float rank) {
		this.rank = rank;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFlagCompleted() {
		return flagCompleted;
	}

	public void setFlagCompleted(String flagCompleted) {
		this.flagCompleted = flagCompleted;
	}

	public String getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(String completedTime) {
		this.completedTime = completedTime;
	}
}
