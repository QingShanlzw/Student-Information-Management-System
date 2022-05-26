package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    Optional<Student> findByStudentNum(String studentNum);
    List<Student> findByStudentName(String studentName);

    @Query(value = "select max(id) from Student  ")
    Integer getMaxId();

    @Query(value = "from Student where ?1='' or studentNum like %?1% or studentName like %?1% ")
    List<Student> findStudentListByNumName(String numName);

    @Query(value = "select * from student  where ?1='' or student_num like %?1% or student_name like %?1% ", nativeQuery = true)
    List<Student> findStudentListByNumNameNative(String numName);
    @Query(value = "select  * from student where ?1=student_name",nativeQuery = true)
    Student findByStudentName1(String studentName);
    @Query(value = "select  * from student where ?1=student_num",nativeQuery = true)
    Student findByStudentNum1(String x);

    @Query(value = "select  * from student where ?1=id",nativeQuery = true)
    Student findById1(Integer id);



}
