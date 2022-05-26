package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(	name = "activity",
        uniqueConstraints = {
        })
public class Activity {
        @Id
        private Integer id;

        @ManyToOne
        @JoinColumn(name="activityTypeId")
        private ActivityType activityType;

        @Size(max = 20)
        private String activityName;

        @Size(max = 20)
        private String activityAdd;//活动地址

        private Date activityDate;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public ActivityType getActivityType() {
                return activityType;
        }

        public void setActivityType(ActivityType activityType) {
                this.activityType = activityType;
        }

        public String getActivityName() {
                return activityName;
        }

        public void setActivityName(String activityName) {
                this.activityName = activityName;
        }

        public String getActivityAdd() {
                return activityAdd;
        }

        public void setActivityAdd(String activityAdd) {
                this.activityAdd = activityAdd;
        }

        public Date getActivityDate() {
                return activityDate;
        }

        public void setActivityDate(Date activityDate) {
                this.activityDate = activityDate;
        }

        public String getOrganizer() {
                return organizer;
        }

        public void setOrganizer(String organizer) {
                this.organizer = organizer;
        }

        @Size(max = 20)
        private String organizer; //主办单位





}
