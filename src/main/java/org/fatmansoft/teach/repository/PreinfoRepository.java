package org.fatmansoft.teach.repository;
import org.fatmansoft.teach.models.Info;
import org.fatmansoft.teach.models.Preinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreinfoRepository extends JpaRepository<Preinfo,Integer> {

    @Query(value = "select max(id) from Preinfo  ")
    Integer getMaxId();

    @Query(value = "from Preinfo where ?1='' or student.studentNum like %?1% or student.studentName like %?1% ")
    List<Preinfo> findPreinfoByNumName(String numName);

    @Query(value = "from Preinfo where student.studentNum like %?1% ")
    Preinfo findPreinfoByNumName1(String numName);

    @Query(value = "from Preinfo where student.id  = ?1 ")
    Preinfo findPreinfoBysId(Integer id);

}
