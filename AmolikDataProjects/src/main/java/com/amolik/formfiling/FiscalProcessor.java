package com.amolik.formfiling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.amolik.data.FiscalRecord;
import com.amolik.scrapers.OdishaRationCardScraper;
import com.amolik.util.*;


public class FiscalProcessor {




	Pattern oneOrMoreSpacePattern = Pattern.compile(Constants.ONE_OR_MORE_SPACE_STRING);

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FiscalProcessor.class);

	/**
	 *  Initializes property file.
	 *  
	 *  
	 */

	Pattern spaceSlashSpacePattern = Pattern.compile(Constants.SPACE_FORWARD_SLASH_SPACE);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String fileLoc=AmolikProperties.getProperty("fiscalprocessor.inputFileDir");
		String fileSeparator=System.getProperty("file.separator");
		String fileName=AmolikProperties.getProperty("fiscalprocessor.inputfileName");
		String fullFileName=fileLoc+fileSeparator +fileName;


		if(fileLoc==null){

			System.out.println("amolik.properties file not found, shutting down system");
			System.exit(1);
		}


		long startTime=System.currentTimeMillis();
		FiscalProcessor fiscalProcessor = new FiscalProcessor();

		fiscalProcessor.processDataFile(fullFileName,fileName); 

		long endTime=System.currentTimeMillis();
		long timeTaken=endTime-startTime;

		if (logger.isInfoEnabled()) {

			logger.info("time taken in miliseconds=" 
					+ new Long(timeTaken).toString()); //$NON-NLS-1$
		}

	}


	public void processDataFile(String fullFileName,String fileName) {

		int[] lineNum = { 0 };

		List recordList = new ArrayList();
		//String[][] record = new String[1][29];
		FiscalRecord fiscalRecord = new FiscalRecord();

		try (Stream<String> stream = Files.lines(Paths.get(fullFileName))) {

			String baseFile= StringUtility.getFileNameBeforeUnderScore(
					fileName)+"img";

			int[] baseRecordCount = {StringUtility.getLastRecordFromFileName(fileName, 200)};

			stream.forEach((line)->{
				lineNum[0]++;
				String imgFileName = baseFile+(baseRecordCount[0]+recordList.size()+1)+".jpeg";
				fiscalRecord.setImageFileName(imgFileName);
				switch(lineNum[0]){

				case 1:
					//					if(logger.isInfoEnabled()){
					//
					//						logger.info("Processing record="+(recordList.size()+1)
					//								+"|file="+imgFileName);
					//					}

					setFiscalRecordFromLine1(line,fiscalRecord);
					break;
				case 2:

					setFiscalRecordFromLine2(line,fiscalRecord);
					break;
				case 3:
					setFiscalRecordFromLine3(line,fiscalRecord);
					break;
				case 4:

					setFiscalRecordFromLine4(line,fiscalRecord);
					break;
				case 5:
					setFiscalRecordFromLine5(line,fiscalRecord);


					break;
				case 6:
					setFiscalRecordFromLine6(line,fiscalRecord);

					break;
				case 7:
					fiscalRecord.setIssuerBank(StringUtility.trim(line));
					if(logger.isDebugEnabled()) {

						logger.debug(fiscalRecord.getIssuerBank());
					}

					fixNumericFields(fiscalRecord);
					fixYearsOfEmployment(fiscalRecord);
					printFiscalRecord(fiscalRecord);
					// Add to list 
					recordList.add(fiscalRecord);

					// Reset to new record
					lineNum[0]=0;


					if(logger.isDebugEnabled()) {

						logger.debug("setting line num to zero");
					}
					break;
				}
			});
			stream.close();
		} catch (IOException ex) {
			// do something with exception
			logger.error("main(String[])", ex); //$NON-NLS-1$
		}
	}




	public  void setFiscalRecordFromLine1(String firstLine,FiscalRecord record){

		if (logger.isDebugEnabled()) {

			logger.debug(firstLine); //$NON-NLS-1$
		}
		StringBuffer printString = new StringBuffer();

		// Check to see if special character for beginning of record
		Matcher m = Pattern.compile("[\\W]").matcher(firstLine);


		if(m.find(0)){

			firstLine=firstLine.substring(1);
		}

		int lastIndexOfPercentage = setMonthsFieldAndGetIndex(firstLine, record);

		if(lastIndexOfPercentage >1) {

			firstLine= firstLine.substring(0, lastIndexOfPercentage+1);
		}

		else {

			logger.error("% symbol not found in line|"+firstLine);
		}


		int dollarIndex = firstLine.lastIndexOf("$");

		String[] amountPercentArray = oneOrMoreSpacePattern.split(firstLine.substring(dollarIndex));

		if(logger.isDebugEnabled()){

			logger.debug("dollarSubstring="+firstLine.substring(dollarIndex));
		}

		if(amountPercentArray.length==2){

			record.setLoanAmount(amountPercentArray[0]);
			record.setRateOfInterest(amountPercentArray[1]);
			firstLine = firstLine.substring(0,dollarIndex);
			if(logger.isDebugEnabled()){

				logger.debug("recordLoanAmount="+record.getLoanAmount()
				+ "|RateOfInterest="+record.getRateOfInterest()
				+"|firstLine="+firstLine);
			}
		}


		String[] splited = oneOrMoreSpacePattern.split(firstLine);


		StringBuffer loanFileNoBuffer = new StringBuffer();
		for (int i=0;i<splited.length;i++) {


			switch (i+1){

			case 1: record.setSrNo(StringUtility.trim(splited[i]));
			break;
			case 2:record.setEmpIdNo(StringUtility.trim(splited[i]));
			break;
			case 3:record.setOccuranceNo(StringUtility.trim(splited[i]));
			break;

			}
		}	



		int startIndexOfOccuranceNo = firstLine.lastIndexOf(record.getOccuranceNo());
		int endIndexOfOccuranceNo=startIndexOfOccuranceNo+record.getOccuranceNo().length();
		String loanFileNo = firstLine.substring(endIndexOfOccuranceNo+1);

		if(logger.isDebugEnabled()){

			logger.debug("loanFileNo="+loanFileNo);
		}
		if(loanFileNo!=null
				&&!loanFileNo.equals(Constants.EMPTY_STRING)){

			Matcher m1 = spaceSlashSpacePattern.matcher(loanFileNo);

			if(!m1.matches()) {

				loanFileNo = loanFileNo.replaceAll(Constants.ONE_OR_MORE_SPACE_STRING,
						Constants.EMPTY_STRING);

				//loanFileNo = loanFileNo.replace(Constants.FORWARD_SLASH, 
				loanFileNo = loanFileNo.replaceAll("[^A-Za-z0-9]", 
						Constants.SPACE_FORWARD_SLASH_SPACE);
			}
		}
		record.setLoanFileNo(loanFileNo);

		if(logger.isDebugEnabled()){

			logger.debug(record.getSrNo()
					+Constants.DOUBLE_PIPE+record.getEmpIdNo()
					+Constants.DOUBLE_PIPE+record.getOccuranceNo()
					+Constants.DOUBLE_PIPE+record.getLoanFileNo()
					+Constants.DOUBLE_PIPE+record.getLoanAmount()
					+Constants.DOUBLE_PIPE+record.getRateOfInterest()
					+Constants.DOUBLE_PIPE+record.getTenure()
					);
		}

	}


	public  int setMonthsFieldAndGetIndex(String firstLine, FiscalRecord record) {

		int lastIndexOfPercentage = firstLine.lastIndexOf("%");

		if(lastIndexOfPercentage>0){

			String tenure=firstLine.substring((lastIndexOfPercentage+1));
			record.setTenure(tenure.trim());
			firstLine = firstLine.substring(0, lastIndexOfPercentage);

			if(logger.isDebugEnabled()){

				logger.debug("7th field="+tenure.trim());
			}
		}

		return lastIndexOfPercentage;
	}



	public  void setFiscalRecordFromLine2(String secondLine,FiscalRecord record){

		if (logger.isDebugEnabled()) {

			logger.debug(secondLine); //$NON-NLS-1$
		}


		String[] splited = oneOrMoreSpacePattern.split(secondLine);

		tillSetInitials :
			for (int i=0;i<splited.length;i++) {

				switch (i+1){

				case 1: record.setTotalLoan(StringUtility.trim(splited[i]));
				break;
				case 2: record.setEmi(StringUtility.trim(splited[i]));
				break;
				case 3: record.setOtherLoans(StringUtility.trim(splited[i]));
				break;
				case 4: record.setInitials(StringUtility.trim(splited[i]));
				break tillSetInitials;

				}
			}
		/* first 3 record are found; last record( person name) is found by adding 
		 * remaining array elements
		 * 
		 */

		int startIndexOfOccuranceNo = secondLine.lastIndexOf(record.getInitials());
		int endIndexOfOccuranceNo=startIndexOfOccuranceNo+record.getInitials().length();
		record.setEmpName(StringUtility.trim(
				secondLine.substring(endIndexOfOccuranceNo+1)));

		if(logger.isDebugEnabled()){

			logger.debug(record.getTotalLoan()
					+Constants.DOUBLE_PIPE+record.getEmi()
					+Constants.DOUBLE_PIPE+record.getOtherLoans()
					+Constants.DOUBLE_PIPE+record.getInitials()
					+Constants.DOUBLE_PIPE+record.getEmpName()
					);
		}

	}


	public  void setFiscalRecordFromLine3(String thirdLine,FiscalRecord record){

		if (logger.isDebugEnabled()) {

			logger.debug(thirdLine); //$NON-NLS-1$
		}

		String lastZipCodeNum = thirdLine.replaceAll("[^0-9]*$", "").replaceAll(".(?!$)", "");
		int indexOfEndOfZipCode = thirdLine.lastIndexOf(lastZipCodeNum);
		record.setCountry(StringUtility.trim(
				thirdLine.substring(indexOfEndOfZipCode+1)));


		thirdLine = thirdLine.substring(0, indexOfEndOfZipCode+1);

		String[] splited = oneOrMoreSpacePattern.split(thirdLine);

		if(logger.isDebugEnabled()){

			logger.debug("line before getting zipcode ="+thirdLine );
		}


		// Assume 2nd last field is zipcode
		int zipCodeIdx=splited.length-1;
		record.setZip(StringUtility.trim(splited[zipCodeIdx]));

		//check if zipcode is numeric
		//if(record.getZip().matches("\\d*")){


		record.setState(StringUtility.trim(splited[zipCodeIdx-1]));
		int startIndexOfState = thirdLine.lastIndexOf(record.getState());
		thirdLine = thirdLine.substring(0,startIndexOfState).trim();		
		//}
		int spacesInString = thirdLine.length() - thirdLine.replace(" ", "").length();
		if(logger.isDebugEnabled()){

			logger.debug("line After setting state and zip="+thirdLine);
		}
		splited = oneOrMoreSpacePattern.split(thirdLine);


		String validSuffix=null;
		// Iterate in reverse and set address and city
		;
		int suffixIndex = 0;
		findValidSuffix:
			for(int j=splited.length-2;j>=0;j--){

				// check if valid address suffix
				if(AddressSuffix.isValidSuffix(splited[j])){


					validSuffix=splited[j];
					suffixIndex=j;
					if(logger.isDebugEnabled()){

						logger.debug("valid suffix="+validSuffix);
					}
					break findValidSuffix;
				}
			}


		if(validSuffix!=null){

			StringBuffer cityBuffer= new StringBuffer();

			for(int j=suffixIndex+1;j<splited.length;j++){

				cityBuffer.append(splited[j]+Constants.SPACE);
			}

			String city = StringUtility.trim(cityBuffer.toString());
			record.setCity(city);

			int startIndexOfCity = thirdLine.lastIndexOf(city);


			record.setAddress(StringUtility.trim(
					thirdLine.substring(0,startIndexOfCity)));



			if(logger.isDebugEnabled()){

				logger.debug("Address="+record.getAddress()+"|city="+record.getCity()
				+"|line="+thirdLine);
			}
		}
		// handle valid suffix not found 
		else {

			logger.error("valid suffix not found|"+thirdLine);
		}

		if (logger.isDebugEnabled()){

			logger.debug(record.getAddress()
					+Constants.DOUBLE_PIPE+record.getCity()
					+Constants.DOUBLE_PIPE+record.getState()
					+Constants.DOUBLE_PIPE+record.getZip()
					+Constants.DOUBLE_PIPE+record.getCountry()
					);
		}
	}

	public  void setFiscalRecordFromLine4(String fourthLine,FiscalRecord record){


		int yearsIndex=0;
		if (logger.isDebugEnabled()) {

			logger.debug(fourthLine); //$NON-NLS-1$
		}

		String[] splited = oneOrMoreSpacePattern.split(fourthLine,3);


		for (int i=0;i<splited.length;i++){

			switch (i+1){

			case 1:
				record.setContactMode(StringUtility.trim(splited[i]));
				break;

			case 2:
				record.setMaritalStatus(StringUtility.trim(splited[i]));
				break;	

			case 3:

				fourthLine = StringUtility.trim(splited[i]);
				break;

			}
		}

		String yearsContainingString = "";
		String yearsAfterString="";
		splited = fourthLine.split(Constants.MONTHS_OR_YEAR_PATTERN_STRING);
		for (int i=0;i<splited.length;i++){

			switch (i+1){

			case 1:
				yearsContainingString=StringUtility.trim(splited[i]);
				break;

			case 2:
				yearsAfterString = StringUtility.trim(splited[i]);
				break;	

			}
		}	

		splited = oneOrMoreSpacePattern.split(yearsAfterString);

		for (int i=0;i<splited.length;i++){

			switch (i+1){

			case 1:

				record.setDesignation(StringUtility.trim(splited[i]));

				break;

			case 2:
				if(splited.length==2){

					record.setDepartment(StringUtility.trim(splited[i]));
				}
				// check if second word is not 
				else if(splited.length>=3) {

					// if true that means it contains position field
					if(splited[i]!=null && !splited[i].trim().equalsIgnoreCase("NOT")){

						record.setDesignation(
								StringUtility.trim(splited[i-1]+Constants.SPACE+splited[i]));
					} 
					else {
						record.setDepartment(
								StringUtility.trim(splited[i]+Constants.SPACE+splited[i+1]));
					}
				}
				break;	

			case 3:
				if(splited.length==3){
					if(splited[i]!=null && !splited[i].trim().equalsIgnoreCase("AVAILABLE")){

						record.setDepartment(
								StringUtility.trim(splited[i])); 

					}
				}
				break;

			case 4:

				record.setDepartment(
						StringUtility.trim(splited[i-1]+Constants.SPACE+splited[i])); 

				break;

			}
		}


		yearsContainingString = StringUtility.trim(
				fourthLine.substring(0,fourthLine.indexOf(yearsAfterString))
				);

		splited = oneOrMoreSpacePattern.split(yearsContainingString);


		if(splited.length>2){

			record.setYearsOfEmployment(
					splited[splited.length-2]+Constants.SPACE+splited[splited.length-1]);

		}
		StringBuffer refNameBuffer= new StringBuffer();
		for(int i=0;i<splited.length-2;i++){

			refNameBuffer.append(StringUtility.trim(splited[i]));
		}

		record.setRefName(refNameBuffer.toString());

		if (logger.isDebugEnabled()){

			logger.debug(record.getContactMode()
					+Constants.DOUBLE_PIPE+record.getMaritalStatus()
					+Constants.DOUBLE_PIPE+record.getRefName()
					+Constants.DOUBLE_PIPE+record.getYearsOfEmployment()
					+Constants.DOUBLE_PIPE+record.getDesignation()
					+Constants.DOUBLE_PIPE+record.getDepartment()
					);
		}

	}

	public  void setFiscalRecordFromLine5(String fifthLine,FiscalRecord record){


		if(logger.isDebugEnabled()) {

			logger.debug(fifthLine);
		}

		String[] splited = oneOrMoreSpacePattern.split(fifthLine,3);


		basicSalary:
			for (int i=0;i<splited.length;i++){

				switch (i+1){

				case 1:
					record.setPerformance(StringUtility.trim(splited[i]));
					break;

				case 2:
					record.setBasicSalary(StringUtility.trim(splited[i]));
					break ;

				case 3:
					record.setCenterName(StringUtility.trim(splited[i]));
					break ;
				}
			}

		if(logger.isDebugEnabled()){

			logger.debug(record.getPerformance()
					+Constants.DOUBLE_PIPE+record.getBasicSalary()
					+Constants.DOUBLE_PIPE+record.getCenterName()
					);
		}

	}


	public void setFiscalRecordFromLine6(String sixthLine,FiscalRecord record){


		if (logger.isDebugEnabled()) {

			logger.debug(sixthLine); //$NON-NLS-1$
		}


		String[] splited = oneOrMoreSpacePattern.split(sixthLine,2);

		forLoop:
			for(int j=0;j<splited.length;j++){

				switch (j+1){

				case 1: record.setHealthId(StringUtility.trim(splited[j]));
				break;
				case 2:
					record.setHealthInsuranceProvider(StringUtility.trim(splited[j]));
					break forLoop;
				}
			}

		if(logger.isDebugEnabled()) {

			logger.debug(record.getHealthId()
					+Constants.DOUBLE_PIPE+record.getHealthInsuranceProvider());
		}
	}

	// replace ocr mistakes
	public void fixNumericFields(FiscalRecord record){


		record.setBasicSalary(fixNonNumericCharInNumericField(record.getBasicSalary()));
		record.setEmi(fixNonNumericCharInNumericField(record.getEmi()));
		record.setLoanAmount(fixNonNumericCharInNumericField(record.getLoanAmount()));
		record.setOccuranceNo(fixNonNumericCharInNumericField(record.getOccuranceNo()));
		record.setRateOfInterest(fixNonNumericCharInNumericField(record.getRateOfInterest()));
		record.setTotalLoan(fixNonNumericCharInNumericField(record.getTotalLoan()));
		record.setZip(fixNonNumericCharInNumericField(record.getZip()));

	}


	public String fixNonNumericCharInNumericField(String field){

			field= StringUtility.getDeAccentedString(field);
			field=field.replaceAll("[I,i,L,l]", "1")
					.replaceAll("[O,o]", "0")
					.replaceAll("[S,s]", "5")
					.replaceAll("[Z,z]", "2")
					;
		

		return field;
	}

	public void fixYearsOfEmployment(FiscalRecord record) {

		record.setYearsOfEmployment(
				record.getYearsOfEmployment()
				.replaceAll("[L,l]|[I,i]", "1")
				);
	}

	public void fixNonNumericFields(FiscalRecord record){


		record.setOtherLoans(fixNumericInNonNumericField(record.getOtherLoans()));
		record.setInitials(fixNumericInNonNumericField(record.getInitials()));
		record.setEmpName(fixNumericInNonNumericField(record.getEmpName()));
		record.setCity(fixNumericInNonNumericField(record.getCity()));
		record.setState(fixNumericInNonNumericField(record.getState()));
		record.setCountry(fixNumericInNonNumericField(record.getCountry()));
		record.setContactMode(fixNumericInNonNumericField(record.getContactMode()));
		record.setMaritalStatus(fixNumericInNonNumericField(record.getMaritalStatus()));
		record.setRefName(fixNumericInNonNumericField(record.getRefName()));
		record.setDesignation(fixNumericInNonNumericField(record.getDesignation()));
		record.setDepartment(fixNumericInNonNumericField(record.getDepartment()));
		record.setRefName(fixNumericInNonNumericField(record.getRefName()));
		record.setIssuerBank(fixNumericInNonNumericField(record.getIssuerBank()));

	}

	public String fixNumericInNonNumericField(String field){


		if(field!=null && field.trim().equals(Constants.EMPTY_STRING)) {

			field=field.replaceAll("[2]", "Z")
					.replaceAll("[0]", "O")
					.replaceAll("[5]", "S")
					.replaceAll("[1]","l")
					;
		}

		return field;
	}

	public void printFiscalRecord(FiscalRecord record){

		if(logger.isInfoEnabled()){

			logger.info(record.getSrNo()
					+Constants.DOUBLE_PIPE+record.getEmpIdNo()
					+Constants.DOUBLE_PIPE+record.getOccuranceNo()
					+Constants.DOUBLE_PIPE+record.getLoanFileNo()
					+Constants.DOUBLE_PIPE+record.getLoanAmount()
					+Constants.DOUBLE_PIPE+record.getRateOfInterest()
					+Constants.DOUBLE_PIPE+record.getTenure()
					);
			logger.info(record.getTotalLoan()
					+Constants.DOUBLE_PIPE+record.getEmi()
					+Constants.DOUBLE_PIPE+record.getOtherLoans()
					+Constants.DOUBLE_PIPE+record.getInitials()
					+Constants.DOUBLE_PIPE+record.getEmpName()
					);

			logger.info(record.getAddress()
					+Constants.DOUBLE_PIPE+record.getCity()
					+Constants.DOUBLE_PIPE+record.getState()
					+Constants.DOUBLE_PIPE+record.getZip()
					+Constants.DOUBLE_PIPE+record.getCountry()
					);

			logger.info(record.getContactMode()
					+Constants.DOUBLE_PIPE+record.getMaritalStatus()
					+Constants.DOUBLE_PIPE+record.getRefName()
					+Constants.DOUBLE_PIPE+record.getYearsOfEmployment()
					+Constants.DOUBLE_PIPE+record.getDesignation()
					+Constants.DOUBLE_PIPE+record.getDepartment()
					);

			logger.info(record.getPerformance()
					+Constants.DOUBLE_PIPE+record.getBasicSalary()
					+Constants.DOUBLE_PIPE+record.getCenterName()
					+Constants.DOUBLE_PIPE+record.getIssuerBank()
					);

			logger.info(record.getHealthId()
					+Constants.DOUBLE_PIPE+record.getHealthInsuranceProvider());
		}
	}
}

