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

		fiscalProcessor.processDataFile(fullFileName); 

		long endTime=System.currentTimeMillis();
		long timeTaken=endTime-startTime;

		if (logger.isInfoEnabled()) {

			logger.info("time taken in miliseconds=" 
					+ new Long(timeTaken).toString()); //$NON-NLS-1$
		}

	}


	public void processDataFile(String fullFileName) {

		int[] lineNum = { 0 };

		List recordList = new ArrayList();
		String[][] record = new String[1][29];
		FiscalRecord fiscalRecord = new FiscalRecord();
		int[] fieldIndex = {0};


		try (Stream<String> stream = Files.lines(Paths.get(fullFileName))) {


			stream.forEach((line)->{
				lineNum[0]++;
				switch(lineNum[0]){

				case 1:
					if(logger.isInfoEnabled()){

						logger.info("Processing record="+(recordList.size()+1));
					}
					setRecordFromLine1(line,record[0],fieldIndex[0]);
					setFiscalRecordFromLine1(line,fiscalRecord);
					break;
				case 2:
					//setRecordFromLine2(line,record[0],fieldIndex[0]);
					break;
				case 3:
					//setRecordFromLine3(line,record[0],fieldIndex[0]);
					break;
				case 4:
					//setRecordFromLine4(line,record[0],fieldIndex[0]);
				break;
				case 5:
					//setRecordFromLine5(line,record[0],fieldIndex[0]);
				break;
				case 6:
					//setRecordFromLine6(line,record[0],fieldIndex[0]);
					break;
				case 7:
					//setRecordFromLine7(line,record[0],fieldIndex[0]);
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

		String[] splited = firstLine.split("\\s+");
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
	    
	    String[] amountPercentArray = firstLine.substring(dollarIndex).split("\\s+");
	    
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
	    
		
		String[] splited = firstLine.split("\\s+");
		
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
		
		
			
		    int startIndexOfOccuranceNo = firstLine.indexOf(record.getOccuranceNo());
		    int endIndexOfOccuranceNo=startIndexOfOccuranceNo+record.getOccuranceNo().length();
		    String loanFileNo = firstLine.substring(endIndexOfOccuranceNo+1);
		    
		    if(logger.isDebugEnabled()){
		    	
		    	logger.debug("loanFileNo="+loanFileNo);
		    }
		    if(loanFileNo!=null
		    		&&!loanFileNo.equals(Constants.EMPTY_STRING)){
		    	
		    	Matcher m1 = spaceSlashSpacePattern.matcher(loanFileNo);
		    	
		    	if(!m1.matches()) {
		    	
		    		loanFileNo = loanFileNo.replaceAll("\\s+",Constants.EMPTY_STRING);
		    		loanFileNo = loanFileNo.replaceAll(Constants.FORWARD_SLASH, 
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

		String[] splited = secondLine.split("\\s+");
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

		String[] splited = thirdLine.split("\\s+");

		String regexZipcode = "^[0-9]{5}(?:-[0-9]{4})?$";
		String state=null;
		String city=null;
		String address=Constants.EMPTY_STRING;


		// Assume 2nd last field is zipcode
		int zipCodeIdx=splited.length-1;
		String zipCode=splited[zipCodeIdx];

		//check if zipcode is numeric
		if(zipCode.matches("\\d*")){

			
			state=splited[zipCodeIdx-1];
			city=splited[zipCodeIdx-2];

			for (int i=0;i<zipCodeIdx-2;i++) {

				address= address+Constants.SPACE+splited[i];


			}	

		}
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


	public  void setRecordFromLine4(String fourthLine,String[] record,int FieldIndex){

		int fieldIndex=17;
		int startIndex=fieldIndex;
		int yearsIndex=0;
		if (logger.isDebugEnabled()) {

			logger.debug(fourthLine); //$NON-NLS-1$
		}

		String[] splited = fourthLine.split("\\s+");
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

		String[] splited = fifthLine.split("\\s+");
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

	//		// Check to see if special character for beginning of record
	//		Matcher matcher = Pattern.compile(amt).matcher(fifthLine);
	//		int amtStartIndex=0;
	//		int amtEndIndex=0;
	//		String matcherAmt=null;
	//
	//		if (matcher.find()) {
	//			amtStartIndex = matcher.start();
	//			amtEndIndex = matcher.end();
	//			matcherAmt = matcher.group();


	//		System.out.println("amtStartIndex="+amtStartIndex
	//					           +"|amtEndIndex="+amtEndIndex);
	//		}	
	//
	//		String remainingString = fifthLine.substring(amtEndIndex+1);
	//		System.out.println("remainingString="+remainingString);
	//		
	//		String[] remainingSplited = remainingString.split("\\s+");



	public  void setRecordFromLine7(String seventhLine,String[] record,int FieldIndex){

		int fieldIndex=26;
		int startIndex=fieldIndex;

		if (logger.isDebugEnabled()) {

			logger.debug(seventhLine); //$NON-NLS-1$
		}
		record[fieldIndex]=seventhLine.trim();
		fieldIndex++;
		printRecord(record,startIndex,fieldIndex);




	}

	public  void setRecordFromLine6(String sixthLine,String[] record,int FieldIndex){

		int fieldIndex=27;
		int startIndex=fieldIndex;
		int yearsIndex=0;

		if (logger.isDebugEnabled()) {

			logger.debug(sixthLine); //$NON-NLS-1$
		}

		String[] splited = sixthLine.split("\\s+");
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

}
