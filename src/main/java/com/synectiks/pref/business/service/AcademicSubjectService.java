package com.synectiks.pref.business.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.synectiks.pref.domain.AttendanceMaster;
import com.synectiks.pref.domain.Batch;
import com.synectiks.pref.domain.Department;
import com.synectiks.pref.domain.ExceptionRecord;
import com.synectiks.pref.domain.Section;
import com.synectiks.pref.domain.Subject;
import com.synectiks.pref.domain.Teach;
import com.synectiks.pref.domain.Teacher;
import com.synectiks.pref.domain.enumeration.SectionEnum;
import com.synectiks.pref.domain.enumeration.Status;
import com.synectiks.pref.domain.enumeration.SubTypeEnum;
import com.synectiks.pref.domain.vo.CmsSubjectVo;
import com.synectiks.pref.repository.AttendanceMasterRepository;
import com.synectiks.pref.repository.ExceptionRecordRepository;
import com.synectiks.pref.repository.SectionRepository;
import com.synectiks.pref.repository.SubjectRepository;
import com.synectiks.pref.repository.TeachRepository;
import com.synectiks.pref.service.util.CommonUtil;

@Component
public class AcademicSubjectService {

	private final static Logger logger = LoggerFactory.getLogger(AcademicSubjectService.class);
	
    @Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private TeachRepository teachRepository;

	@Autowired
	private AttendanceMasterRepository attendanceMasterRepository;

