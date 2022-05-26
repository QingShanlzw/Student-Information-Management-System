package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.StudentInfo;
import org.fatmansoft.teach.repository.StudentInfoRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IntroduceService {
    @Autowired
    private StudentInfoRepository studentInfoRepository;

    public String getHtmlCount(String name){
        String content = "";
        content= "<!DOCTYPE html>";
        content += "<html>";
        content += "<head>";
        content += "<style>";
        content += "html { font-family: \"SourceHanSansSC\", \"Open Sans\";}";
        content += "</style>";
        content += "<meta charset='UTF-8' />";
        content += "<title>Insert title here</title>";
        content += "</head>";
        content += "<body>";

        content += "<table style='width: 100%;'>";
        content += "   <thead >";
        content += "     <tr style='text-align: center;font-size: 32px;font-weight:bold;'>";
        content += "        "+name+ " HTML </tr>";
        content += "   </thead>";
        content += "   </table>";


        content += "</body>";
        content += "</html>";
        return content;
    }
    //个人简历信息数据准备方法  请同学修改这个方法，请根据自己的数据的希望展示的内容拼接成字符串，放在Map对象里， attachList 可以放多段内容，具体内容由个人决定
    public Map getIntroduceDataMap(Integer studentId) {
        StudentInfo si = null;
        Optional<StudentInfo> op = studentInfoRepository.findByStudentId(studentId);
        if (op.isPresent()) {
            si = op.get();
        }
        Map data = new HashMap();
        String html = "";
//        html = getHtmlCount(name);
        if(html.length()> 0) {
            data.put("html", html);
            return data;
        }
        if (si != null) {

            data.put("myName","山东大学月球校区学生教务");   // 学生信息
            data.put("overview","学生姓名："+si.getStudent().getStudentName()+"学生学号："+si.getStudent().getStudentNum()+"学生院系："+si.getStudent().getDept());  //学生基本信息综述
            List attachList = new ArrayList();
            Map m;
            m = new HashMap();
            m.put("title", "学生生源地");
            m.put("content", si.getAddress());
            attachList.add(m);
            m = new HashMap();
            m.put("title", "毕业院校");
            m.put("content", si.getGraduateSchool());
            attachList.add(m);
            m = new HashMap();
            m.put("title", "学生竞赛经历");
            m.put("content", si.getCompetition());
            attachList.add(m);
            m = new HashMap();
            m.put("title", "社会实践");
            m.put("content", si.getSocialPractice());  // 社会实践综述
            attachList.add(m);
            m = new HashMap();
            m.put("title", "学习荣誉");
            m.put("content", si.getCredit());
            attachList.add(m);
            m = new HashMap();
            data.put("attachList", attachList);
        }
        return data;

    }
}
