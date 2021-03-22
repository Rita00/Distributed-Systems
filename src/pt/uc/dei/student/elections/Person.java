package pt.uc.dei.student.elections;

import java.io.Serializable;

public class Person implements Serializable {

    private String address;
    private final int cc_number;
    private int cc_validity;
    private int department_id;
    private String password;
    private String job;
    private int phone;

    public Person(String address, int cc_number, int cc_validity, int department_id,  String job, String password,int phone) {
        this.address=address;
        this.cc_number=cc_number;
        this.cc_validity=cc_validity;
        this.department_id=department_id;
        this.job=job;
        this.password=password;
        this.phone=phone;
    }

    public String getAddress() { return address; }
    public int getCc_number() { return cc_number; }
    public int getCc_validity() { return cc_validity; }
    public int getDepartment_id() { return department_id; }
    public String getPassword() { return password; }
    public String getJob() { return job; }
    public int getPhone() { return phone; }
}