	@Autowired
	private SectionRepository sectionRepository;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	private ExceptionRecordRepository exceptionRecordRepository;
	
//    @Transactional(propagation=Propagation.REQUIRED)
//    public QueryResult insertSubjectAndTeachRecord(AcademicSubjectMutationPayload academicSubjectMutationPayload) throws JSONException, ParseException {
//        String[] subjectData = academicSubjectMutationPayload.getSubjectData();
//        QueryResult res = new QueryResult();
//        res.setStatusCode(0);
//    	res.setStatusDesc("Records inserted successfully.");
//    	
//        JSONObject jsonObj = null;
//        try {
//        	for (String sub : subjectData) {
//                sub = sub.replaceAll("\\{", "\\{\"").replaceAll("=", "\":\"").replaceAll(",", "\",\"").replaceAll(" ", "").replaceAll("\\}", "\"\\}");
//                logger.debug(String.format("Array contents : %s",sub));
//                jsonObj = new JSONObject(sub);
//                
//                Subject subject = new Subject();
//                subject.setSubjectCode(jsonObj.getString("subjectCode"));
//                subject.setSubjectDesc(jsonObj.getString("subjectDesc"));
//                // in case of null (default) subject type will be common
//                if(SubTypeEnum.ELECTIVE.toString().equalsIgnoreCase(jsonObj.getString("subjectType"))) {
//                	subject.setSubjectType(SubTypeEnum.ELECTIVE);
//                }else {
//                	subject.setSubjectType(SubTypeEnum.COMMON);
//                }
//                // in case of null (default) subject status will be active
//                if(Status.DEACTIVE.toString().equalsIgnoreCase(jsonObj.getString("status"))) {
//                	subject.setStatus(Status.DEACTIVE);
//                }else {
//                	subject.setStatus(Status.ACTIVE);
//                }
//                Department dept = commonService.getDepartmentById(Long.parseLong(jsonObj.getString("departmentId")));
//                Batch bth = commonService.getBatchById(Long.parseLong(jsonObj.getString("batchId")));
//                subject.setDepartment(dept);
//                subject.setBatch(bth);
//                subject = this.subjectRepository.save(subject);
//                
//                Teach teach = new Teach();
//                teach.setDesc(jsonObj.getString("subjectDesc"));
//                teach.setSubject(subject);
//                Teacher teacher = commonService.getTeacherById(Long.parseLong(jsonObj.getString("teacherId")));
//                teach.setTeacher(teacher);
//                this.teachRepository.save(teach);
//
//            }
//        }catch(Exception e) {
//        	logger.error("Exception. There is some error in inserting subject and teach records. ",e);
//    		res.setStatusCode(1);
//        	res.setStatusDesc("Error in inserting subject and teach records.");
//        }
//        
//       return res;
//    }
    
//    @Transactional(propagation=Propagation.REQUIRED)
//    public QueryResult updateSubjectAndTeachRecord(AcademicSubjectMutationPayload academicSubjectMutationPayload) throws JSONException, ParseException {
//        
//    	String[] subjectData = academicSubjectMutationPayload.getSubjectData();
//        JSONObject jsonObj = null;
//        QueryResult res = new QueryResult();
//        res.setStatusCode(0);
//    	res.setStatusDesc("Records updated successfully.");
//    	try {
//    		for (String sub : subjectData) {
//                sub = sub.replaceAll("\\{", "\\{\"").replaceAll("=", "\":\"").replaceAll(",", "\",\"").replaceAll(" ", "").replaceAll("\\}", "\"\\}");
//                jsonObj = new JSONObject(sub);
//                
//                Subject subject = new Subject();
//                subject.setId(Long.parseLong(jsonObj.getString("subjectId")));
//                subject.setSubjectCode(jsonObj.getString("subjectCode"));
//                subject.setSubjectDesc(jsonObj.getString("subjectDesc"));
//                // While update, subject type must be provided by end user
//                if(SubTypeEnum.ELECTIVE.toString().equalsIgnoreCase(jsonObj.getString("subjectType"))) {
//                	subject.setSubjectType(SubTypeEnum.ELECTIVE);
//                }else if(SubTypeEnum.COMMON.toString().equalsIgnoreCase(jsonObj.getString("subjectType"))){
//                	subject.setSubjectType(SubTypeEnum.COMMON);
//                }
//                // While update, status must be provided by end user
//                if(Status.DEACTIVE.toString().equalsIgnoreCase(jsonObj.getString("status"))) {
//                	subject.setStatus(Status.DEACTIVE);
//                }else if(Status.ACTIVE.toString().equalsIgnoreCase(jsonObj.getString("status"))){
//                	subject.setStatus(Status.ACTIVE);
//                }
//                
//                Department dept = commonService.getDepartmentById(Long.parseLong(jsonObj.getString("departmentId")));
//                Batch bth = commonService.getBatchById(Long.parseLong(jsonObj.getString("batchId")));
//                subject.setDepartment(dept);
//                subject.setBatch(bth);
//                subject = this.subjectRepository.save(subject);
//                
//                Teach teach = new Teach();
//                teach.setId(Long.parseLong(jsonObj.getString("teachId")));
//                Teacher teacher = commonService.getTeacherById(Long.parseLong(jsonObj.getString("teacherId")));
//                teach.setTeacher(teacher);
//                this.teachRepository.save(teach);
//            }
//    	}catch(Exception e) {
//        	logger.error("Exception. There is some error in updating subject and teach records. ",e);
//    		res.setStatusCode(1);
//        	res.setStatusDesc("Error in updating subject and teach records.");
//        }
//        
//     return res;
//
//    }
    
//    public List<Subject> getAcademicSubjects(AcademicSubjectQueryPayload academicSubjectQueryPayload) {
//        List<Subject> subjectList = getSubjectList(Long.valueOf(academicSubjectQueryPayload.getDepartmentId()), 
//        		Long.valueOf(academicSubjectQueryPayload.getBatchId()));
//        return subjectList;
//    }

