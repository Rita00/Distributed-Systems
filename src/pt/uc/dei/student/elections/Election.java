package pt.uc.dei.student.elections;

import java.io.Serializable;

public class Election implements Serializable {

	private long begin;
	private long end;
	private String title;
	private String description;

	public Election(long begin, long end, String title, String description) {
		this.begin=begin;
		this.end=end;
		this.title=title;
		this.description=description;
	}

	public long getBegin(){return this.begin;}
	public long getEnd(){return this.end;}
	public String getTitle(){return this.title;}
	public String getDescription(){return this.description;}
}