package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(	name = "LessonInfo",
        uniqueConstraints = {
        })
public class LessonInfo {
    @Id
    private Integer id;

    @OneToOne
    @JoinColumn(name = "courseId")
    private Course course;

    private String textBook;
    private String referenceBook;
    private String attendance;
    private String examination;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTextBook() {
        return textBook;
    }

    public void setTextBook(String textBook) {
        this.textBook = textBook;
    }

    public String getReferenceBook() {
        return referenceBook;
    }

    public void setReferenceBook(String referenceBook) {
        this.referenceBook = referenceBook;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getExamination() {
        return examination;
    }

    public void setExaminetion(String examination) {
        this.examination = examination;
    }
}
