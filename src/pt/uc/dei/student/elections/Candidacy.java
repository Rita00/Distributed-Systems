package pt.uc.dei.student.elections;

import java.util.List;

import pt.uc.dei.student.people.Person;

public class Candidacy {

    private Election election;
    private List<Person> candidates;

    Candidacy(Election election, List<Person> candidates){
        this.election=election;
        this.candidates=candidates;
    }
}