    public List<Subject> getSubjectList(Long departmentId, Long batchId)  {
		Department department = this.commonService.getDepartmentById(departmentId);
		Batch batch = this.commonService.getBatchById(batchId);
		Subject sub = new Subject();
		sub.setDepartment(department);
		sub.setBatch(batch);
		Example<Subject> example = Example.of(sub);
		List<Subject> list = this.subjectRepository.findAll(example, Sort.by(Direction.DESC, "id"));
		return list;
	}
    public List<Subject> getSubjectList(Long departmentId)  {
		Department department = this.commonService.getDepartmentById(departmentId);
		Subject sub = new Subject();
		sub.setDepartment(department);
		Example<Subject> example = Example.of(sub);
		List<Subject> list = this.subjectRepository.findAll(example, Sort.by(Direction.DESC, "id"));
		return list;
	}
    public List<CmsSubjectVo> getAllSubjectAndTeachData(Map<String, String> dataMap){
    	List<Teach> teachList = this.teachRepository.findAll();
    	List<CmsSubjectVo> ls = new ArrayList<>();
    	for(Teach th: teachList ) {
    		Department dp = th.getSubject().getDepartment();// this.commonService.getDepartmentById(th.getSubject().getDepartment().getId());
    		Batch bt = th.getSubject().getBatch(); //this.commonService.getBatchById(th.getSubject().getBatch().getId());
    		logger.debug("Department : "+dp+", Batch: "+bt);
    		CmsSubjectVo vo = new CmsSubjectVo();
    		vo.setDepartment(dp);
    		vo.setBatch(bt);
    		vo.setSubject(th.getSubject());
    		vo.setTeacher(th.getTeacher());
    		vo.setTeach(th);
    		vo.setTeacherId(th.getTeacher().getId());
    		
    		vo.setId(th.getSubject().getId());
    		vo.setStatus(th.getSubject().getStatus());
    		vo.setSubjectCode(th.getSubject().getSubjectCode());
    		vo.setSubjectDesc(th.getSubject().getSubjectDesc());
    		vo.setSubjectType(th.getSubject().getSubjectType());
    		if (dataMap.containsKey("unfiltered-records")) {
    			logger.debug("Getting all subjects without considering status: "+th.getSubject().toString());
    			ls.add(vo);
    		}else {
    			logger.debug("Getting subjects for ACTIVE status only: "+th.getSubject().toString());
    			if(th.getSubject().getStatus().equals(Status.ACTIVE)) {
    				ls.add(vo);
    			}
    		}
    		
    	}
    	return ls;
    }
    

    public List<Subject> getAllSubjects(Map<String, String> dataMap) {
		Subject subject = new Subject();
//		subject.setStatus(Status.ACTIVE);
    	if(dataMap.containsKey("subCode")) {
    		String subjectCode = dataMap.get("subCode");
    		subject.setSubjectCode(subjectCode);
    	}
		if (dataMap.containsKey("deptId")) {
			String deptId = dataMap.get("deptId");
			Department dt = commonService.getDepartmentById(Long.parseLong(deptId));
			logger.debug("Department retrieved by given department id : "+dt.toString());
			subject.setDepartment(dt);
		}
		if (dataMap.containsKey("batchId")) {
			String batchId = dataMap.get("batchId");
			Batch bt = commonService.getBatchById(Long.parseLong(batchId));
			logger.debug("Batch retrieved by given batch id : "+bt.toString());
			subject.setBatch(bt);
		}
		List<Subject> list = null;
		if(dataMap.size() > 0) {
			Example<Subject> example = Example.of(subject);
			list = this.subjectRepository.findAll(example);
		}else {
			list = this.subjectRepository.findAll();
		}
		
		logger.info("Total subjects retrieved: "+list.size());
		return list;
	}
    
    
    
