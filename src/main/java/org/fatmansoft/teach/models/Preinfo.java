package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "preinfo",
        uniqueConstraints = {
        })
public class Preinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="studentId")
    private Student student;




    @Size(max = 20)
    private String priSchool;//小学
    @Size(max = 20)
    private String priTeacher;//小学班主任
    @Size(max = 20)
    private String priPlace;//小学地址

    @Size(max = 20)
    private String junSchool;//初中

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getPriSchool() {
        return priSchool;
    }

    public void setPriSchool(String priSchool) {
        this.priSchool = priSchool;
    }

    public String getPriTeacher() {
        return priTeacher;
    }

    public void setPriTeacher(String priTeacher) {
        this.priTeacher = priTeacher;
    }

    public String getPriPlace() {
        return priPlace;
    }

    public void setPriPlace(String priPlace) {
        this.priPlace = priPlace;
    }

    public String getJunSchool() {
        return junSchool;
    }

    public void setJunSchool(String junSchool) {
        this.junSchool = junSchool;
    }

    public String getJunTeacher() {
        return junTeacher;
    }

    public void setJunTeacher(String junTeacher) {
        this.junTeacher = junTeacher;
    }

    public String getJunPlace() {
        return junPlace;
    }

    public void setJunPlace(String junPlace) {
        this.junPlace = junPlace;
    }

    public String getHighSchool() {
        return HighSchool;
    }

    public void setHighSchool(String highSchool) {
        HighSchool = highSchool;
    }

    public String getHighTeacher() {
        return HighTeacher;
    }

    public void setHighTeacher(String highTeacher) {
        HighTeacher = highTeacher;
    }

    public String getHighPlace() {
        return HighPlace;
    }

    public void setHighPlace(String highPlace) {
        HighPlace = highPlace;
    }

    @Size(max = 20)
    private String junTeacher;//初中班主任
    @Size(max = 20)
    private String junPlace;//初中地址

    @Size(max = 20)
    private String HighSchool;//高中
    @Size(max = 20)
    private String HighTeacher;//高中班主任
    @Size(max = 20)
    private String HighPlace;//高中地址


}
