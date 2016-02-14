package com.amolik.springdata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="fiscaltest")
@NamedQuery(name = "Fiscal.findByTheFiscalImageFileName", query = "from Fiscal u where u.imageFileName = ?1")
public class Fiscal {

    @Column(nullable = false, name = "imageFileName")
    private String imageFileName;

    @Column(nullable = false, name = "srNo")
    private String srNo;

    @Column(nullable = false, name = "empIdNo")
    private String empIdNo;

    @Column(nullable = false, name = "occuranceNo")
    private String occuranceNo;

    @Column(nullable = false, name = "loanFileNo")
    private String loanFileNo;

    @Column(nullable = false, name = "loanAmount")
    private String loanAmount;

    @Column(nullable = false, name = "rateOfInterest")
    private String rateOfInterest;

    @Column(nullable = false, name = "tenure")
    private String tenure;

    @Column(nullable = false, name = "totalLoan")
    private String totalLoan;

    @Column(nullable = false, name = "emi")
    private String emi;

    @Column(nullable = false, name = "otherLoans")
    private String otherLoans;

    @Column(nullable = false, name = "initials")
    private String initials;

    @Column(nullable = false, name = "empName")
    private String empName;

    @Column(nullable = false, name = "address")
    private String address;

    @Column(nullable = false, name = "city")
    private String city;

    @Column(nullable = false, name = "state")
    private String state;

    @Column(nullable = false, name = "zip")
    private String zip;

    @Column(nullable = false, name = "country")
    private String country;

    @Column(nullable = false, name = "contactMode")
    private String contactMode;

    @Column(nullable = false, name = "maritalStatus")
    private String maritalStatus;

    @Column(nullable = false, name = "refName")
    private String refName;

    @Column(nullable = false, name = "yearsOfEmployment")
    private String yearsOfEmployment;

    @Column(nullable = false, name = "designation")
    private String designation;

    @Column(nullable = false, name = "department")
    private String department;

    @Column(nullable = false, name = "performance")
    private String performance;

    @Column(nullable = false, name = "basicSalary")
    private String basicSalary;

    @Column(nullable = false, name = "centerName")
    private String centerName;

    @Column(nullable = false, name = "issuerBank")
    private String issuerBank;

    @Column(nullable = false, name = "healthId")
    private String healthId;

    @Column(nullable = false, name = "healthInsuranceProvider")
    private String healthInsuranceProvider;


    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getEmpIdNo() {
        return empIdNo;
    }

    public void setEmpIdNo(String empIdNo) {
        this.empIdNo = empIdNo;
    }

    public String getOccuranceNo() {
        return occuranceNo;
    }

    public void setOccuranceNo(String occuranceNo) {
        this.occuranceNo = occuranceNo;
    }

    public String getLoanFileNo() {
        return loanFileNo;
    }

    public void setLoanFileNo(String loanFileNo) {
        this.loanFileNo = loanFileNo;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getRateOfInterest() {
        return rateOfInterest;
    }

    public void setRateOfInterest(String rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public String getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(String totalLoan) {
        this.totalLoan = totalLoan;
    }

    public String getEmi() {
        return emi;
    }

    public void setEmi(String emi) {
        this.emi = emi;
    }

    public String getOtherLoans() {
        return otherLoans;
    }

    public void setOtherLoans(String otherLoans) {
        this.otherLoans = otherLoans;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactMode() {
        return contactMode;
    }

    public void setContactMode(String contactMode) {
        this.contactMode = contactMode;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getYearsOfEmployment() {
        return yearsOfEmployment;
    }

    public void setYearsOfEmployment(String yearsOfEmployment) {
        this.yearsOfEmployment = yearsOfEmployment;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(String basicSalary) {
        this.basicSalary = basicSalary;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getIssuerBank() {
        return issuerBank;
    }

    public void setIssuerBank(String issuerBank) {
        this.issuerBank = issuerBank;
    }

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public String getHealthInsuranceProvider() {
        return healthInsuranceProvider;
    }

    public void setHealthInsuranceProvider(String healthInsuranceProvider) {
        this.healthInsuranceProvider = healthInsuranceProvider;
    }

    @Override
    public String toString() {
        return "Fiscal{" +
                "imageFileName='" + imageFileName + '\'' +
                ", srNo='" + srNo + '\'' +
                ", empIdNo='" + empIdNo + '\'' +
                ", occuranceNo='" + occuranceNo + '\'' +
                ", loanFileNo='" + loanFileNo + '\'' +
                ", loanAmount='" + loanAmount + '\'' +
                ", rateOfInterest='" + rateOfInterest + '\'' +
                ", tenure='" + tenure + '\'' +
                ", totalLoan='" + totalLoan + '\'' +
                ", emi='" + emi + '\'' +
                ", otherLoans='" + otherLoans + '\'' +
                ", initials='" + initials + '\'' +
                ", empName='" + empName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", contactMode='" + contactMode + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", refName='" + refName + '\'' +
                ", yearsOfEmployment='" + yearsOfEmployment + '\'' +
                ", designation='" + designation + '\'' +
                ", department='" + department + '\'' +
                ", performance='" + performance + '\'' +
                ", basicSalary='" + basicSalary + '\'' +
                ", centerName='" + centerName + '\'' +
                ", issuerBank='" + issuerBank + '\'' +
                ", healthId='" + healthId + '\'' +
                ", healthInsuranceProvider='" + healthInsuranceProvider + '\'' +
                '}';
    }
}
