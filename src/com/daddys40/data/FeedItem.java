package com.daddys40.data;

public class FeedItem {
	private String week;
	private String content;
	
	private final int FOLD = 0;
	private final int OPEN = 1;
	private int mode = FOLD;
	
	public void setWeek(String week){
		this.week = week;
	}
	public void setContent(String content){
		this.content = content;
	}
	public String getWeek(){
		return week;
	}
	public String getContent(){
		return content;
	}
	public void setModeChange(){
		if(mode == FOLD)
			mode = OPEN;
		else
			mode = FOLD;
	}
	public int getMode(){
		return mode;
	}
	public FeedItem() {
	}
	public FeedItem(String week, String content){
		setWeek(week);
		setContent(content);
	}
}
