package pt.uc.dei.student.elections;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Person implements Serializable {

    private String name;
    private final int cc_number;
    private LocalDate cc_validity;
    private String password;
    private String address;
    private int phone;
    private String job;
    private int department_id;


    public Person(String name,int cc_number, String cc_validity,String password, String address,int phone,String job, int department_id) {
        this.name=name;
        this.cc_number=cc_number;
        this.cc_validity=LocalDate.parse(cc_validity, DateTimeFormatter.ofPattern("yyyy-M-d"));
        this.password=password;
        this.address=address;
        this.phone=phone;
        this.department_id=department_id;
        this.job=job;

    }
    public String getName() { return name; }
    public int getCc_number() { return cc_number; }
    public LocalDate getCc_validity() { return cc_validity; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public int getPhone() { return phone; }
    public String getJob() { return job; }
    public int getDepartment_id() { return department_id; }

}
