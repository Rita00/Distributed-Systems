package pt.uc.dei.student.elections;

import java.sql.ResultSet;
import java.util.Date;

public class Election {
	
	private String title;
	private String description;
    private String electoralComission;
    private Date begin;
    private Date end;

	public Election(ResultSet query) {
		System.out.println(query.toString());
	}
}