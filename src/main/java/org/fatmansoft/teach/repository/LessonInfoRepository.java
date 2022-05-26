package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonInfoRepository extends JpaRepository<LessonInfo,Integer> {

    @Query(value = " select * from LessonInfo where course.id=?1",nativeQuery = true)
    List<LessonInfo> findLessonInfoByCourse(Integer Id);

    @Query(value = "  from LessonInfo where course.id=?1")
    List<LessonInfo> findByCourseId(Integer Id);
    @Query(value = "select * from  lesson_info where text_book =?1",nativeQuery = true)
    LessonInfo findByTextBook(String x);

    @Query(value = "select max(id) from lesson_info ",nativeQuery = true)
    Integer getMaxId();

}
