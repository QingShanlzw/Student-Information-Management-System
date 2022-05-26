package org.fatmansoft.teach.controllers;
import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class InfoController<form> {
    @Autowired
    private InfoRepository infoRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private PreinfoRepository preinfoRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentInfoRepository studentInfoRepository;


    public List getInfoMapList(String numName){
        List dataList = new ArrayList();
        List<Info> infoList =infoRepository.findInfoByNumName(numName);//数据库查询操作
        if(infoList == null || infoList.size()==0)
            return dataList;
        Info s;
        Student sc;
        Map m;
        for(int i =0;i<infoList.size();i++){
            s=infoList.get(i);
            sc = s.getStudent();
            m=new HashMap();
            String familyParas ,preinfoParas;
            m.put("id",s.getId());
            m.put("studentNum",sc.getStudentNum());
            m.put("studentName",sc.getStudentName());
            m.put("phone",s.getPhone());

            familyParas = "model=familyEdit&id="+ s.getStudent().getId();
            m.put("family","家庭信息");
            m.put("familyParas",familyParas);

            String dialogInfoParas = "model=dialogForm&id="+s.getStudent().getId();
            m.put("dialogInfo","生活日志");
            m.put("dialogInfoParas",dialogInfoParas);
            String introduceParas = "model=introduceEdit&id="+s.getStudent().getId();
            m.put("introduce","简历详情");
            m.put("introduceParas",introduceParas);

            preinfoParas= "model=preinfoEdit&id="+ s.getStudent().getId();
            m.put("preinfo","入学前信息");
            m.put("preinfoParas",preinfoParas);

            dataList.add(m);
        }
        return dataList;
    }
    
    //页面初始化
    @PostMapping("/infoInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse infoInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList=getInfoMapList("");
        return CommonMethod.getReturnData(dataList);
    }

    //点击查询按钮请求
    @PostMapping("/infoQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse infoQuery(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getInfoMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    @PostMapping("/infoDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse infoDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        Info s= null;
        Optional<Info> op;
        if(id != null) {
            op= infoRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            infoRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    public List getFamilyMapList(String numName) {
        List dataList = new ArrayList();
        List<Family> sList = familyRepository.findAll();  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        Family s;
        Student sc;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            sc = s.getStudent();
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",sc.getStudentNum());
            m.put("studentName",sc.getStudentName());
            m.put("father", s.getFather());
            m.put("fphone", s.getFphone());
            m.put("fwork_address", s.getFwork_address());
            m.put("foccupation", s.getFoccupation());
            m.put("mother", s.getMother());
            m.put("mphone", s.getMphone());
            m.put("mwork_address", s.getMwork_address());
            m.put("moccupation", s.getMoccupation());
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/familyInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse familyInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getFamilyMapList("");
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    @PostMapping("/familyEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse familyEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");

        Family s = null;

        s = familyRepository.findFamilyByStudent(id);
        Optional<Family> op;
//        if (id != null) {
//            op = familyRepository.findById(id);
//            if (op.isPresent()) {
//                s = op.get();
//            }
//        }
        Map m;
        Map form = new HashMap();
        form.put("studentNum",studentRepository.findById1(id).getStudentNum());
        if (s != null) {
            form.put("id", s.getId());

            form.put("studentId",s.getStudent().getId());
            form.put("studentNum",s.getStudent().getStudentNum());
            form.put("studentName",s.getStudent().getStudentName());
            form.put("father", s.getFather());
            form.put("fphone", s.getFphone());
            form.put("fwork_address", s.getFwork_address());
            form.put("foccupation", s.getFoccupation());
            form.put("mother", s.getMother());
            form.put("mphone", s.getMphone());
            form.put("mwork_address", s.getMwork_address());
            form.put("moccupation", s.getMoccupation());

        }
        return CommonMethod.getReturnData(form);
    };

    public synchronized Integer getNewFamilyId(){
        Integer
                id = familyRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };

    @PostMapping("/familyEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse familyEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String studentNum = CommonMethod.getString(form,"studentNum");

        //Integer id = preinfoRepository.findPreinfoByNumName1(studentNum).getId();
        Integer studentId = studentRepository.findByStudentNum1(CommonMethod.getString(form,"studentNum")).getId();
        String father = CommonMethod.getString(form,"father");
        String Fphone = CommonMethod.getString(form,"fphone");
        String Fwork_address = CommonMethod.getString(form,"fwork_address");
        String Foccupation=CommonMethod.getString(form,"foccupation");
        String mother = CommonMethod.getString(form,"mother");
        String Mphone = CommonMethod.getString(form,"mphone");
        String Mwork_address = CommonMethod.getString(form,"mwork_address");
        String Moccupation=CommonMethod.getString(form,"moccupation");
        Family s= null;
        Optional<Family> op;
         s= familyRepository.findFamilyByStudent(studentId);  //查询对应数据库中主键为id的值的实体对象


        if(s == null) {
            s = new Family();   //不存在 创建实体对象
             Integer id = getNewFamilyId(); //获取鑫的主键，这个是线程同步问题;
            s.setId(id);  //设置新的id
        }
        s.setStudent(studentRepository.findById(studentId).get());  //设置属性
        s.setFather(father);
        s.setFphone(Fphone);
        s.setFwork_address(Fwork_address);
        s.setFoccupation(Foccupation);
        s.setMother(mother);
        s.setMphone(Mphone);
        s.setMwork_address(Mwork_address);
        s.setMoccupation(Moccupation);
        familyRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(s.getId());  // 将记录的id返回前端
    }




    @PostMapping("/infoEditInit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse infoEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Info sc= null;
        Student s;
        Optional<Info> op;
        if(id != null) {
            op= infoRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                sc = op.get();
            }
        }
        Map m;
        int i;
        List studentIdList = new ArrayList();
        List<Student> sList = studentRepository.findAll();
        for(i = 0; i <sList.size();i++) {
            s =sList.get(i);
            m = new HashMap();
            m.put("label",s.getStudentName());
            m.put("value",s.getId());
            studentIdList.add(m);
        }
        Map form = new HashMap();
        form.put("studentId","");
        if(sc != null) {
            form.put("id",sc.getId());
            form.put("studentId",sc.getStudent().getId());
            form.put("phone",sc.getPhone());
        }
        form.put("studentIdList",studentIdList);
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }

    public synchronized Integer getNewInfoId(){
        Integer  id = infoRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };

    @PostMapping("/infoEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse infoEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Integer id = CommonMethod.getInteger(form,"id");
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        String phone = CommonMethod.getString(form,"phone");
        Info sc= null;
        Student s= null;
        Optional<Info> op;
        if(id != null) {
            op= infoRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                sc = op.get();
            }
        }
        if(sc == null) {
            sc = new Info();   //不存在 创建实体对象
            id = getNewInfoId(); //获取鑫的主键，这个是线程同步问题;
            sc.setId(id);  //设置新的id
        }
        sc.setStudent(studentRepository.findById(studentId).get());  //设置属性
        sc.setPhone(phone);
        infoRepository.save(sc);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(sc.getId());  // 将记录的id返回前端
    }
    public List getPreinfoMapList(String numName) {
        List dataList = new ArrayList();
        List<Preinfo> sList = preinfoRepository.findAll();  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        Preinfo s;
        Student sc;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            sc = s.getStudent();
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",sc.getStudentNum());
            m.put("studentName",sc.getStudentName());
            m.put("priSchool",s.getPriSchool());
            m.put("priTeacher",s.getPriTeacher());
            m.put("priPlace",s.getPriPlace());
            m.put("junSchool",s.getJunSchool());
            m.put("junPlace",s.getJunPlace());
            m.put("junTeacher",s.getJunTeacher());
            m.put("highSchool",s.getHighSchool());
            m.put("highPlace",s.getHighPlace());
            m.put("highTeacher",s.getHighTeacher());
            dataList.add(m);
        }
        return dataList;
    }
    @PostMapping("/preinfoInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse preinfoInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getPreinfoMapList("");
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }


    @PostMapping("/preinfoEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse preinfoEditInit(@Valid @RequestBody DataRequest dataRequest) {

        Integer id = dataRequest.getInteger("id");

        Preinfo s = null;
        s= preinfoRepository.findPreinfoBysId(id);


        Map form = new HashMap();
        form.put("studentNum",studentRepository.findById1(id).getStudentNum());
        if (s != null) {
            form.put("id", s.getId());
            form.put("studentName",s.getStudent().getStudentName());
            form.put("priSchool",s.getPriSchool());
            form.put("priTeacher",s.getPriTeacher());
            form.put("priPlace",s.getPriPlace());
            form.put("junSchool",s.getJunSchool());
            form.put("junPlace",s.getJunPlace());
            form.put("junTeacher",s.getJunTeacher());
            form.put("highSchool",s.getHighSchool());
            form.put("highPlace",s.getHighPlace());
            form.put("highTeacher",s.getHighTeacher());
        }
        return CommonMethod.getReturnData(form);
    };

    public synchronized Integer getNewPreinfoId(){
        Integer  id = preinfoRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };

    @PostMapping("/preinfoEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse preinfoEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String studentNum = CommonMethod.getString(form,"studentNum");

        //Integer id = preinfoRepository.findPreinfoByNumName1(studentNum).getId();
        Integer studentId = studentRepository.findByStudentNum1(CommonMethod.getString(form,"studentNum")).getId();
        String priSchool = CommonMethod.getString(form,"priSchool");
        String priTeacher= CommonMethod.getString(form,"priTeacher");
        String priPlace = CommonMethod.getString(form,"priPlace");
        String junSchool = CommonMethod.getString(form,"junSchool");
        String junTeacher= CommonMethod.getString(form,"junTeacher");
        String junPlace = CommonMethod.getString(form,"junPlace");
        String highSchool = CommonMethod.getString(form,"highSchool");
        String highTeacher= CommonMethod.getString(form,"highTeacher");
        String highPlace = CommonMethod.getString(form,"highPlace");


        Preinfo s= null;
        Optional<Preinfo> op;
        if(studentId != null) {
            s=preinfoRepository.findPreinfoBysId(studentId);
        }
        if(s == null) {
            s = new Preinfo();   //不存在 创建实体对象
             Integer id = getNewPreinfoId(); //获取鑫的主键，这个是线程同步问题;
            s.setId(id);  //设置新的id
        }
        s.setStudent(studentRepository.findById(studentId).get());
        s.setPriSchool(priSchool);
        s.setPriPlace(priPlace);
        s.setPriTeacher(priTeacher);
        s.setJunSchool(junSchool);
        s.setJunPlace(junPlace);
        s.setJunTeacher(junTeacher);
        s.setHighSchool(highSchool);
        s.setHighPlace(highPlace);
        s.setHighTeacher(highTeacher);
        preinfoRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(s.getId());  // 将记录的id返回前端
    }

    @PostMapping("/introduceEditInit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse introduceEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        System.out.println("-----------ID为"+id);
        StudentInfo si =null;

        Optional<StudentInfo> op;
        if(id != null) {
            op =studentInfoRepository.findByStudentId(id);
            if(op.isPresent()) {
                si = op.get();
            }
        }
        Map form = new HashMap();
        if(si != null) {
            form.put("address",si.getAddress());
            form.put("studentNum",si.getStudent().getStudentNum());
            form.put("graduateSchool",si.getGraduateSchool());
            form.put("competition",si.getCompetition());
            form.put("socialPractice",si.getSocialPractice());
            form.put("credit",si.getCredit());
        }
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }

    @PostMapping("/introduceEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse introduceEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        System.out.println("----------------到了提交这里");
        Integer id = studentRepository.findByStudentNum1(CommonMethod.getString(form,"studentNum")).getId();
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        String address = CommonMethod.getString(form,"address");
        String graduateSchool=CommonMethod.getString(form,"graduateSchool");
        String competition=CommonMethod.getString(form,"competition");
        String socialPractice=CommonMethod.getString(form,"socialPractice");
        String credit=CommonMethod.getString(form,"credit");
        StudentInfo si= null;
        Optional<StudentInfo> op;
        if(id != null) {
            op= studentInfoRepository.findBySdId(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                si = op.get();
            }

        }
        int infoid;
        if(si == null) {
            si = new StudentInfo();   //不存在 创建实体对象
            infoid = getNewInfoId(); //获取鑫的主键，这个是线程同步问题;
            si.setId(infoid);  //设置新的id
        }
        //si.setStudent(studentInfoRepository.findByStudentId(studentId).get());
        System.out.println("-----------------");
        si.setStudent(studentRepository.findById(id).get());
        si.setAddress(address);
        System.out.println(si.getAddress());
        si.setGraduateSchool(graduateSchool);
        System.out.println(si.getGraduateSchool());
        si.setCompetition(competition);
        System.out.println(si.getCompetition());
        si.setSocialPractice(socialPractice);
        System.out.println(si.getSocialPractice());
        si.setCredit(credit);
        System.out.println(si.getCredit());
        studentInfoRepository.save(si);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(si.getId());  // 将记录的id返回前端
    }

}
