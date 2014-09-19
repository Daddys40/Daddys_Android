package com.daddys40.data;

import java.io.Serializable;
/**
 * 컨텐츠 하나를 담는 클래스
 * @author Kim
 *
 */
public class QnAData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mQuestion;
	private String mAnswer;
	private String mTitle;
	private int mWeek;
	public void setQustion(String question){
		mQuestion =question;
	}
	public void setAnswer(String answer){
		mAnswer = answer;
	}
	public void setTitle(String title){
		mTitle = title;
	}
	public void setWeek(int week){
		mWeek = week;
	}
	public String getQuestion(){
		return mQuestion;
	}
	public String getAnswer(){
		return mAnswer;
	}
	public String getTitle(){
		return mTitle;
	}
	public int getWeek(){
		return mWeek;
	}
	public QnAData(){
	}
	public QnAData(String question, String answer, String title, int week){
		setQustion(question);
		setAnswer(answer);
		setTitle(title);
		setWeek(week);
	}
}