    @Transactional(propagation=Propagation.REQUIRED)
	public void createSubjects(List<CmsSubjectVo> list) {
		for (Iterator<CmsSubjectVo> iterator = list.iterator(); iterator.hasNext();) {
			CmsSubjectVo dto = iterator.next();
			Subject sub = new Subject();
			sub.setSubjectCode(dto.getSubjectCode());
			if(dto.getSubjectType() == null) {
				sub.setSubjectType(SubTypeEnum.COMMON);
			}else {
				sub.setSubjectType(dto.getSubjectType());
			}
			
			if(dto.getSubjectDesc() != null) {
				sub.setSubjectDesc(dto.getSubjectDesc());
			}else {
				sub.setSubjectDesc(dto.getSubjectCode());
			}
			
			if(dto.getStatus() != null) {
				sub.setStatus(dto.getStatus());
			}else {
				sub.setStatus(Status.ACTIVE);
			}
			
			Department dt = commonService.getDepartmentById(dto.getDepartmentId());
			sub.setDepartment(dt);
			Batch bt =  commonService.getBatchById(dto.getBatchId());
			sub.setBatch(bt);
			
			Subject newSub = this.subjectRepository.save(sub);
			logger.info("Subject data saved.");
			Teach teach = new Teach();
			teach.setSubject(newSub);
			Teacher teacher = commonService.getTeacherById(dto.getTeacherId());
			teach.setTeacher(teacher);
			teach.setDesc(dto.getSubjectCode());
			this.teachRepository.save(teach);
			logger.info("Teach data saved.");
			
			AttendanceMaster am = new AttendanceMaster();
			am.setTeach(teach);
			am.setBatch(bt);
			am = this.attendanceMasterRepository.save(am);
			logger.info("Attendance Master data saved.");
			
		}
		logger.info("Subject and Teach records saved into database successfully.");
		
	}
    
