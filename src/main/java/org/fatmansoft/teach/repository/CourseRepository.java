package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {
    @Query(value = "from Course")
    List<Course> findCourseById(String id1);
    @Query(value = "select max(id) from Course  ")
    Integer getMaxId();
    @Query(value = "select * from course where  course_name = ?1",nativeQuery = true)
    Course findByCourseName(String x);

    @Query(value = "select * from course where  id = ?1",nativeQuery = true)
    Course findById3(Integer x);

    @Query(value = "select * from course where  course_num = ?1",nativeQuery = true)
    Course findById4(String x);

    @Query(value = "select id from course where  course_num = ?1",nativeQuery = true)
    Integer findBynum(String x);
}
