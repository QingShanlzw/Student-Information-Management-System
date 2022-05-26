package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "DialogInfo",
        uniqueConstraints = {}
)
public class DialogInfo {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name ="studentId")
    private Student student;
    @Size(max =30)
    private String content;
    @Size(max = 100)
    private String detail;

    @Override
    public String toString() {
        return "DialogInfo{" +
                "id=" + id +
                ", student=" + student +
                ", content='" + content + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
