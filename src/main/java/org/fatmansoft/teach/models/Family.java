package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "family",
        uniqueConstraints = {
        })

public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="studentId")
    private Student student;

    @Size(max = 20)
    private String father;
    @Size(max = 20)
    private String Fphone;//父亲号码
    @Size(max = 20)
    private String Fwork_address;//工作单位occupation
    @Size(max = 20)
    private String Foccupation;//职业

    @Size(max = 20)
    private String mother;
    @Size(max = 20)
    private String Mphone;
    @Size(max = 20)
    private String Mwork_address;//工作单位
    @Size(max = 20)
    private String Moccupation;//职业

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

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getFphone() {
        return Fphone;
    }

    public void setFphone(String fphone) {
        Fphone = fphone;
    }

    public String getFwork_address() {
        return Fwork_address;
    }

    public void setFwork_address(String fwork_address) {
        Fwork_address = fwork_address;
    }

    public String getFoccupation() {
        return Foccupation;
    }

    public void setFoccupation(String foccupation) {
        Foccupation = foccupation;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getMphone() {
        return Mphone;
    }

    public void setMphone(String mphone) {
        Mphone = mphone;
    }

    public String getMwork_address() {
        return Mwork_address;
    }

    public void setMwork_address(String mwork_address) {
        Mwork_address = mwork_address;
    }

    public String getMoccupation() {
        return Moccupation;
    }

    public void setMoccupation(String moccupation) {
        Moccupation = moccupation;
    }



}
