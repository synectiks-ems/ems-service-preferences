package com.synectiks.pref.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import com.synectiks.pref.domain.enumeration.Religion;
import com.synectiks.pref.domain.enumeration.Caste;
import com.synectiks.pref.domain.enumeration.Gender;
import com.synectiks.pref.domain.enumeration.Bloodgroup;
import com.synectiks.pref.domain.enumeration.RelationWithStudentEnum;
import com.synectiks.pref.domain.enumeration.Status;
import com.synectiks.pref.domain.enumeration.StaffType;

/**
 * A DTO for the {@link com.synectiks.pref.domain.Teacher} entity.
 */
public class TeacherDTO implements Serializable {

    private Long id;

    private String teacherName;

    private String teacherMiddleName;

    private String teacherLastName;

    private String fatherName;

    private String fatherMiddleName;

    private String fatherLastName;

    private String spouseName;

    private String spouseMiddleName;

    private String spouseLastName;

    private String motherName;

    private String motherMiddleName;

    private String motherLastName;

    private Long aadharNo;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private Religion religion;

    private Caste caste;

    private String subCaste;

    private Integer age;

    private Gender sex;

    private Bloodgroup bloodGroup;

    private String addressLineOne;

    private String addressLineTwo;

    private String addressLineThree;

    private String town;

    private String state;

    private String country;

    private Long pincode;

    private String teacherContactNumber;

    private String alternateContactNumber;

    private String teacherEmailAddress;

    private String alternateEmailAddress;

    private RelationWithStudentEnum relationWithStaff;

    private String emergencyContactName;

    private String emergencyContactMiddleName;

    private String emergencyContactLastName;

    private String emergencyContactNo;

    private String emergencyContactEmailAddress;

    private String uploadPhoto;

    private Status status;

    private Long employeeId;

    private String designation;

    private StaffType staffType;


    private Long departmentId;

    private Long branchId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherMiddleName() {
        return teacherMiddleName;
    }

    public void setTeacherMiddleName(String teacherMiddleName) {
        this.teacherMiddleName = teacherMiddleName;
    }

    public String getTeacherLastName() {
        return teacherLastName;
    }

