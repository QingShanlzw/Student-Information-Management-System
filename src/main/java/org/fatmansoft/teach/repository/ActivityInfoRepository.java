package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Activity;
import org.fatmansoft.teach.models.ActivityInfo;
import org.fatmansoft.teach.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityInfoRepository extends JpaRepository<ActivityInfo, Integer> {

    @Query(value = "from ActivityInfo where ?1='' or student.studentNum like %?1% or student.studentName like %?1% ")
    List<ActivityInfo> findActivityInfoListByNumName(String numName);

    @Query(value = "select max(id) from ActivityInfo  ")
    Integer getMaxId();

    @Query(value = "select * from activityInfo where activity_id=?1 and student_id =?2",nativeQuery = true)
    Score findIfHave(Integer a, Integer b);
}
