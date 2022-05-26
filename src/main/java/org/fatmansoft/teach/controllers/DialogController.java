package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.DialogInfo;
import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.DialogInfoRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.util.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class DialogController {
    @Autowired
    private DialogInfoRepository dialogInfoRepository;
    @Autowired
    private StudentRepository studentRepository;
    public List getDialogInfoMapList(String studentNum) {
        List dataList = new ArrayList();
        List<DialogInfo> sList = dialogInfoRepository.findDialogInfoByNumName(studentNum);  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        DialogInfo s;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentName",s.getStudent().getStudentName());
            m.put("content",s.getContent());
            m.put("detail",s.getDetail());
            dataList.add(m);
        }
        return dataList;
    }
    @PostMapping("/dialogInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse dialogInit(@Valid @RequestBody DataRequest dataRequest){
        List dataList = getDialogInfoMapList("");
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/dialogEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse dialogEditInit(@Valid @RequestBody DataRequest dataRequest){
        Integer id =dataRequest.getInteger("id");
        DialogInfo d =null;
        Student s;
        Optional<DialogInfo> op;
        if(id!=null){
            op = dialogInfoRepository.findById(id);
            if(op.isPresent()){
                d=op.get();
            }
        }
        Map m;
        int i;
        List studentIdList =new ArrayList();
        List<Student> sList =studentRepository.findAll();
        for(i = 0; i <sList.size();i++) {
            s =sList.get(i);
            m = new HashMap();
            m.put("label",s.getStudentName());
            m.put("value",s.getId());
            studentIdList.add(m);
        }
        Map form =new HashMap();
        form.put("studentId","");
        if(d!=null){
            form.put("id",d.getId());
            form.put("content",d.getContent());
            form.put("detail",d.getDetail());
            //form.put("studentName",d.getStudent().getStudentName());
            form.put("studentId",d.getStudent().getId());
        }
        form.put("studentIdList",studentIdList);
        return CommonMethod.getReturnData(form);
    }
    public synchronized Integer getNewDialogId(){
        Integer
                id = dialogInfoRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };
    @PostMapping("/dialogEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse dialogEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Integer id = CommonMethod.getInteger(form,"id");
        String detail = CommonMethod.getString(form,"detail");  //Map 获取属性的值
        String studentName = CommonMethod.getString(form,"studentName");
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        String content = CommonMethod.getString(form,"content");
        //Integer age = CommonMethod.getInteger(form,"age");
        //Date birthday = CommonMethod.getDate(form,"birthday");
        //Student s= null;
        DialogInfo d =null;
        Optional<DialogInfo> op;
        if(id != null) {
            op= dialogInfoRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                d= op.get();
            }
        }
        if(d == null) {
            d = new DialogInfo();   //不存在 创建实体对象
            id = getNewDialogId(); //获取鑫的主键，这个是线程同步问题;
            d.setId(id);  //设置新的id
        }
        d.setDetail(detail);  //设置属性
        d.setContent(content);
        d.setStudent(studentRepository.findById(studentId).get());
        //studentRepository.save(s);  //新建和修改都调用save方法
        dialogInfoRepository.save(d);
        return CommonMethod.getReturnData(d.getId());  // 将记录的id返回前端
    }
    @PostMapping("/dialogQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse dialogQuery(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getDialogInfoMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/dialogDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse dialogDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        //Student s= null;
        DialogInfo d =null;
        Optional<DialogInfo> op;
        if(id != null) {
            op= dialogInfoRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                d = op.get();
            }
        }
        if(d != null) {
            dialogInfoRepository.delete(d);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
    @PostMapping("/dialogFormInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse dialogFormInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        //System.out.println("点击成绩得到的id是"+id);
        //System.out.println("ID is" + id);
        //Student s= null;
        DialogInfo d = null;
        List<DialogInfo> op = null;
        if (id != null) {
            //op = scoreRepository.findScoreByStudent(id);
            op = dialogInfoRepository.findDialogInfoByStudent(id);
            if (op.isEmpty()) {
                System.out.println("op是空的");
            } else {
                System.out.println("op不为空");
            }
        } else {
            System.out.println("id是空的");
        }
        System.out.println(op.toString()+" op");
        Map form =new HashMap();;
        List foo = new ArrayList();
        //double all1=0;//记录总分数
        //double all2=0;//记录总学分

        for(int i=0;i<op.size();i++){

            d = op.get(i);
            form.put("student"+i, d.getStudent().getStudentName());
            //System.out.println(s.getStudent());
            form.put("content"+i, d.getContent());
            form.put("detail"+i, d.getDetail());
            //System.out.println(s.getCourse().getCourseName());
            }

            //System.out.println(s.getCourse().getCredit()+"学分");
            //System.out.println(s.getScore()+"成绩");
            foo.add(form);


        // System.out.println(foo.toString());
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }




}
