package com.amolik.springdata.application;

import com.amolik.springdata.entity.Fiscal;
import com.amolik.springdata.service.FiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration


public class FiscalApplication {

    @Autowired
    private FiscalService fiscalService;

    public static void main(String[] args) {

        SpringApplication.run(FiscalApplication.class, args);

        Fiscal fiscal = new Fiscal();
        fiscal.setAddress("A");
        fiscal.setBasicSalary("A");
        fiscal.setCenterName("A");
        fiscal.setCity("A");
        fiscal.setContactMode("A");
        fiscal.setCountry("A");
        fiscal.setDepartment("D");
        fiscal.setDesignation("A");
        fiscal.setEmi("A");
        fiscal.setEmpIdNo("A");
        fiscal.setEmpName("A");
        fiscal.setHealthId("A");
        fiscal.setEmpName("A");
        fiscal.setHealthId("A");
        fiscal.setHealthInsuranceProvider("A");
        fiscal.setImageFileName("A");
        fiscal.setInitials("A");
        fiscal.setIssuerBank("A");
        fiscal.setLoanAmount("A");
        fiscal.setMaritalStatus("A");
        fiscal.setOccuranceNo("A");
        fiscal.setOtherLoans("A");
        fiscal.setPerformance("A");
        fiscal.setRateOfInterest("A");
        fiscal.setRefName("A");
        fiscal.setSrNo("A");
        fiscal.setState("A");
        fiscal.setTenure("A");
        fiscal.setTotalLoan("A");
        fiscal.setYearsOfEmployment("A");
        fiscal.setZip("A");

    }
}
