package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Info;
import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InfoRepository extends JpaRepository<Info,Integer> {

    @Query(value = " select * from Info where id=?1",nativeQuery = true)
    List<Info> findInfoById(Integer Id);//注意参数的类型*！

    @Query(value = "select studentId from Info where studentId=?1",nativeQuery = true)
    List<Info> findInfoById(String studentId);

    @Query(value = "from Info where ?1='' or student.studentNum like %?1% or student.studentName like %?1% ")
    List<Info> findInfoByNumName(String numName);

    @Query(value = "select max(id) from Info  ")
    Integer getMaxId();

    @Query(value = "select student from Info where studentId=?1",nativeQuery = true)
    List<Student> findStudentList(Integer studentId);

}
