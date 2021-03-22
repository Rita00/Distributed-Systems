package pt.uc.dei.student.elections;

import java.io.Serializable;

public class Candidacy implements Serializable {
    private final int id;
    private String name;
    private String type;

    public Candidacy(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String toString() {
        return "===========LISTA===========\n" +
                String.format("%s\t(%s)\n", this.name, this.type) +
                "=============================\n";
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
}
