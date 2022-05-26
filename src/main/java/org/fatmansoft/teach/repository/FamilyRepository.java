package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Family;
import org.fatmansoft.teach.models.Info;
import org.fatmansoft.teach.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FamilyRepository extends JpaRepository<Family,Integer> {



    @Query(value = " select * from family where student_id=?1",nativeQuery = true)
     Family findFamilyByStudent(Integer Id);

    @Query(value = "select max(id) from Family ")
    Integer getMaxId();

    List<Family> findByStudentId(Integer studentId);

    @Query(value = "from Family where ?1='' or student.studentNum like %?1% or student.studentName like %?1% ")
    List<Family> findFamilyByNumName(String numName);


}
