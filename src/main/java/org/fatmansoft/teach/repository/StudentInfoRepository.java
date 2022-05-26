package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.StudentInfo;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo,Integer> {

    @Query(value = "select max(id) from StudentInfo  ")
    Integer getMaxId();
    Optional<StudentInfo> findByStudentId(Integer studentId);
    List<StudentInfo> findByCourseCourseNum(String courseNum);

    @Query(value = "from StudentInfo where ?1='' or student.studentNum like %?1% or student.studentName like %?1% ")
    List<Student> findStudentListByNumName(String numName);

    @Query(value="select si.course from StudentInfo si where si.student.id=?1")
    List<Course> findCourseList(Integer studentId);
    @Query(value = "select * from student_info where student_id = ?1" ,nativeQuery = true)
    Optional<StudentInfo> findBySdId(Integer id);


}
