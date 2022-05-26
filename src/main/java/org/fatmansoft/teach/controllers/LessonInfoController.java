package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Family;
import org.fatmansoft.teach.models.LessonInfo;
import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.LessonInfoRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

// origins： 允许可访问的域列表
// maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class LessonInfoController {

    @Autowired
    private LessonInfoRepository lessonInfoRepository;
    @Autowired
    private CourseRepository courseRepository;




    @PostMapping("/LessonInfoEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse LessonInfoEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        System.out.println("点击成绩得到的id是"+id);
        System.out.println("ID is " + id);
        LessonInfo li = null;
        List<LessonInfo> op = null;
        if (id != null) {
            op = lessonInfoRepository.findByCourseId(id);
            if (op.isEmpty()) {
                System.out.println("op是空的");
            } else {
                System.out.println("op不为空");
            }
        } else {
            System.out.println("id是空的");
        }
        System.out.println(op.toString()+" op");
        Map form =new HashMap();
        List foo = new ArrayList();
        form.put("className",courseRepository.findById3(id).getCourseName());
        for(int i=0;i<op.size();i++) {

            li = op.get(i);

            form.put("attendance", li.getAttendance());
            form.put("examination", li.getExamination());
            form.put("referenceBook",li.getReferenceBook());
            form.put("textBook",li.getTextBook());
            foo.add(form);
        }
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }
    public synchronized Integer getNewLessonInfoId(){
        Integer
                id = lessonInfoRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };
    @PostMapping("/LessonInfoEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse LessonInfoEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String className = CommonMethod.getString(form,"className");
        String attendance = CommonMethod.getString(form,"attendance");
        String examination = CommonMethod.getString(form,"examination");
        String referenceBook =CommonMethod.getString(form,"referenceBook");
        String textBook = CommonMethod.getString(form,"textBook");
        LessonInfo li =null;
        li =lessonInfoRepository.findByTextBook(textBook);
        if(li== null) {
            li = new LessonInfo();   //不存在 创建实体对象
            Integer id = getNewLessonInfoId(); //获取鑫的主键，这个是线程同步问题;
            li.setId(id);  //设置新的id
        }
        li.setAttendance(attendance);
        li.setCourse(courseRepository.findByCourseName(className));
        li.setReferenceBook(referenceBook);
        li.setExaminetion(examination);
        li.setTextBook(textBook);
        lessonInfoRepository.save(li);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(li.getId());  // 将记录的id返回前端
    }








}
