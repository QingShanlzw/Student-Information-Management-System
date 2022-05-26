package org.fatmansoft.teach.controllers;


import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.ActivityInfoRepository;
import org.fatmansoft.teach.repository.ActivityRepository;
import org.fatmansoft.teach.repository.ActivityTypeRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private ActivityInfoRepository activityInfoRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List getActivityMapList(String name) {
        List dataList = new ArrayList();
        List<Activity> sList = activityRepository.findActivityListByName(name);  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        Activity s;
        ActivityType sc;
        Map m;
        String activityInfoParas;

        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            sc = s.getActivityType();
            m = new HashMap();
            m.put("id", s.getId());
            if(s.getActivityType()!=null){
            m.put("activityType",s.getActivityType().getActivityType());}
            else{
                m.put("activityType",null);
            }
            m.put("activityName",s.getActivityName());
            m.put("activityAdd",s.getActivityAdd());
            m.put("organizer",s.getOrganizer());

            activityInfoParas = "model=activityInfo&activityId=" + s.getId();
            m.put("activityInfo","活动参与情况");
            m.put("activityInfoParas",activityInfoParas);

            m.put("activityDate", DateTimeTool.parseDateTime(s.getActivityDate(),"yyyy-MM-dd"));  //时间格式转换字符串
            dataList.add(m);
        }
        return dataList;
    }
    @PostMapping("/activityInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getActivityMapList("");
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    //点击查询按钮请求
    @PostMapping("/activityQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityQuery(@Valid @RequestBody DataRequest dataRequest) {
        String name= dataRequest.getString("name");
        List dataList = getActivityMapList(name);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/activityDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse activityDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        Activity s= null;
        Optional<Activity> op;
        if(id != null) {
            op= activityRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            activityRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    @PostMapping("/activityEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Activity sc= null;
        ActivityType s;
        Optional<Activity> op;
        if(id != null) {
            op= activityRepository.findById(id);
            if(op.isPresent()) {
                sc = op.get();
            }
        }
        Map m;
        int i;
        List activityTypeIdList = new ArrayList();
        List<ActivityType> sList = activityTypeRepository.findAll();
        for(i = 0; i <sList.size();i++) {
            s =sList.get(i);
            m = new HashMap();
            m.put("label",s.getActivityType());
            m.put("value",s.getId());
            activityTypeIdList.add(m);
        }
        Map form = new HashMap();
        form.put("activityTypeId","");
        if(sc != null) {
            form.put("id",sc.getId());
            form.put("activityTypeId",sc.getActivityType().getId());
            form.put("activityName",sc.getActivityName());
            form.put("activityAdd",sc.getActivityAdd());
            form.put("organizer",sc.getOrganizer());
            form.put("activityDate",DateTimeTool.parseDateTime(sc.getActivityDate(),"yyyy-MM-dd"));
        }
        form.put("activityTypeIdList",activityTypeIdList);
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }


    public synchronized Integer getNewActivityId(){
        Integer  id = activityRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };
    @PostMapping("/activityEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse activityEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Integer id = CommonMethod.getInteger(form,"id");
        Integer activityTypeId = CommonMethod.getInteger(form,"activityTypeId");
        String activityName = CommonMethod.getString(form,"activityName");
        String activityAdd = CommonMethod.getString(form,"activityAdd");
        String organizer = CommonMethod.getString(form,"organizer");
        Date activityDate = CommonMethod.getDate(form,"activityDate");
        //Integer activityTypeId = activityTypeRepository.AcId(CommonMethod.getString(form,"activityTypeId"));
        Activity sc= null;
        ActivityType s= null;
        Optional<Activity> op;
        if(id != null) {
            op= activityRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                sc = op.get();
            }
        }
        if(sc == null) {
            sc = new Activity();   //不存在 创建实体对象
            id = getNewActivityId(); //获取鑫的主键，这个是线程同步问题;
            sc.setId(id);  //设置新的id
        }
        sc.setActivityType(activityTypeRepository.findById(activityTypeId).get());  //设置属性
        sc.setActivityName(activityName);
        sc.setActivityAdd(activityAdd);
        sc.setActivityDate(activityDate);
        sc.setOrganizer(organizer);
        activityRepository.save(sc);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(sc.getId());  // 将记录的id返回前端
    }

    public List getActivityInfoMapList(String numName) {
        List dataList = new ArrayList();
        List<ActivityInfo> sList = activityInfoRepository.findActivityInfoListByNumName(numName);  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        ActivityInfo s;
        Activity sc;
        Student c;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            c = s.getStudent();
            sc = s.getActivity();
            m = new HashMap();
            m.put("id", s.getId());
            m.put("activityId",sc.getId());
            m.put("activityName",sc.getActivityName());
            m.put("studentName",c.getStudentName());
            m.put("studentNum",c.getStudentNum());
            m.put("job",s.getJob());
            m.put("hour",s.getHour());
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/activityInfoInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityInfoInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getActivityInfoMapList("");
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    //点击查询按钮请求
    @PostMapping("/activityInfoQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityInfoQuery(@Valid @RequestBody DataRequest dataRequest) {
        String name = dataRequest.getString("numName");
        List dataList = getActivityInfoMapList(name);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/activityInfoDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse activityInfoDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        ActivityInfo s= null;
        Optional<ActivityInfo> op;
        if(id != null) {
            op= activityInfoRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            activityInfoRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    @PostMapping("/activityInfoEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityInfoEditInit(@Valid @RequestBody DataRequest dataRequest) {

        Integer id = dataRequest.getInteger("id");
        System.out.println("id是" + id);
        ActivityInfo s = null;
        Student c;
        Activity a;
        Optional<ActivityInfo> op;
        if (id != null) {
            op = activityInfoRepository.findById(id);
            if (op.isPresent()) {
                s = op.get();
            }
        }
        Map m;
        int i;
        List studentIdList = new ArrayList();
        List<Student> sList = studentRepository.findAll();
        for(i = 0; i <sList.size();i++) {
            c = sList.get(i);
            m = new HashMap();
            m.put("label", c.getStudentName());
            m.put("value", c.getId());
            studentIdList.add(m);
        }
        Map m1;
        int i1;
        List activityIdList = new ArrayList();
        List<Activity> aList = activityRepository.findAll();
        for(i1 = 0; i1<aList.size();i1++) {
            a = aList.get(i1);
            m = new HashMap();
            m.put("label", a.getActivityName());
            m.put("value", a.getId());
            activityIdList.add(m);
        }

        Map form = new HashMap();
        form.put("studentId","");
        form.put("activityId","");
        if (s != null) {
            form.put("id", s.getId());
            form.put("studentId",s.getStudent().getId());
            form.put("job",s.getJob());
            form.put("hour",s.getHour());
            form.put("activityId",s.getActivity().getId());
        }
        form.put("studentIdList",studentIdList);
        form.put("activityIdList",activityIdList);
        return CommonMethod.getReturnData(form);
    };
    public synchronized Integer getNewActivityInfoId(){
        Integer  id = activityInfoRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };

    @PostMapping("/activityInfoEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse activityInfoEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象

        Integer id = CommonMethod.getInteger(form,"id");
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        Integer activityId = CommonMethod.getInteger(form,"activityId");
        String job = CommonMethod.getString(form,"job");
        Integer hour = CommonMethod.getInteger(form,"hour");
        ActivityInfo s= null;
        Student c;
        Optional<ActivityInfo> op;
        if(id != null) {
            op= activityInfoRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new ActivityInfo();   //不存在 创建实体对象
            id = getNewActivityInfoId(); //获取鑫的主键，这个是线程同步问题;
            s.setId(id);  //设置新的id
        }
        Activity activity=null;
       // activity.setActivityType(null);

        s.setStudent(studentRepository.findById(studentId).get());
        s.setActivity(activityRepository.findById(activityId).get());
        s.setJob(job);
        s.setHour(hour);
        //s.setActivity(activity);
        //s.setStudent(null);
        activityInfoRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(s.getId());  // 将记录的id返回前端
    }


}
