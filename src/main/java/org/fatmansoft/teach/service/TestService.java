package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.repository.ScoreRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TestService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    public void createStudent(){
        Student s;
        Integer id = studentRepository.getMaxId();
        if(id == null)
            id = 1;
        else
            id = id+1;
        s = new Student();
        s.setId(id);
        s.setStudentNum("001");
        s.setStudentName("张三");
        s.setSex("1");
        s.setAge(12);
        s.setBirthday(new Date());
        studentRepository.save(s);
    }
    public void testDelete(){
        Student s = studentRepository.findById(1).get();
        System.out.println(DateTimeTool.parseDateTime(s.getBirthday(),"yyyy-MM-dd"));
        studentRepository.delete(s);
    }
    public void testQuery(){
        List list = scoreRepository.findByStudentId(1);
        list = scoreRepository.findByCourseCourseNum("1");
        list = scoreRepository.findCourseList(1);
        System.out.println(list);
    }
    public void testMain(){
//        createStudent();
//        testDelete();
//        testQuery();
    }

}
