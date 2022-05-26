package org.fatmansoft.teach.models;


import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "activityType",
        uniqueConstraints = {
        })
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 20)
    private String activityType;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }


}
