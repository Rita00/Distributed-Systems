package pt.uc.dei.student.elections;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Election implements Serializable {

    private final int id;
    private String title;
    private String type;
    private String description;
    private LocalDateTime begin;
    private LocalDateTime end;
    private ArrayList<Department> departments;

    public Election(int id, ArrayList<Department> departments) {
        this.id = id;
        this.departments=departments;
    }

    public Election(int id, String title, String type, String description, String begin, String end) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.begin = LocalDateTime.parse(begin.replace('T',' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        this.end = LocalDateTime.parse(end.replace('T',' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
    }

    public Election(int id, String title, String type, String description, String begin, String end, ArrayList<Department> departments) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.begin = LocalDateTime.parse(begin.replace('T',' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        this.end = LocalDateTime.parse(end.replace('T',' '), DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
        this.departments=departments;
    }

	private LocalDateTime parseTime(String date, String time) {
		LocalDateTime dateTime;
		try {
			dateTime = LocalDateTime.parse(date+" "+time,DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
		} catch (DateTimeParseException e) {
			return null;
		}
		return dateTime;
	}

    public String toString() {
        return "===========ELEICAO===========\n" +
                String.format("%s\t(%s)\n", this.title, this.type) +
                String.format("%s\n", this.description) +
                String.format("Inicio - %s às %s\n", this.begin.toLocalDate().toString(), this.begin.toLocalTime().toString()) +
                String.format("Fim - %s às %s\n", this.end.toLocalDate().toString(), this.end.toLocalTime().toString()) +
                "-----------------------------\n";
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getBegin() {
        return this.begin;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    public ArrayList<Department> getDepartments() {
        return this.departments;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        switch (type) {
            case 1:
                this.type = "Estudante";
                break;
            case 2:
                this.type = "Docente";
                break;
            case 3:
                this.type = "Funcionário";
                break;
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean setBegin(String date, String time) {
		LocalDateTime dateTime = parseTime(date, time);
		if (dateTime!=null){
			this.begin = dateTime;
			return true;
		}
		return false;
	}

	public boolean setEnd(String date, String time) {
		LocalDateTime dateTime = parseTime(date, time);
		if (dateTime!=null){
			this.end = dateTime;
			return true;
		}
		return false;
	}
}