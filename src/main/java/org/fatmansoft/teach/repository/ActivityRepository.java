package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Activity;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query(value = "from Activity where ?1='' or activityName like %?1% ")
    List<Activity> findActivityListByName(String name);

    @Query(value = "select max(id) from Activity  ")
    Integer getMaxId();

}
