package pt.uc.dei.student.elections;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Election implements Serializable {

	private int id;
	private String title;
	private String type;
	private String description;
	private LocalDateTime begin;
	private LocalDateTime end;


	public Election(int id, String title,String type ,String description, String begin, String end) {
		this.id=id;
		this.title=title;
		this.type=type;
		this.description=description;
		this.begin = LocalDateTime.parse(begin, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		this.end = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	public String toString(){
		return 	"===========ELEICAO===========\n"+
				String.format("%s\t(%s)\n", this.title, this.type)+
				String.format("%s\n", this.description)+
				String.format("Inicio - %s às %s\n", this.begin.toLocalDate().toString(),this.begin.toLocalTime().toString())+
				String.format("Fim - %s às %s\n", this.end.toLocalDate().toString(),this.end.toLocalTime().toString())+
				"=============================\n";
	}

	public int getId(){return this.id;}
	public String getTitle(){return this.title;}
	public String getType(){return this.type;}
	public String getDescription(){return this.description;}
    public LocalDateTime getBegin(){return this.begin;}
	public LocalDateTime getEnd(){return this.end;}
	public void setTitle(String title){this.title=title;}
	public void setType(String type){this.type=type;}
	public void setDescription(String description){this.description=description;}
	public void setBegin(String begin){this.begin=LocalDateTime.parse(begin, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));}
	public void setEnd(String end){this.end=LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));}

}