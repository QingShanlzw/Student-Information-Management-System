package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "activityInfo",
        uniqueConstraints = {
        })
public class ActivityInfo {
        @Id
        private Integer id;

        @Size(max = 20)
        private String job;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getJob() {
                return job;
        }

        public void setJob(String job) {
                this.job = job;
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

        public Activity getActivity() {
                return activity;
        }

        public void setActivity(Activity activity) {
                this.activity = activity;
        }

        @ManyToOne
        @JoinColumn(name = "activityId")
        private Activity activity;

        public Integer getHour() {
                return hour;
        }

        public void setHour(Integer hour) {
                this.hour = hour;
        }

 //Integer类型不需要加Size
        private Integer hour;

}
