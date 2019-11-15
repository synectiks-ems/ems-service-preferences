package com.synectiks.pref.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.domain.College;

/**
 * A Vo for the BankAccounts entity.
 */
public class CmsBankAccountsVo implements Serializable {

    private Long id;
    private String nameOfBank;
    private String accountNumber;
    private String typeOfAccount;
    private String ifscCode;
    private String branchAddress;
    private String corporateId;
    private Long branchId;
    private Long collegeId;
    private Branch branch;
    private College college;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameOfBank() {
        return nameOfBank;
    }

    public void setNameOfBank(String nameOfBank) {
        this.nameOfBank = nameOfBank;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(String corporateId) {
        this.corporateId = corporateId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CmsBankAccountsVo bankAccountsDTO = (CmsBankAccountsVo) o;
        if (bankAccountsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bankAccountsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BankAccountsDTO{" +
            "id=" + getId() +
            ", nameOfBank='" + getNameOfBank() + "'" +
            ", accountNumber=" + getAccountNumber() +
            ", typeOfAccount='" + getTypeOfAccount() + "'" +
            ", ifscCode='" + getIfscCode() + "'" +
            ", branchAddress='" + getBranchAddress() + "'" +
            ", corporateId=" + getCorporateId() +
            ", branch=" + getBranchId() +
            ", college=" + getCollegeId() +
            "}";
    }

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public College getCollege() {
		return college;
	}

	public void setCollege(College college) {
		this.college = college;
	}
}
