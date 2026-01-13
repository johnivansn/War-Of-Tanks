package io;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class ScoreData {

	private String date;
	private int score;
	private String nickname;
	public ScoreData(int score,String nickname) {
		this.score = score;
		this.nickname = nickname;
		Date today = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		date = format.format(today);
	}

	public ScoreData() {

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
