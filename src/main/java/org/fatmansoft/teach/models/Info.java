package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Optional;

@Entity
@Table(	name = "info",
        uniqueConstraints = {
})

public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Size(max =20)
    private String phone;


    public void setId(Optional<Info> byId) {
    }
}
//在学生管理中加以行  成绩->超链  ：  成绩->显示每个人的所有成绩。
//把三个数据库关联起来。
//在学生信息界面点击一个超链跳到个人简历，但是个人简历好像不在yml里   加到信息管理中（info）
//把课程管理改一下
