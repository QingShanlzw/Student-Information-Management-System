package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Integer> {
    @Query(value = "select max(id) from Score  ")
    Integer getMaxId();
    List<Score> findByStudentId(Integer studentId);
    List<Score> findByCourseCourseNum(String courseNum);
    @Query(value="select s.course from Score s where s.student.id=?1")
    List<Course> findCourseList(Integer studentId);
    @Query(value = " select * from score where student_id=?1",nativeQuery = true)
    List<Score> findScoreByStudent(Integer Id);//注意参数的类型*！
    @Query(value = "select * from score where student_id=?1 and course_id =?2",nativeQuery = true)
    Score findIfHave(Integer a, Integer b);



}
