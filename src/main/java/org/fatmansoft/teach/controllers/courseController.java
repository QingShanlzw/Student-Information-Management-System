package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.ScoreRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.service.IntroduceService;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sun.rmi.transport.proxy.RMIHttpToPortSocketFactory;

import javax.validation.Valid;
import java.util.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class courseController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    //添加一个弹窗，读取score库里面的分数，等等。
    //参照studentEditInit方法，
    //无法用List的形式回传，导致没法显示所有的课程。
    //score的学生人是和student联系着的，course是手动连的course的类，数据库都连起来了。
    //yml里的form弹窗的名字是xxxEdit，自动寻找xxxEditInit方法，repository里的数据库查询中函数命名。
    @PostMapping("/courseEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("studentId");
        System.out.println("点击成绩得到的id是"+id);
        System.out.println("ID is" + id);
        //Student s= null;
        Score s = null;
        List<Score> op = null;
        if (id != null) {
            op = scoreRepository.findScoreByStudent(id);
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
        double all1=0;//记录总分数
        double all2=0;//记录总学分
        for(int i=0;i<op.size();i++){

            s = op.get(i);
            form.put("student"+i, s.getStudent().getStudentName());
            //System.out.println(s.getStudent());
            form.put("course"+i, s.getCourse().getCourseName());
            //System.out.println(s.getCourse().getCourseName());
            if(s.getMark()==null){
                form.put("score"+i, 0);
            }
            else{
                form.put("score"+i, s.getMark());}
            if(s.getCourse().getCredit()!=null&&s.getMark()!=null){//有些课没有学分//还有人选了课没成绩
                all1+=(s.getMark()*s.getCourse().getCredit());
                all2+=s.getCourse().getCredit();
            }

            //System.out.println(s.getCourse().getCredit()+"学分");
            //System.out.println(s.getScore()+"成绩");
            foo.add(form);
        }
        double gpa =((all1/all2)-50.00)/10;
        String level;//评级
        if(gpa>=4.5){level="A+";}
        else if(gpa>=4){level="A";;}
        else if(gpa>=3.5){level="B+";;}
        else if(gpa>=3.0){level="B";}
        else if(gpa>=2.5){level="C+";}
        else if(gpa>=2.0){level="C";}
        else  {level="差";}
        String temp = String.format("%.2f",gpa)+"("+level+")";//String类型，小数点后两位。
        form.put("gpa",temp);
        // System.out.println(foo.toString());
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }

    public List getcourseManagementMapList(String numName,Integer id) {
        Integer studentId = id;
        List<Course> clist = courseRepository.findCourseById("");
        Student s=null;
        if(studentId!=null){
        Optional<Student> op =studentRepository.findById(studentId);

        if(op!=null){

            s= op.get();
        }
        }

        System.out.println("点击后得到的id是："+studentId);
        List dataList = new ArrayList();
        if (clist == null || clist.size() == 0) {
           // return CommonMethod.getReturnData(dataList);
            return dataList;
        }
        Map m;
        Course c;
        m=new HashMap();
        //String studentId
        for (int i = 0; i < clist.size(); i++) {
            c = clist.get(i);
            m = new HashMap();
            m.put("id", c.getId());
            if(studentId!=null){
            m.put("studentName",s.getStudentName());
            if(scoreRepository.findIfHave(studentId,courseRepository.findBynum(c.getCourseNum()))!=null){
                m.put("isSelect","是");
            }
            else {
                m.put("isSelect","否");
            }}
            else
            {
                m.put("studentName","");
                m.put("isSelect","");
            }
            m.put("courseName", c.getCourseName());
            m.put("courseNum", c.getCourseNum());
            m.put("credit", c.getCredit());
            String LessonInfoParas;
            LessonInfoParas = "model=LessonInfoEdit&id=" + c.getId();
            m.put("LessonInfo","点击查看");
            m.put("LessonInfoParas",LessonInfoParas);
            m.put("preCourse", c.getPreCourse());
            String scoreAddParas ="url=doScoreAdd&studentId="+studentId+"&courseId="+c.getCourseNum()+"&mark=60";
            m.put("scoreAdd","添加成绩");
            m.put("scoreAddParas",scoreAddParas);
            dataList.add(m);
        }
        return  dataList;
       // return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    @PostMapping("/courseManagementInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseManagementInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getcourseManagementMapList("",dataRequest.getInteger("studentId"));
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    public synchronized Integer getNewScoreId(){
        Integer  id = scoreRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };
    @PostMapping("/doScoreAdd")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse doScoreAdd(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        String  courseId = dataRequest.getString("courseId");
        Integer mark = dataRequest.getInteger("mark");
        Score sc = null;
        Student s = null;
        Course c = null;
        Optional<Score> op;
        Integer id;
        sc = new Score();
        id = getNewScoreId();
        sc.setId(id);
        if (scoreRepository.findIfHave(studentId, courseRepository.findBynum(courseId)) != null) {
            System.out.println("已经选择了这个课程" + scoreRepository.findIfHave(studentId, courseRepository.findBynum(courseId)).toString());
            return CommonMethod.getReturnMessageOK();
        }
        sc.setStudent(studentRepository.findById(studentId).get());
        sc.setCourse(courseRepository.findById4(courseId));
        sc.setMark(mark);
        scoreRepository.save(sc);
        return CommonMethod.getReturnMessageOK();
    }
    //courseAlterInit初始化方法
    @PostMapping("/courseManagementEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseManagementEditInit(@Valid @RequestBody DataRequest dataRequest){
        Integer id= dataRequest.getInteger("id");
        Course c=null;
        Optional<Course> op;
        if(id!=null){
            op=courseRepository.findById(id);
            if(op.isPresent()){
                c= op.get();
            }
        }
        Map form=new HashMap();
        if(c!=null){
            form.put("id",c.getId());
            form.put("courseNum",c.getCourseNum());//用课程号
            form.put("courseName",c.getCourseName());
            form.put("preCourse",c.getPreCourse());
            form.put("Credit",c.getCredit());

        }
        return CommonMethod.getReturnData(form);
    }
    //删除方法
    @PostMapping("/courseManagementDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse courseManagementDelete(@Valid @RequestBody DataRequest dataRequest){
        Integer id = dataRequest.getInteger("id");  //获取id值
        Course c= null;
        Optional<Course> op;
        if(id != null) {
            op= courseRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                c = op.get();
            }
        }
        if(c != null) {
            courseRepository.delete(c);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
    //课程提交
    @PostMapping("/courseManagementEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse courseManagementEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Integer id = CommonMethod.getInteger(form,"id");
        String courseNum =CommonMethod.getString(form,"courseNum");
        String courseName = CommonMethod.getString(form,"courseName");
        String preCourse = CommonMethod.getString(form,"preCourse");
        Integer credit = CommonMethod.getInteger(form,"credit");

        Course c= null;
        Optional<Course> op;
        if(id != null) {
            op= courseRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                c = op.get();
            }
        }
        if(c == null) {
            c = new Course();   //不存在 创建实体对象
            id = getNewCourseId(); //获取鑫的主键，这个是线程同步问题;
            c.setId(id);  //设置新的id
        }
        c.setCourseName(courseName);  //设置属性
        c.setPreCourse(preCourse);
        c.setCredit(credit);
        c.setCourseNum(courseNum);
        courseRepository.save(c);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(c.getId());  // 将记录的id返回前端
    }
    public synchronized Integer getNewCourseId(){
        Integer  id = courseRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };
    //student页面点击查询按钮请求
    //Table界面初始是请求列表的数据，从请求对象里获得前端界面输入的字符串，作为参数传递给方法getStudentMapList，返回所有学生数据，
    @PostMapping("/courseManagementQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseManagementQuery(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getcourseManagementMapList(numName,null);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

}

//    @Autowired
//    private CourseRepository courseRepository;
//
//    @PostMapping("/courseManagementEditInit")
//    @PreAuthorize("hasRole('ADMIN')")
//    public DataResponse courseManagementEditInit(@Valid @RequestBody DataRequest dataRequest){
//        Map form = new HashMap();
//        return CommonMethod.getReturnData(form);
//    }
    //如何让编辑和删除页面显示出来？
    //课程库的编辑页面为什吗会跳转到学生信息编辑页面。
    //studentEdit初始化方法
    //studentEdit编辑页面进入时首先请求的一个方法， 如果是Edit,再前台会把对应要编辑的那个学生信息的id作为参数回传给后端，我们通过Integer id = dataRequest.getInteger("id")
    //获得对应学生的id， 根据id从数据库中查出数据，存在Map对象里，并返回前端，如果是添加， 则前端没有id传回，Map 对象数据为空（界面上的数据也为空白）
//
//    @PostMapping("/studentEditInit")
//    @PreAuthorize("hasRole('ADMIN')")
//    public DataResponse studentEditInit(@Valid @RequestBody DataRequest dataRequest) {
//        Integer id = dataRequest.getInteger("id");
//        Student s= null;
//        Optional<Student> op;
//        if(id != null) {
//            op= studentRepository.findById(id);
//            if(op.isPresent()) {
//                s = op.get();
//            }
//        }
//        List sexList = new ArrayList();
//        Map m;
////        m = new HashMap();
////        m.put("label","男");
////        m.put("value","1");
////        sexList.add(m);
////        m = new HashMap();
////        m.put("label","女");
////        m.put("value","2");
////        sexList.add(m);
//        Map form = new HashMap();
//        if(s != null) {
//            form.put("id",s.getId());
//            form.put("studentNum",s.getStudentNum());
//            form.put("studentName",s.getStudentName());
//            form.put("sex",s.getSex());  //这里不需要转换
//            form.put("age",s.getAge());
//            form.put("birthday", DateTimeTool.parseDateTime(s.getBirthday(),"yyyy-MM-dd")); //这里需要转换为字符串
//        }
//        //       form.put("sexList",sexList);
//        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
//    }
//}