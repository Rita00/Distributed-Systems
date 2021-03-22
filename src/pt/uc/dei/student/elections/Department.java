package pt.uc.dei.student.elections;

import java.io.Serializable;

public class Department implements Serializable {
    private final int id;
    private String name;

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
