package com.amolik.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.amolik.data.FiscalCorrectionField;
import com.amolik.data.FiscalRecord;
import com.amolik.formfiling.FiscalProcessor;

public class FiscalExcelToTextFileConverter {

	private static final Logger logger = Logger.getLogger(FiscalExcelToTextFileConverter.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if(logger.isInfoEnabled()) {

			logger.info("Starting excel conversion");
		}

		String excelDataFilePath = "E:\\FISCAL_DATA\\database-rupesh-system5.xls";
		String outputFilePath ="E:\\FISCAL_DATA\\database-rupesh-system5-fullcorrect.txt";
		String excelDestFileName     = "E:\\FISCAL_DATA\\database-rupesh-system5-fullcorrect.xls";
		String excelCorrectionFilePath = "E:\\FISCAL_DATA\\FPDATA1106.xls";
		
		Map<String,FiscalRecord> fiscalRecordsMap =
				ExcelUtil.getFiscalRecordsFromExcel(excelDataFilePath);

		if(logger.isInfoEnabled()) {

			logger.info("Finish reading excel data file");
		}

		List<FiscalCorrectionField> correctionList = 
				ExcelUtil.getCorrectFiscalFieldsFromExcel(excelCorrectionFilePath);

		if(logger.isInfoEnabled()) {

			logger.info("Finish reading excel correction file");
		}

		OverwriteFiscalRecordWithCorrection(fiscalRecordsMap,correctionList);

		if(logger.isInfoEnabled()) {

			logger.info("Finish overwriting data file with correction file");
		}

		List<FiscalRecord> recordsList = new ArrayList<FiscalRecord>(fiscalRecordsMap.values());
		
		String outputFileDelimiter = "||";
		//FileUtility.writeDelimitedRecordsToFile(outputFilePath, outputFileDelimiter, recordsList);
		String excelTemplateFileName = "E:\\FISCAL_DATA\\fiscal_compare_template.xls";
		
		
		ExcelUtil.generateExcelFromBean(recordsList, 
				excelTemplateFileName, null, excelDestFileName, "fiscal");
	}

	public static void OverwriteFiscalRecordWithCorrection(
			Map<String,FiscalRecord> fiscalRecordsMap,List<FiscalCorrectionField> correctionList){


		for(FiscalCorrectionField correctionField: correctionList){

			String correctValue = correctionField.getCorrectFieldValue();
			String imageName = correctionField.getImageName();
			FiscalRecord fiscalRecord = fiscalRecordsMap.get(imageName);



			if(correctValue!=null && !correctValue.equals("")) {

				if(logger.isDebugEnabled()) {

					logger.debug("imageName="+imageName
							+"|correctionField="+correctionField.getFieldName()
							+"|correctValue="+correctValue
							);
				}


				switch(correctionField.getFieldName()){

				case "Address 1"      : fiscalRecord.setAddress(correctValue); break;
				case "Basic Salary"   : fiscalRecord.setBasicSalary(correctValue); break;
				case "Center Name"    : fiscalRecord.setCenterName(correctValue); break;
				case "City 1"         : fiscalRecord.setCity(correctValue); break;
				case "Contact Mode"   : fiscalRecord.setContactMode(correctValue); break;
				case "country 1"      : fiscalRecord.setCountry(correctValue); break;
				case "Department"     : fiscalRecord.setDepartment(correctValue);break;
				case "Designation"    : fiscalRecord.setDesignation(correctValue);break;
				case "EMI"            : fiscalRecord.setEmi(correctValue);break;
				case "EMP Name"       : fiscalRecord.setEmpName(correctValue);break;
				case "ID No"          : fiscalRecord.setEmpIdNo(correctValue); break;
				case "Initials"       : fiscalRecord.setInitials(correctValue); break;
				case "Interest"       : fiscalRecord.setRateOfInterest(correctValue); break;
				case "Issuer Bank"    : fiscalRecord.setIssuerBank(correctValue);break;
				case "Loan Amount"    : fiscalRecord.setLoanAmount(correctValue);break;
				case "Loan File No"   : fiscalRecord.setLoanFileNo(correctValue);break;
				case "MStatus"        : fiscalRecord.setMaritalStatus(correctValue);break;
				case "OcNo"           : fiscalRecord.setOccuranceNo(correctValue);break;
				case "OTH Loan"       : fiscalRecord.setOtherLoans(correctValue);break;
				case "Performance"    : fiscalRecord.setPerformance(correctValue);break;
				case "Ref Name"       : fiscalRecord.setRefName(correctValue);break;
				case "Sr No"          : fiscalRecord.setSrNo(correctValue);break;
				case "State 1"        : fiscalRecord.setState(correctValue);break;
				case "Tenure"         : fiscalRecord.setTenure(correctValue);break;
				case "Total Loan"     : fiscalRecord.setTotalLoan(correctValue);break;
				case "Years of Emp"   : fiscalRecord.setYearsOfEmployment(correctValue);break;
				case "ZIP 1"          : fiscalRecord.setYearsOfEmployment(correctValue);break;
				default               : throw new IllegalArgumentException("Correction field not mapped="
						+correctionField.getFieldName());
				}
			}
		}






	}

}
