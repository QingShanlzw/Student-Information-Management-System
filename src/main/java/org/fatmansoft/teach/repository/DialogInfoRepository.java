package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.DialogInfo;
import org.fatmansoft.teach.models.Info;
import org.fatmansoft.teach.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface DialogInfoRepository extends JpaRepository<DialogInfo, Integer> {
    @Query(value="select  * from dialog_info ", nativeQuery = true)
    List<DialogInfo>findDialogInfo();
    @Query(value = "select max(id) from dialog_info",nativeQuery = true)
    Integer getMaxId();
    @Query(value = "from DialogInfo where ?1='' or student.studentNum like %?1% or student.studentName like %?1% ")
    List<DialogInfo> findDialogInfoByNumName(String numName);
    @Query(value = " select * from dialog_info where student_id=?1",nativeQuery = true)
    List<DialogInfo> findDialogInfoByStudent(Integer Id);//注意参数的类型*！
}