    @Transactional(propagation=Propagation.REQUIRED)
	public CmsSubjectVo createSubject(CmsSubjectVo cmsSubjectVo, Map<String, String> dataMap) {
		Subject sub = new Subject();
		sub.setSubjectCode(cmsSubjectVo.getSubjectCode());
		if(cmsSubjectVo.getSubjectType() == null) {
			sub.setSubjectType(SubTypeEnum.COMMON);
		}else {
			sub.setSubjectType(cmsSubjectVo.getSubjectType());
		}
		
		if(cmsSubjectVo.getSubjectDesc() != null) {
			sub.setSubjectDesc(cmsSubjectVo.getSubjectDesc());
		}else {
			sub.setSubjectDesc(cmsSubjectVo.getSubjectCode());
		}
		
		if(cmsSubjectVo.getStatus() == null) {
			sub.setStatus(Status.DEACTIVE);
		}else {
			sub.setStatus(cmsSubjectVo.getStatus());
		}
		Department dt = commonService.getDepartmentById(cmsSubjectVo.getDepartmentId());
		Batch bt =  commonService.getBatchById(cmsSubjectVo.getBatchId());
		sub.setDepartment(dt);
		sub.setBatch(bt);
		sub = this.subjectRepository.save(sub);
		logger.info("Subject data saved.");
		
		Teacher teacher = commonService.getTeacherById(cmsSubjectVo.getTeacherId());
		
		Teach teach = new Teach();
		teach.setSubject(sub);
		teach.setTeacher(teacher);
		teach.setDesc(cmsSubjectVo.getSubjectCode());
		teach = this.teachRepository.save(teach);
		logger.info("Teach data saved.");
		
		String sectionA = dataMap.get("sectionA");
		String sectionB = dataMap.get("sectionB");
		String sectionC = dataMap.get("sectionC");
		String sectionD = dataMap.get("sectionD");
		
		if(!CommonUtil.isNullOrEmpty(sectionA) && !"undefined".equalsIgnoreCase(sectionA) && !"null".equalsIgnoreCase(sectionA)) {
			Section section = new Section();
			section.setBatch(bt);
			section.setSection(SectionEnum.A);
			Optional<Section> ose = this.sectionRepository.findOne(Example.of(section));
			if(ose.isPresent()) {
				AttendanceMaster am = new AttendanceMaster();
				am.setTeach(teach);
				am.setBatch(bt);
				am.setSection(ose.get());
				am = this.attendanceMasterRepository.save(am);
			}else {
				ExceptionRecord er = getExceptionObject(SectionEnum.A, bt);
				this.exceptionRecordRepository.save(er);
			}
		}
		
		if(!CommonUtil.isNullOrEmpty(sectionB) && !"undefined".equalsIgnoreCase(sectionB) && !"null".equalsIgnoreCase(sectionB)) {
			Section section = new Section();
			section.setBatch(bt);
			section.setSection(SectionEnum.B);
			Optional<Section> ose = this.sectionRepository.findOne(Example.of(section));
			if(ose.isPresent()) {
				AttendanceMaster am = new AttendanceMaster();
				am.setTeach(teach);
				am.setBatch(bt);
				am.setSection(ose.get());
				am = this.attendanceMasterRepository.save(am);
			}else {
				ExceptionRecord er = getExceptionObject(SectionEnum.B, bt);
				this.exceptionRecordRepository.save(er);
			}
		}
		
		if(!CommonUtil.isNullOrEmpty(sectionC) && !"undefined".equalsIgnoreCase(sectionC) && !"null".equalsIgnoreCase(sectionC)) {
			Section section = new Section();
			section.setBatch(bt);
			section.setSection(SectionEnum.C);
			Optional<Section> ose = this.sectionRepository.findOne(Example.of(section));
			if(ose.isPresent()) {
				AttendanceMaster am = new AttendanceMaster();
				am.setTeach(teach);
				am.setBatch(bt);
				am.setSection(ose.get());
				am = this.attendanceMasterRepository.save(am);
			}else {
				ExceptionRecord er = getExceptionObject(SectionEnum.C, bt);
				this.exceptionRecordRepository.save(er);
			}
			
		}
		if(!CommonUtil.isNullOrEmpty(sectionD) && !"undefined".equalsIgnoreCase(sectionD) && !"null".equalsIgnoreCase(sectionD)) {
			Section section = new Section();
			section.setBatch(bt);
			section.setSection(SectionEnum.D);
			Optional<Section> ose = this.sectionRepository.findOne(Example.of(section));
			if(ose.isPresent()) {
				AttendanceMaster am = new AttendanceMaster();
				am.setTeach(teach);
				am.setBatch(bt);
				am.setSection(ose.get());
				am = this.attendanceMasterRepository.save(am);
			}else {
				ExceptionRecord er = getExceptionObject(SectionEnum.D, bt);
				this.exceptionRecordRepository.save(er);
			}
		}
		
		logger.info("Attendance Master data saved.");
		
		cmsSubjectVo = CommonUtil.createCopyProperties(sub, CmsSubjectVo.class);
		cmsSubjectVo.setId(sub.getId());
		cmsSubjectVo.setTeacher(teacher);
		cmsSubjectVo.setTeacherId(teacher.getId());
		
		logger.info("Subject and Teach records saved in database successfully.");
		return cmsSubjectVo;
		
	}
    
    
    @Transactional(propagation=Propagation.REQUIRED)
	public CmsSubjectVo updateSubject(CmsSubjectVo cmsSubjectVo, Map<String, String> dataMap) {
//		Subject sub = CommonUtil.createCopyProperties(cmsSubjectVo, Subject.class);
    	Subject sub = this.subjectRepository.findById(cmsSubjectVo.getId()).get();
    	sub.setSubjectCode(cmsSubjectVo.getSubjectCode());
    	sub.setSubjectDesc(cmsSubjectVo.getSubjectDesc());
    	sub.setStatus(cmsSubjectVo.getStatus());
    	
		Department dt = commonService.getDepartmentById(cmsSubjectVo.getDepartmentId());
		Batch bt =  commonService.getBatchById(cmsSubjectVo.getBatchId());
//		sub.setDepartment(dt);
		sub.setBatch(bt);
		sub = this.subjectRepository.save(sub);
		logger.info("Subject data updated.");
		
		Teacher teacher = commonService.getTeacherById(cmsSubjectVo.getTeacherId());
		
		// find the existing record in teach table for given teacher and subject. If not exists, create a new teach record.
		Teach teach = new Teach();
		teach.setSubject(sub);
		teach.setTeacher(teacher);
		Example<Teach> example = Example.of(teach);
		
		if(!this.teachRepository.exists(example)) {
			teach.setDesc(cmsSubjectVo.getSubjectCode());
			teach = this.teachRepository.save(teach);
			logger.info("Teach data updated.");
		}else {
			teach = this.teachRepository.findOne(example).get();
		}
		
		String sectionA = dataMap.get("sectionA");
		String sectionB = dataMap.get("sectionB");
		String sectionC = dataMap.get("sectionC");
		String sectionD = dataMap.get("sectionD");
		
		AttendanceMaster am = new AttendanceMaster();
		am.setTeach(teach);
		am.setBatch(bt);
		Section section = new Section();
		section.setBatch(bt);
		section.setSection(SectionEnum.A);
		Optional<Section> ose = this.sectionRepository.findOne(Example.of(section));
		if(ose.isPresent()) {
			if(CommonUtil.isNullOrEmpty(sectionA) || "undefined".equalsIgnoreCase(sectionA) || "null".equalsIgnoreCase(sectionA)) {
				am.setSection(null);
				am = this.attendanceMasterRepository.save(am);
			}
		}
		am = new AttendanceMaster();
		am.setTeach(teach);
		am.setBatch(bt);
		section = new Section();
		section.setBatch(bt);
		section.setSection(SectionEnum.B);
		ose = this.sectionRepository.findOne(Example.of(section));
		if(ose.isPresent()) {
			if(CommonUtil.isNullOrEmpty(sectionB) || "undefined".equalsIgnoreCase(sectionB) || "null".equalsIgnoreCase(sectionB)) {
				am.setSection(null);
				am = this.attendanceMasterRepository.save(am);
			}
		}
		
		am = new AttendanceMaster();
		am.setTeach(teach);
		am.setBatch(bt);
		section = new Section();
		section.setBatch(bt);
		section.setSection(SectionEnum.C);
		ose = this.sectionRepository.findOne(Example.of(section));
		if(ose.isPresent()) {
			if(CommonUtil.isNullOrEmpty(sectionC) || "undefined".equalsIgnoreCase(sectionC) || "null".equalsIgnoreCase(sectionC)) {
				am.setSection(null);
				am = this.attendanceMasterRepository.save(am);
			}
		}
		
		am = new AttendanceMaster();
		am.setTeach(teach);
		am.setBatch(bt);
		section = new Section();
		section.setBatch(bt);
		section.setSection(SectionEnum.D);
		ose = this.sectionRepository.findOne(Example.of(section));
		if(ose.isPresent()) {
			am.setSection(ose.get());
			if(!this.attendanceMasterRepository.exists(Example.of(am))) {
				if(!CommonUtil.isNullOrEmpty(sectionD) && !"undefined".equalsIgnoreCase(sectionD) && !"null".equalsIgnoreCase(sectionD)) {
					am = this.attendanceMasterRepository.save(am);
				}
			}else {
				if(CommonUtil.isNullOrEmpty(sectionD) || "undefined".equalsIgnoreCase(sectionD) || "null".equalsIgnoreCase(sectionD)) {
					am.setSection(null);
					am = this.attendanceMasterRepository.save(am);
				}
			}
			
		}
		
		CmsSubjectVo vo = CommonUtil.createCopyProperties(sub, CmsSubjectVo.class);
		vo.setId(sub.getId());
		vo.setTeacher(teacher);
		vo.setTeacherId(teacher.getId());
		
		logger.info("Subject, Teach and attendance master records updated in database successfully.");
		return vo;
		
	}
    
    private ExceptionRecord getExceptionObject(SectionEnum se, Batch batch) {
		ExceptionRecord obj = new ExceptionRecord(); 
		obj.setExceptionSource("AcademicSubjectService");
		obj.setExceptionType("DataNotFoundException : ");
		obj.setExceptionRecord("Section :"+se.toString()+" for batch : "+batch.getBatch().toString()+" not found");
		obj.setExceptionDate(LocalDate.now());
		return obj;
	}
    
    public List<Teach> getAllSubjectsWithTeacher(Long departmentId){
    	Department department = this.commonService.getDepartmentById(departmentId);
    	Subject subject = new Subject();
    	Teacher teacher = new Teacher();
    	subject.setDepartment(department);
    	teacher.setDepartment(department);
    	Teach teach = new Teach();
    	teach.setSubject(subject);
    	teach.setTeacher(teacher);
    	List<Teach> teachList = this.teachRepository.findAll(Example.of(teach));
    	return teachList;
    }
}
