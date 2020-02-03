package com.synectiks.pref.dataimport.loader;

import java.util.Optional;

import org.dhatim.fastexcel.reader.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.synectiks.pref.constant.CmsConstants;
import com.synectiks.pref.dataimport.AllRepositories;
import com.synectiks.pref.dataimport.DataLoader;
import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.domain.City;
import com.synectiks.pref.domain.Country;
import com.synectiks.pref.domain.Department;
import com.synectiks.pref.domain.Employee;
import com.synectiks.pref.domain.State;
import com.synectiks.pref.domain.Teacher;
import com.synectiks.pref.domain.enumeration.BatchEnum;
import com.synectiks.pref.exceptions.DuplicateRecordFoundException;
import com.synectiks.pref.exceptions.MandatoryFieldMissingException;
import com.synectiks.pref.service.util.CommonUtil;
import com.synectiks.pref.service.util.DateFormatUtil;

public class TeacherDataLoader extends DataLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AllRepositories allRepositories;
    private String sheetName;

    public TeacherDataLoader(String sheetName, AllRepositories allRepositories) {
        super(sheetName, allRepositories);
        this.allRepositories = allRepositories;
        this.sheetName = sheetName;
    }

    @Override
    public <T> T getObject(Row row, Class<T> cls) throws InstantiationException, IllegalAccessException, DuplicateRecordFoundException , MandatoryFieldMissingException {
        StringBuilder sb = new StringBuilder();

        Teacher obj = CommonUtil.createCopyProperties(cls.newInstance(), Teacher.class);
        
        String teacherName = row.getCellAsString(0).orElse(null);
        if(CommonUtil.isNullOrEmpty(teacherName)) {
        	sb.append("teacher_name, ");
            logger.warn("Mandatory field missing. Field name - teacher_name");
        }else {
        	obj.setTeacherName(teacherName);
        }
        
        String teacherMiddleName = row.getCellAsString(1).orElse(null);
        if(CommonUtil.isNullOrEmpty(teacherMiddleName)) {
        	sb.append("teacher_middle_name, ");
            logger.warn("Mandatory field missing. Field name - teacher_middle_name");
        }else {
        	obj.setTeacherMiddleName(teacherMiddleName);
        }
        
        String teacherLastName = row.getCellAsString(2).orElse(null);
        if(CommonUtil.isNullOrEmpty(teacherLastName)) {
        	sb.append("teacher_last_name, ");
            logger.warn("Mandatory field missing. Field name - teacher_last_name");
        }else {
        	obj.setTeacherLastName(teacherLastName);
        }
        
        String fatherName = row.getCellAsString(3).orElse(null);
        if(CommonUtil.isNullOrEmpty(fatherName)) {
        	sb.append("father_name, ");
            logger.warn("Mandatory field missing. Field name - father_name");
        }else {
        	obj.setFatherName(fatherName);
        }
        
        String fatherMiddleName = row.getCellAsString(4).orElse(null);
        if(CommonUtil.isNullOrEmpty(fatherMiddleName)) {
        	sb.append("father_middle_name, ");
            logger.warn("Mandatory field missing. Field name - father_middle_name");
        }else {
        	obj.setFatherMiddleName(fatherMiddleName);
        }
        
        String fatherLastName = row.getCellAsString(5).orElse(null);
        if(CommonUtil.isNullOrEmpty(fatherLastName)) {
        	sb.append("father_last_name, ");
            logger.warn("Mandatory field missing. Field name - father_last_name");
        }else {
        	obj.setFatherLastName(fatherLastName);
        }
        
        
        String spouseName = row.getCellAsString(6).orElse(null);
        obj.setSpouseName(spouseName);
        
        String spouseMiddleName = row.getCellAsString(7).orElse(null);
        obj.setSpouseMiddleName(spouseMiddleName);
        
        String spouseLastName = row.getCellAsString(8).orElse(null);
        obj.setSpouseLastName(spouseLastName);
        
        String motherName = row.getCellAsString(9).orElse(null);
        if(CommonUtil.isNullOrEmpty(motherName)) {
        	sb.append("mother_name, ");
            logger.warn("Mandatory field missing. Field name - mother_name");
        }else {
        	obj.setMotherName(motherName);
        }
        
        String motherMiddleName = row.getCellAsString(10).orElse(null);
        if(CommonUtil.isNullOrEmpty(motherMiddleName)) {
        	sb.append("mother_middle_name, ");
            logger.warn("Mandatory field missing. Field name - mother_middle_name");
        }else {
        	obj.setMotherMiddleName(motherMiddleName);
        }
        
        String motherLastName = row.getCellAsString(11).orElse(null);
        if(CommonUtil.isNullOrEmpty(motherLastName)) {
        	sb.append("mother_last_name, ");
            logger.warn("Mandatory field missing. Field name - mother_last_name");
        }else {
        	obj.setMotherLastName(motherLastName);
        }
        
        String aadharNo = row.getCellAsString(12).orElse(null);
        if(CommonUtil.isNullOrEmpty(aadharNo)) {
        	sb.append("aadhar_no, ");
            logger.warn("Mandatory field missing. Field name - aadhar_no");
        }else {
        	obj.setAadharNo(aadharNo);
        }
        
        String dob = row.getCellAsString(13).orElse(null);
        if(CommonUtil.isNullOrEmpty(dob)) {
        	sb.append("date_of_birth, ");
            logger.warn("Mandatory field missing. Field name - date_of_birth");
        }else {
        	obj.setDateOfBirth(DateFormatUtil.convertStringToLocalDate(dob, CmsConstants.DATE_FORMAT_dd_MM_yyyy));
        }
        
        String placeOfBirth = row.getCellAsString(14).orElse(null);
        if(CommonUtil.isNullOrEmpty(placeOfBirth)) {
        	sb.append("place_of_birth, ");
            logger.warn("Mandatory field missing. Field name - place_of_birth");
        }else {
        	obj.setPlaceOfBirth(placeOfBirth);
        }
        
        String religion = row.getCellAsString(15).orElse(null);
        if(CommonUtil.isNullOrEmpty(religion)) {
        	sb.append("religion, ");
            logger.warn("Mandatory field missing. Field name - religion");
        }else {
        	obj.setReligion(religion);
        }
        
        String cast = row.getCellAsString(16).orElse(null);
        if(CommonUtil.isNullOrEmpty(cast)) {
        	sb.append("cast, ");
            logger.warn("Mandatory field missing. Field name - caste");
        }else {
        	obj.setCaste(cast);
        }
        
        String subCast = row.getCellAsString(17).orElse(null);
        if(CommonUtil.isNullOrEmpty(subCast)) {
        	sb.append("sub_caste, ");
            logger.warn("Mandatory field missing. Field name - sub_caste");
        }else {
        	obj.setSubCaste(subCast);
        	
        }
        
        String age = row.getCellAsString(18).orElse(null);
        if(CommonUtil.isNullOrEmpty(age)) {
        	sb.append("age, ");
            logger.warn("Mandatory field missing. Field name - age");
        }else {
        	obj.setAge(Integer.parseInt(age));
        }
        
        String sex = row.getCellAsString(19).orElse(null);
        if(CommonUtil.isNullOrEmpty(sex)) {
        	sb.append("sex, ");
            logger.warn("Mandatory field missing. Field name - sex");
        }else {
        	obj.setSex(sex);
        }
        
        String bloodGroup = row.getCellAsString(20).orElse(null);
        if(CommonUtil.isNullOrEmpty(bloodGroup)) {
        	sb.append("blood_group, ");
            logger.warn("Mandatory field missing. Field name - blood_group");
        }else {
        	obj.setBloodGroup(bloodGroup);
        }
        
        String addressLineOne = row.getCellAsString(21).orElse(null);
        if(CommonUtil.isNullOrEmpty(addressLineOne)) {
        	sb.append("address_line_one, ");
            logger.warn("Mandatory field missing. Field name - address_line_one");
        }else {
        	obj.setAddress(addressLineOne);
        }
        
//        String addressLineTwo = row.getCellAsString(22).orElse(null);
//        if(CommonUtil.isNullOrEmpty(addressLineTwo)) {
//        	sb.append("address_line_two, ");
//            logger.warn("Mandatory field missing. Field name - address_line_two");
//        }else {
//        	obj.setAddressLineTwo(addressLineTwo);
//        }
//        
//        String addressLineThree = row.getCellAsString(23).orElse(null);
//        if(CommonUtil.isNullOrEmpty(addressLineThree)) {
//        	sb.append("address_line_three, ");
//            logger.warn("Mandatory field missing. Field name - address_line_three");
//        }else {
//        	obj.setAddressLineThree(addressLineThree);
//        }
//        
        Optional<State> ostate = null;
        String stateName = row.getCellAsString(25).orElse(null);
		if(CommonUtil.isNullOrEmpty(stateName)) {
			sb.append("state_id, ");
			logger.warn("Mandatory field missing. Field name - state_id");
		}else {
			State state = new State();
			state.setStateName(stateName);
			ostate = this.allRepositories.findRepository("state").findOne(Example.of(state));
			if(ostate != null && ostate.isPresent()) {
				obj.setState(ostate.get().getStateName());
			}else {
				sb.append("state_id, ");
				logger.warn("State not found. Given state name : "+stateName);
			}
		}
        
		String cityName = row.getCellAsString(24).orElse(null);
		if(CommonUtil.isNullOrEmpty(cityName)) {
			sb.append("town, ");
			logger.warn("Mandatory field missing. Field name - town");
		}else {
			Optional<City> c = null;
			if(ostate != null && ostate.isPresent()) {
				City city = new City();
				city.setCityName(cityName);
				city.setState(ostate.get());
				c = this.allRepositories.findRepository("city").findOne(Example.of(city));
			}
			if(c != null && c.isPresent()) {
				obj.setTown(c.get().getCityName());
			}else {
				sb.append("town, ");
				logger.warn("Town/city not found. Given city/town name : "+cityName);
			}
		}
        
        String countryName = row.getCellAsString(26).orElse(null);
		if(CommonUtil.isNullOrEmpty(countryName)) {
			sb.append("country_id, ");
			logger.warn("Mandatory field missing. Field name - country_id");
		}else {
			Country country = new Country();
			country.countryName(countryName);
			Optional<Country> octry = this.allRepositories.findRepository("country").findOne(Example.of(country));
			if(octry.isPresent()) {
				obj.setCountry(octry.get().getCountryName());
			}else {
				sb.append("country_id, ");
				logger.warn("Country not found. Given country name : "+countryName);
			}
		}
		
		String pinCode = row.getCellAsString(27).orElse(null);
        if(CommonUtil.isNullOrEmpty(pinCode)) {
        	sb.append("pincode, ");
            logger.warn("Mandatory field missing. Field name - pincode");
        }else {
        	obj.setPinCode(pinCode);
        }
        
        String teacherContactNumber = row.getCellAsString(28).orElse(null);
        if(CommonUtil.isNullOrEmpty(teacherContactNumber)) {
        	sb.append("teacher_contact_number, ");
            logger.warn("Mandatory field missing. Field name - teacher_contact_number");
        }else {
        	obj.setTeacherContactNumber(teacherContactNumber);
        }
        
        String alternateContactNumber = row.getCellAsString(29).orElse(null);
        obj.setAlternateContactNumber(alternateContactNumber);
        
        String teacherEmailAddress = row.getCellAsString(30).orElse(null);
        if(CommonUtil.isNullOrEmpty(teacherEmailAddress)) {
        	sb.append("teacher_email_address, ");
            logger.warn("Mandatory field missing. Field name - teacher_email_address");
        }else {
        	obj.setTeacherEmailAddress(teacherEmailAddress);
        }
        
        String alternateEmailAddress = row.getCellAsString(31).orElse(null);
        obj.setAlternateEmailAddress(alternateEmailAddress);
        
        String relationWithStaff = row.getCellAsString(32).orElse(null);
        if(CommonUtil.isNullOrEmpty(relationWithStaff)) {
        	sb.append("relation_with_staff, ");
            logger.warn("Mandatory field missing. Field name - relation_with_staff");
        }else {
        	obj.setRelationWithStaff(relationWithStaff);
        }
        
        String emergencyContactName = row.getCellAsString(33).orElse(null);
        if(CommonUtil.isNullOrEmpty(emergencyContactName)) {
        	sb.append("emergency_contact_name, ");
            logger.warn("Mandatory field missing. Field name - emergency_contact_name");
        }else {
        	obj.setEmergencyContactName(emergencyContactName);
        }
        
        String emergencyContactMiddleName = row.getCellAsString(34).orElse(null);
        obj.setEmergencyContactMiddleName(emergencyContactMiddleName);
        
        String emergencyContactLastName = row.getCellAsString(35).orElse(null);
        if(CommonUtil.isNullOrEmpty(emergencyContactLastName)) {
        	sb.append("emergency_contact_last_name, ");
            logger.warn("Mandatory field missing. Field name - emergency_contact_last_name");
        }else {
        	obj.setEmergencyContactLastName(emergencyContactLastName);
        }
        
        String emergencyContactNo = row.getCellAsString(36).orElse(null);
        if(CommonUtil.isNullOrEmpty(emergencyContactNo)) {
        	sb.append("emergency_contact_no, ");
            logger.warn("Mandatory field missing. Field name - emergency_contact_no");
        }else {
        	obj.setEmergencyContactNo(emergencyContactNo);
        }
        
        String emergencyContactEmailAddress = row.getCellAsString(37).orElse(null);
        if(CommonUtil.isNullOrEmpty(emergencyContactEmailAddress)) {
        	sb.append("emergency_contact_email_address, ");
            logger.warn("Mandatory field missing. Field name - emergency_contact_email_address");
        }else {
        	obj.setEmergencyContactEmailAddress(emergencyContactEmailAddress);
        }
        
        obj.setUploadPhoto(""); // cell 38
        
        String status = row.getCellAsString(39).orElse(null);
        if(CommonUtil.isNullOrEmpty(status)) {
        	sb.append("status, ");
            logger.warn("Mandatory field missing. Field name - status");
        }else {
        	obj.setStatus(status);
        }
        
        Employee employee = new Employee();
    	String joiningDate = row.getCellAsString(40).orElse(null);
        if(CommonUtil.isNullOrEmpty(joiningDate)) {
        	sb.append("joining_date, ");
            logger.warn("Mandatory field missing. Field name - joining_date");
        }else {
        	employee.setJoiningDate(DateFormatUtil.convertStringToLocalDate(joiningDate, CmsConstants.DATE_FORMAT_dd_MM_yyyy));
        }
        String designation = row.getCellAsString(41).orElse(null);
        if(CommonUtil.isNullOrEmpty(designation)) {
        	sb.append("designation, ");
            logger.warn("Mandatory field missing. Field name - designation");
        }else {
        	employee.setDesignation(designation);
        	obj.setDesignation(designation);
        }
        employee.employeeName(teacherName + " "+teacherMiddleName + " "+teacherLastName);
        
        String staffType = row.getCellAsString(42).orElse(null);
        if(CommonUtil.isNullOrEmpty(staffType)) {
        	sb.append("staff_type, ");
            logger.warn("Mandatory field missing. Field name - staff_type");
        }else {
        	obj.setStaffType(staffType);
        }
        
        String departmentName = row.getCellAsString(43).orElse(null);
        if(CommonUtil.isNullOrEmpty(departmentName)) {
            sb.append("department_id, ");
            logger.warn("Mandatory field missing. Field name - department_id");
        }else {
        	String branchName = row.getCellAsString(44).orElse(null);
            String branchAddress = row.getCellAsString(45).orElse(null);
            if(CommonUtil.isNullOrEmpty(branchName) || CommonUtil.isNullOrEmpty(branchAddress)) {
            	sb.append("department_id, ");
                logger.warn("Either branch name or branch address not provided, Cannot identify that given department belongs to which branch");
            }else {
                Branch branch = new Branch();
                branch.setBranchName(branchName);
                branch.address(branchAddress);
                Optional<Branch> b = this.allRepositories.findRepository("branch").findOne(Example.of(branch));
                
                if(b.isPresent()) {
                	Department department = new Department();
                    department.setName(departmentName);
                    department.setBranch(b.get());
                    Optional<Department> dp = this.allRepositories.findRepository("department").findOne(Example.of(department));
                    if(dp.isPresent()) {
                        obj.setDepartment(dp.get());
                    }else {
                        sb.append("department_id, ");
                        logger.warn("Department not found. Given department name : " + departmentName);
                    }
                }else {
                	sb.append("department_id, ");
                    logger.warn("Either branch name or branch address not provided, Cannot identify that given department belongs to which branch");
                }
            }
        }
        
        
        String branchName = row.getCellAsString(44).orElse(null);
        String branchAddress = row.getCellAsString(45).orElse(null);
        if(CommonUtil.isNullOrEmpty(branchName) || CommonUtil.isNullOrEmpty(branchAddress)) {
        	sb.append("branch_id, ");
            logger.warn("branch name or branch address not provided, Cannot find the branch");
        }else {
            Branch branch = new Branch();
            branch.setBranchName(branchName);
            branch.address(branchAddress);
            Optional<Branch> b = this.allRepositories.findRepository("branch").findOne(Example.of(branch));
            
            if(b.isPresent()) {
            	obj.setBranch(b.get());
            	employee.setBranch(b.get());
            }else {
            	sb.append("branch_id, ");
                logger.warn("branch name or branch address not provided, Cannot find the branch");
            }
        }
        
        if (sb.length() > 0) {
            String msg = "Field name - ";
            throw new MandatoryFieldMissingException(msg + sb.substring(0, sb.lastIndexOf(",")));
        }
        
        if (this.allRepositories.findRepository(this.sheetName).exists(Example.of(obj))) {
        	String msg = "Duplicate Teacher found";
        	sb.append(msg+",");
            logger.warn(msg);
            if (sb.length() > 0) {
                throw new DuplicateRecordFoundException(msg);
            }
        }
        
        if(!CommonUtil.isNullOrEmpty(employee.getEmployeeName()) && !CommonUtil.isNullOrEmpty(employee.getDesignation())
        		&& employee.getJoiningDate() != null && employee.getBranch() != null) {
        	logger.info("Saving record in employee table");
        	employee = (Employee)this.allRepositories.findRepository("employee").save(employee); 
        }
        
        if(employee.getId() != null && employee.getId() > 0) {
        	obj.setEmployeeId(employee.getId());
        }
        
        return (T)obj;
    }
    
    private BatchEnum findBatch(String batchName) {
    	if(BatchEnum.FIRSTYEAR.toString().equalsIgnoreCase(batchName)) {
    		return BatchEnum.FIRSTYEAR;
    	}else if(BatchEnum.SECONDYEAR.toString().equalsIgnoreCase(batchName)) {
    		return BatchEnum.SECONDYEAR;
    	}else if(BatchEnum.THIRDYEAR.toString().equalsIgnoreCase(batchName)) {
    		return BatchEnum.THIRDYEAR;
    	}else if(BatchEnum.FOURTHYEAR.toString().equalsIgnoreCase(batchName)) {
    		return BatchEnum.FOURTHYEAR;
    	}
    	return null;
    }
    
    
}