    public void setTeacherLastName(String teacherLastName) {
        this.teacherLastName = teacherLastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherMiddleName() {
        return fatherMiddleName;
    }

    public void setFatherMiddleName(String fatherMiddleName) {
        this.fatherMiddleName = fatherMiddleName;
    }

    public String getFatherLastName() {
        return fatherLastName;
    }

    public void setFatherLastName(String fatherLastName) {
        this.fatherLastName = fatherLastName;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getSpouseMiddleName() {
        return spouseMiddleName;
    }

    public void setSpouseMiddleName(String spouseMiddleName) {
        this.spouseMiddleName = spouseMiddleName;
    }

    public String getSpouseLastName() {
        return spouseLastName;
    }

    public void setSpouseLastName(String spouseLastName) {
        this.spouseLastName = spouseLastName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherMiddleName() {
        return motherMiddleName;
    }

    public void setMotherMiddleName(String motherMiddleName) {
        this.motherMiddleName = motherMiddleName;
    }

    public String getMotherLastName() {
        return motherLastName;
    }

    public void setMotherLastName(String motherLastName) {
        this.motherLastName = motherLastName;
    }

    public Long getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(Long aadharNo) {
        this.aadharNo = aadharNo;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Caste getCaste() {
        return caste;
    }

    public void setCaste(Caste caste) {
        this.caste = caste;
    }

    public String getSubCaste() {
        return subCaste;
    }

    public void setSubCaste(String subCaste) {
        this.subCaste = subCaste;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    public Bloodgroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(Bloodgroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public String getAddressLineThree() {
        return addressLineThree;
    }

    public void setAddressLineThree(String addressLineThree) {
        this.addressLineThree = addressLineThree;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getPincode() {
        return pincode;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public String getTeacherContactNumber() {
        return teacherContactNumber;
    }

    public void setTeacherContactNumber(String teacherContactNumber) {
        this.teacherContactNumber = teacherContactNumber;
    }

    public String getAlternateContactNumber() {
        return alternateContactNumber;
    }

    public void setAlternateContactNumber(String alternateContactNumber) {
        this.alternateContactNumber = alternateContactNumber;
    }

    public String getTeacherEmailAddress() {
        return teacherEmailAddress;
    }

    public void setTeacherEmailAddress(String teacherEmailAddress) {
        this.teacherEmailAddress = teacherEmailAddress;
    }

    public String getAlternateEmailAddress() {
        return alternateEmailAddress;
    }

    public void setAlternateEmailAddress(String alternateEmailAddress) {
        this.alternateEmailAddress = alternateEmailAddress;
    }

    public RelationWithStudentEnum getRelationWithStaff() {
        return relationWithStaff;
    }

    public void setRelationWithStaff(RelationWithStudentEnum relationWithStaff) {
        this.relationWithStaff = relationWithStaff;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactMiddleName() {
        return emergencyContactMiddleName;
    }

    public void setEmergencyContactMiddleName(String emergencyContactMiddleName) {
        this.emergencyContactMiddleName = emergencyContactMiddleName;
    }

    public String getEmergencyContactLastName() {
        return emergencyContactLastName;
    }

    public void setEmergencyContactLastName(String emergencyContactLastName) {
        this.emergencyContactLastName = emergencyContactLastName;
    }

    public String getEmergencyContactNo() {
        return emergencyContactNo;
    }

    public void setEmergencyContactNo(String emergencyContactNo) {
        this.emergencyContactNo = emergencyContactNo;
    }

    public String getEmergencyContactEmailAddress() {
        return emergencyContactEmailAddress;
    }

    public void setEmergencyContactEmailAddress(String emergencyContactEmailAddress) {
        this.emergencyContactEmailAddress = emergencyContactEmailAddress;
    }

    public String getUploadPhoto() {
        return uploadPhoto;
    }

    public void setUploadPhoto(String uploadPhoto) {
        this.uploadPhoto = uploadPhoto;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public StaffType getStaffType() {
        return staffType;
    }

    public void setStaffType(StaffType staffType) {
        this.staffType = staffType;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeacherDTO teacherDTO = (TeacherDTO) o;
        if (teacherDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teacherDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TeacherDTO{" +
            "id=" + getId() +
            ", teacherName='" + getTeacherName() + "'" +
            ", teacherMiddleName='" + getTeacherMiddleName() + "'" +
            ", teacherLastName='" + getTeacherLastName() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", fatherMiddleName='" + getFatherMiddleName() + "'" +
            ", fatherLastName='" + getFatherLastName() + "'" +
            ", spouseName='" + getSpouseName() + "'" +
            ", spouseMiddleName='" + getSpouseMiddleName() + "'" +
            ", spouseLastName='" + getSpouseLastName() + "'" +
            ", motherName='" + getMotherName() + "'" +
            ", motherMiddleName='" + getMotherMiddleName() + "'" +
            ", motherLastName='" + getMotherLastName() + "'" +
            ", aadharNo=" + getAadharNo() +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", religion='" + getReligion() + "'" +
            ", caste='" + getCaste() + "'" +
            ", subCaste='" + getSubCaste() + "'" +
            ", age=" + getAge() +
            ", sex='" + getSex() + "'" +
            ", bloodGroup='" + getBloodGroup() + "'" +
            ", addressLineOne='" + getAddressLineOne() + "'" +
            ", addressLineTwo='" + getAddressLineTwo() + "'" +
            ", addressLineThree='" + getAddressLineThree() + "'" +
            ", town='" + getTown() + "'" +
            ", state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            ", pincode=" + getPincode() +
            ", teacherContactNumber='" + getTeacherContactNumber() + "'" +
            ", alternateContactNumber='" + getAlternateContactNumber() + "'" +
            ", teacherEmailAddress='" + getTeacherEmailAddress() + "'" +
            ", alternateEmailAddress='" + getAlternateEmailAddress() + "'" +
            ", relationWithStaff='" + getRelationWithStaff() + "'" +
            ", emergencyContactName='" + getEmergencyContactName() + "'" +
            ", emergencyContactMiddleName='" + getEmergencyContactMiddleName() + "'" +
            ", emergencyContactLastName='" + getEmergencyContactLastName() + "'" +
            ", emergencyContactNo='" + getEmergencyContactNo() + "'" +
            ", emergencyContactEmailAddress='" + getEmergencyContactEmailAddress() + "'" +
            ", uploadPhoto='" + getUploadPhoto() + "'" +
            ", status='" + getStatus() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", designation='" + getDesignation() + "'" +
            ", staffType='" + getStaffType() + "'" +
            ", department=" + getDepartmentId() +
            ", branch=" + getBranchId() +
            "}";
    }
}
