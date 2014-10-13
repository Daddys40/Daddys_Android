package com.daddys40.data;

public class FeedItem {
	private String week;
	private String content;
	
	private String title;
	private String imgSrc;
	
	public static final int FOLD = 0;
	public static final int OPEN = 1;
	private int mode = FOLD;
	
	private int Img_width = 200;
	private int Img_height = 200;
	
	private int id = -1;
	private boolean isReaded = false;
	private boolean changed = true;
	
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
	public void setTitle(String title){
		this.title =title;
	}
	public String getTitle(){
		return title;
	}
	public void setImgSrc(String url){
		this.imgSrc = url;
	}
	public String getImgSrc(){
		return imgSrc;
	}
	public void setWidth(int width){
		this.Img_width = width;
	}
	public void setHeight(int height){
		this.Img_height = height;
	}
	public int getWidth(){
		return Img_width;
	}
	public int getHeight(){
		return Img_height;
	}
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return id;
	}
	public void setReaded(boolean isReaded){
		this.isReaded = isReaded;
	}
	public boolean isReaded(){
		return isReaded;
	}
	public FeedItem() {
	}
	public FeedItem(String week, String content,String title){
		setWeek(week);
		setContent(content);
		setTitle(title);
	}
	public void setChanged(boolean changed){
		this.changed = changed;
	}
	public boolean isChanged(){
		return changed;
	}
}
