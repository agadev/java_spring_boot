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

		//		if (myString.matches("\\p{L}+")) { \\P{Letter}
		//		    return true;
		//		}
		if(fileLoc==null){

			System.out.println("amolik.properties file not found, shutting down system");
			System.exit(1);
		}
		//String NEW_RECORD_START_STRING="\f";

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
		String[][] record = new String[1][29];
		FiscalRecord fiscalRecord = new FiscalRecord();
		int[] fieldIndex = {0};



		try (Stream<String> stream = Files.lines(Paths.get(fullFileName))) {

			String baseFile= StringUtility.getFileNameBeforeUnderScore(fileName)+"img";
			int[] baseRecordCount = {StringUtility.getLastRecordFromFileName(fileName, 200)};

			stream.forEach((line)->{
				lineNum[0]++;
				String imgFileName = baseFile+(baseRecordCount[0]+recordList.size()+1)+".jpeg";
				fiscalRecord.setImageFileName(imgFileName);
				switch(lineNum[0]){

				case 1:
					if(logger.isInfoEnabled()){

						logger.info("Processing record="+(recordList.size()+1)
								+"|file="+imgFileName);
					}

					//setFiscalRecordFromLine1(line,fiscalRecord);
					break;
				case 2:

					//setFiscalRecordFromLine2(line,fiscalRecord);
					break;
				case 3:
					//setFiscalRecordFromLine3(line,fiscalRecord);
					break;
				case 4:
					setRecordFromLine4(line,record[0],fieldIndex[0]);
					setFiscalRecordFromLine4(line,fiscalRecord);
					break;
				case 5:
					//setFiscalRecordFromLine5(line,fiscalRecord);
					//setRecordFromLine5(line,record[0],fieldIndex[0]);
					
					break;
				case 6:
					//setFiscalRecordFromLine6(line,fiscalRecord);
					//setRecordFromLine6(line,record[0],fieldIndex[0]);
					break;
				case 7:
//					fiscalRecord.setIssuerBank(StringUtility.trim(line));
//					if(logger.isInfoEnabled()) {
//						
//						logger.info(fiscalRecord.getIssuerBank());
//					}
					// Add to list 
					recordList.add(record[0]);

					// Reset to new record
					lineNum[0]=0;
					fieldIndex[0]=0;
					record[0] = new String[29];

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


	public  void setRecordFromLine1(String firstLine,String[] record,int fieldIndex){

		if (logger.isDebugEnabled()) {

			logger.debug(firstLine); //$NON-NLS-1$
		}
		StringBuffer printString = new StringBuffer();

		// Check to see if special character for beginning of record
		Matcher m = Pattern.compile("[\\W]").matcher(firstLine);


		if(m.find(0)){

			firstLine=firstLine.substring(1);
		}

		String[] splited = oneOrMoreSpacePattern.split(firstLine);

		for (int i=0;i<splited.length;i++) {


			// If field does'nt contain '/' or '%' symbol just assign value
			if(!splited[i].contains(Constants.FORWARD_SLASH) 
					&& !splited[i].contains(Constants.PERCENT_SYMBOL)){

				record[fieldIndex]=splited[i];
			}

			// if contains '/' then process
			else if(splited[i].contains(Constants.FORWARD_SLASH)){

				StringBuffer fieldBuffer= new StringBuffer(Constants.EMPTY_STRING);
				int indexOfSlash=0;

				// If '/' is first character then add previous array field 
				if((splited[i]!=null) 
						&& splited[i].charAt(0)==Constants.FORWARD_SLASH.charAt(0)){

					fieldBuffer.append(splited[i-1]+Constants.SPACE);
					fieldIndex--;
				}

				fieldBuffer.append(splited[i]);

				// If '/' is last character then add next array field 
				if(splited[i].indexOf(Constants.FORWARD_SLASH)== splited[i].length()-1){

					fieldBuffer.append(Constants.SPACE+splited[i+1]);
					i++;
				}

				record[fieldIndex]=fieldBuffer.toString().trim();


			}


			// if contains '%' then process
			else if (splited[i].contains(Constants.PERCENT_SYMBOL)){

				record[fieldIndex]=splited[i];
				fieldIndex++;
				if (logger.isDebugEnabled()) {
					logger.debug("setRecordFromLine1(String, String[], int) - fieldIndex=" 
							+ fieldIndex); //$NON-NLS-1$
				}
				record[fieldIndex]=splited[i+1]+Constants.SPACE+splited[i+2];
				i=i+2;
			}

			fieldIndex++;
		}
		printRecord(record, 0,fieldIndex);

		if (logger.isDebugEnabled()) {
			logger.debug("setRecordFromLine1(String, String[], int) - fieldIndex="
					+ fieldIndex); //$NON-NLS-1$
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

		if(logger.isInfoEnabled()){

			logger.info(record.getSrNo()
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
	public  void printRecord(String[] record, int startIndex, int endIndex) {

		StringBuffer printString = new StringBuffer();

		for(int k=startIndex;k<endIndex;k++){

			printString.append(record[k]+Constants.DOUBLE_PIPE);

		}
		if (logger.isInfoEnabled()) {

			logger.info( printString.toString()); //$NON-NLS-1$
		}
	}

	public  void setRecordFromLine2(String secondLine,String[] record,int fieldIndex){

		if (logger.isDebugEnabled()) {

			logger.debug(secondLine); //$NON-NLS-1$
		}
		fieldIndex=7;
		int startIndex = fieldIndex;
		StringBuffer nameBuffer = new StringBuffer();

		String[] splited = oneOrMoreSpacePattern.split(secondLine);
				
		for (int i=0;i<splited.length;i++) {

			if(i<=3){

				record[fieldIndex]=splited[i];
				fieldIndex++;
			}

			/* first 3 record are found; last record( person name) is found by adding 
			 * remaining array elements
			 * 
			 */
			if(i>=4){

				nameBuffer.append(splited[i]);
				nameBuffer.append(Constants.SPACE);
			}

		}
		// Last column did'nt increment,so increment now.
		record[fieldIndex]=nameBuffer.toString().trim();
		fieldIndex++;
		printRecord(record,startIndex,fieldIndex);
		if (logger.isDebugEnabled()) {

			logger.debug("setRecordFromLine2(String, String[], int) - fieldIndex=" 
					+ fieldIndex); //$NON-NLS-1$
		}
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

		if(logger.isInfoEnabled()){

			logger.info(record.getTotalLoan()
					+Constants.DOUBLE_PIPE+record.getEmi()
					+Constants.DOUBLE_PIPE+record.getOtherLoans()
					+Constants.DOUBLE_PIPE+record.getInitials()
					+Constants.DOUBLE_PIPE+record.getEmpName()
					);
		}

	}

	public  void setRecordFromLine3(String thirdLine,String[] record,int fieldIndex){

		fieldIndex=12;
		int startIndex=fieldIndex;
		if (logger.isDebugEnabled()) {

			logger.debug(thirdLine); //$NON-NLS-1$
		}

		String lastZipCodeNum = thirdLine.replaceAll("[^0-9]*$", "").replaceAll(".(?!$)", "");
		int indexOfEndOfZipCode = thirdLine.lastIndexOf(lastZipCodeNum);
		String country= thirdLine.substring(indexOfEndOfZipCode+1).trim();

		thirdLine = thirdLine.substring(0, indexOfEndOfZipCode+1);

		String[] splited = oneOrMoreSpacePattern.split(thirdLine);
				
		String regexZipcode = "^[0-9]{5}(?:-[0-9]{4})?$";
		String state=null;
		String city=null;
		String address=Constants.EMPTY_STRING;


		// Assume 2nd last field is zipcode
		int zipCodeIdx=splited.length-1;
		String zipCode=splited[zipCodeIdx];

		//check if zipcode is numeric
		//if(zipCode.matches("\\d*")){


		state=splited[zipCodeIdx-1];
		city=splited[zipCodeIdx-2];

		for (int i=0;i<zipCodeIdx-2;i++) {

			address= address+Constants.SPACE+splited[i];


		}	

		//}
		record[fieldIndex]=address.trim();
		fieldIndex++;

		record[fieldIndex]=city.trim();
		fieldIndex++;

		record[fieldIndex]=state.trim();
		fieldIndex++;

		record[fieldIndex]=zipCode.trim();
		fieldIndex++;

		record[fieldIndex]=country.trim();
		fieldIndex++;

		printRecord(record,startIndex,fieldIndex);
		if (logger.isDebugEnabled()) {

			logger.debug("setRecordFromLine3(String, String[], int) - fieldIndex="
					+ fieldIndex); //$NON-NLS-1$
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

		//		String state=null;
		//		String city=null;
		//		String address=Constants.EMPTY_STRING;


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

		if (logger.isInfoEnabled()){

			logger.info(record.getAddress()
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
					//record.setRefName(StringUtility.trim(splited[i]));
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
					//System.out.println(StringUtility.trim(splited[i]));
					break;

				case 2:
					yearsAfterString = StringUtility.trim(splited[i]);
					//System.out.println(yearsAfterString);
					break;	

				}
			}	
			
			splited = oneOrMoreSpacePattern.split(yearsAfterString);
					
			String department="";
			String position="";
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
			record.setYearsOfEmployment(
					record.getYearsOfEmployment().replaceAll("[L,l]|[I,i]", "1"));
			
			if (logger.isInfoEnabled()){

				logger.info(record.getContactMode()
						+Constants.DOUBLE_PIPE+record.getMaritalStatus()
						+Constants.DOUBLE_PIPE+record.getRefName()
						+Constants.DOUBLE_PIPE+record.getYearsOfEmployment()
						+Constants.DOUBLE_PIPE+record.getDesignation()
						+Constants.DOUBLE_PIPE+record.getDepartment()
						);
			}
	
		}

	public  void setRecordFromLine4(String fourthLine,String[] record,int FieldIndex){

		int fieldIndex=17;
		int startIndex=fieldIndex;
		int yearsIndex=0;
		if (logger.isDebugEnabled()) {

			logger.debug(fourthLine); //$NON-NLS-1$
		}

		String[] splited = oneOrMoreSpacePattern.split(fourthLine);
				
		for (int i=0;i<splited.length;i++){

			if(i<2){
				record[fieldIndex]=splited[i];
				fieldIndex++;
			}

			else if((splited[i]!=null) 
					&& (splited[i].equalsIgnoreCase(Constants.YEARS)
							||splited[i].equalsIgnoreCase(Constants.YEAR ))
					) {

				yearsIndex=i;
				StringBuffer nameBuffer = new StringBuffer();
				StringBuffer positionBuffer = new StringBuffer();

				// from index 2 to current index, add to string
				for(int j=2;j<yearsIndex-1;j++){

					nameBuffer.append(splited[j]+Constants.SPACE);
				}

				if (logger.isDebugEnabled()) {

					logger.debug("setRecordFromLine4(String, String[], int) - nameBuffer="); //$NON-NLS-1$
				}
				record[fieldIndex]=nameBuffer.toString().trim();

				fieldIndex++;

				// Assign experience field in years
				record[fieldIndex]=splited[i-1]+Constants.SPACE+splited[i];

				// from index 2 to current index, add to string
				for(int j=i+1;j<splited.length-1;j++){

					positionBuffer.append(splited[j]+Constants.SPACE);
				}
				// Assign Position
				fieldIndex++;
				record[fieldIndex]=positionBuffer.toString().trim();

				// Assign Department
				fieldIndex++;
				record[fieldIndex]=splited[splited.length-1];

				//
				i=splited.length;
				fieldIndex++;
				printRecord(record,startIndex,fieldIndex);
			}
		}

	}

	public  void setRecordFromLine5(String fifthLine,String[] record,int FieldIndex){

		int fieldIndex=23;
		int startIndex=fieldIndex;
		int yearsIndex=0;
		String amt="";
		if (logger.isDebugEnabled()) {

			logger.debug(fifthLine); //$NON-NLS-1$
		}
		StringBuffer officeBuffer = new StringBuffer();

		String[] splited = oneOrMoreSpacePattern.split(fifthLine);
				
		for (int i=0;i<splited.length;i++){

			if(i<2) {
				record[fieldIndex]=splited[i];
				fieldIndex++;
			}
			else {
				officeBuffer.append(splited[i]+Constants.SPACE);
			}
		}
		record[fieldIndex]=officeBuffer.toString().trim();
		fieldIndex++;
		printRecord(record,startIndex,fieldIndex);

	}


	public  void setFiscalRecordFromLine5(String fifthLine,FiscalRecord record){
		
		
		if(logger.isInfoEnabled()) {
			
			logger.info(fifthLine);
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

		if(logger.isInfoEnabled()){

			logger.info(record.getPerformance()
					+Constants.DOUBLE_PIPE+record.getBasicSalary()
					+Constants.DOUBLE_PIPE+record.getCenterName()
					);
		}
		int startIndexOfBasicSalary = fifthLine.lastIndexOf(record.getBasicSalary());
		int lastIndexOfBasicSalary = startIndexOfBasicSalary+record.getBasicSalary().length();


		record.setCenterName(StringUtility.trim(
				fifthLine.substring(lastIndexOfBasicSalary)));

		if(logger.isInfoEnabled()){

			logger.info(record.getPerformance()
					+Constants.DOUBLE_PIPE+record.getBasicSalary()
					+Constants.DOUBLE_PIPE+record.getCenterName()
					);
		}

	}






	public  void setRecordFromLine6(String sixthLine,String[] record,int FieldIndex){

		int fieldIndex=27;
		int startIndex=fieldIndex;
		int yearsIndex=0;

		if (logger.isInfoEnabled()) {

			logger.info(sixthLine); //$NON-NLS-1$
		}

		String[] splited = oneOrMoreSpacePattern.split(sixthLine);
				
		record[fieldIndex]=splited[0];
		fieldIndex++;
		StringBuffer healthPlanBuffer = new StringBuffer();

		// from index 1 to length index, add to string
		for(int j=1;j<splited.length;j++){

			healthPlanBuffer.append(splited[j]+Constants.SPACE);
		}

		if (logger.isDebugEnabled()) {

			logger.debug("setRecordFromLine6(String, String[], int) - healthPlanBuffer=" 
					+ healthPlanBuffer.toString().trim()); //$NON-NLS-1$
		}

		record[fieldIndex]=healthPlanBuffer.toString().trim();
		fieldIndex++;

		printRecord(record,startIndex,fieldIndex);
	}



	public void setFiscalRecordFromLine6(String sixthLine,FiscalRecord record){


		if (logger.isInfoEnabled()) {

			logger.info(sixthLine); //$NON-NLS-1$
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

		if(logger.isInfoEnabled()) {

			logger.info(record.getHealthId()
					+Constants.DOUBLE_PIPE+record.getHealthInsuranceProvider());
		}
	}
}

