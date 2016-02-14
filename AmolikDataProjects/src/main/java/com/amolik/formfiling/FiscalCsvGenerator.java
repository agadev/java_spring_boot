package com.amolik.formfiling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
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


public class FiscalCsvGenerator {

	Pattern doublePipePattern = Pattern.compile("\\|\\|");
	DecimalFormat fourDigitFormatter = new DecimalFormat("0000");

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FiscalCsvGenerator.class);

	/**
	 *  Initializes property file.
	 *  
	 *  
	 */

	Pattern spaceSlashSpacePattern = Pattern.compile(Constants.SPACE_FORWARD_SLASH_SPACE);



	private static String inputFileDir;

	private static String fileSeparator;

	private static String inputFileName;

	private static String outputFileDelimiter;

	private static String inputFileFullName;

	private String outFileDir;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FiscalCsvGenerator fiscalProcessor = new FiscalCsvGenerator();
		fiscalProcessor.initializeFromPropertyFile();
		List<FiscalRecord> recordList = new ArrayList<FiscalRecord>();


		if(fiscalProcessor.inputFileDir==null){

			System.out.println("amolik.properties file not found, shutting down system");
			System.exit(1);
		}


		long startTime=System.currentTimeMillis();


		fiscalProcessor.processDataFile(inputFileFullName,
				inputFileName,recordList); 

		fiscalProcessor.writeDelimitedRecordsToFile(
				StringUtility.getFileNameWithoutExtension(inputFileName)+"_csv.txt"
				,outputFileDelimiter,recordList);

		long endTime=System.currentTimeMillis();
		long timeTaken=endTime-startTime;

		if (logger.isInfoEnabled()) {

			logger.info("time taken in miliseconds=" 
					+ new Long(timeTaken).toString()); //$NON-NLS-1$
		}

	}


	public void initializeFromPropertyFile() {

		inputFileDir = AmolikProperties.getProperty("fiscalCsvGenerator.inputFileCsvDir");
		outFileDir = AmolikProperties.getProperty("fiscalCsvGenerator.outputFileCsvDir");
		fileSeparator = System.getProperty("file.separator");
		inputFileName = AmolikProperties.getProperty("fiscalCsvGenerator.inputfileCsvName");
		outputFileDelimiter = AmolikProperties.getProperty("fiscalCsvGenerator.outputFileCsvDelimiter");
		inputFileFullName = inputFileDir+fileSeparator +inputFileName;
	}


	private void writeDelimitedRecordsToFile(String outFileNameWithoutExt,
			String outputFileDelimiter,List<FiscalRecord> recordList) {

		String outputFilePath = outFileDir
				+fileSeparator+outFileNameWithoutExt;

		FileUtility.writeCsvFiscalRecordsToFile(outputFilePath, outputFileDelimiter, recordList);
	}


	public void processDataFile(String fullFileName,String fileName,List<FiscalRecord> recordList) {


		if(logger.isInfoEnabled()){

			logger.info("Processing input file ="+fullFileName);
		}

		int lineNum = -1 ;

		try (BufferedReader reader = Files.newBufferedReader(Paths.get(fullFileName))) {
			FiscalRecord fiscalRecord = new FiscalRecord();
			String line = null;
			while ((line = reader.readLine()) != null) {
				lineNum++;
				switch(lineNum){

				case 0:
					if(logger.isInfoEnabled()){

						logger.info("Processing record="+(recordList.size()+1));
					}

					setFiscalRecordFromLine0(line,fiscalRecord);

					break;
				case 1:
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
					// Add to list 
					recordList.add(fiscalRecord);
					fiscalRecord = new FiscalRecord();
					// Reset to new record
					lineNum=-1;


					if(logger.isDebugEnabled()) {

						logger.debug("setting line num to 0");
					}
					break;
				}

			}
		} catch (IOException ex) {
			// do something with exception
			logger.error("main(String[])", ex); //$NON-NLS-1$
		}
	}




	public  FiscalRecord setFiscalRecordFromLine0(String imageNameLine,FiscalRecord record){

		if (logger.isDebugEnabled()) {

			logger.debug(imageNameLine); 
		}

		record.setImageFileName(StringUtility.trim(imageNameLine));
		return record;
	}

	public  FiscalRecord setFiscalRecordFromLine1(String firstLine,FiscalRecord record){

		if (logger.isDebugEnabled()) {

			logger.debug(firstLine); 
		}

		String[] splited = doublePipePattern.split(firstLine,8);

		for (int i=0;i<splited.length;i++) {


			switch (i+1){

			case 1: record.setSrNo(StringUtility.trim(splited[i]));
			break;

			case 2: record.setEmpIdNo(StringUtility.trim(splited[i]));
			break;

			case 3:record.setOccuranceNo(StringUtility.trim(splited[i]));
			break;

			case 4: record.setLoanFileNo(StringUtility.trim(splited[i]));
			break;

			case 5: record.setLoanAmount(StringUtility.trim(splited[i]));
			break;

			case 6:record.setRateOfInterest(StringUtility.trim(splited[i]));
			break;

			case 7:record.setTenure(StringUtility.trim(splited[i]));
			break;
			
			default:break;

			}
		}	

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
		return record;
	}

	public  void setFiscalRecordFromLine2(String secondLine,FiscalRecord record){

		if (logger.isDebugEnabled()) {

			logger.debug(secondLine); 
		}

		String[] splited = doublePipePattern.split(secondLine,6);

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
				break ;
				case 5: record.setEmpName(StringUtility.trim(splited[i]));
				break;
				default:break;
				}
			}

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

			logger.debug(thirdLine);
		}

		String[] splited = doublePipePattern.split(thirdLine,6);

		tillSetInitials :
			for (int i=0;i<splited.length;i++) {

				switch (i+1){

				case 1: record.setAddress(StringUtility.trim(splited[i]));
				break;
				case 2: record.setCity(StringUtility.trim(splited[i]));
				break;
				case 3: record.setState(StringUtility.trim(splited[i]));
				break;
				case 4: record.setZip(StringUtility.trim(splited[i]));
				break ;
				case 5: record.setCountry(StringUtility.trim(splited[i]));
				break;
				default:break;
				}
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

		if (logger.isDebugEnabled()) {

			logger.debug(fourthLine); 
		}

		String[] splited = doublePipePattern.split(fourthLine,7);


		for (int i=0;i<splited.length;i++){

			switch (i+1){

			case 1:record.setContactMode(StringUtility.trim(splited[i]));
			break;

			case 2:record.setMaritalStatus(StringUtility.trim(splited[i]));
			break;	

			case 3:record.setRefName(StringUtility.trim(splited[i]));
			break;

			case 4:
				record.setYearsOfEmployment(StringUtility.trim(splited[i]));
				break;

			case 5:
				record.setDesignation(StringUtility.trim(splited[i]));
				break;	

			case 6:
				record.setDepartment(StringUtility.trim(splited[i]));
				break;
			default:break;
			}
		}
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

		String[] splited = doublePipePattern.split(fifthLine,5);


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

				case 4:
					record.setIssuerBank(StringUtility.trim(splited[i]));
					break ;
					
				default:break;
				}
			}

		if(logger.isDebugEnabled()){

			logger.debug(record.getPerformance()
					+Constants.DOUBLE_PIPE+record.getBasicSalary()
					+Constants.DOUBLE_PIPE+record.getCenterName()
					+Constants.DOUBLE_PIPE+record.getIssuerBank()
					);
		}
	}


	public void setFiscalRecordFromLine6(String sixthLine,FiscalRecord record){


		if (logger.isDebugEnabled()) {

			logger.debug(sixthLine); 
		}

		String[] splited = doublePipePattern.split(sixthLine,3);

		forLoop:
			for(int j=0;j<splited.length;j++){

				switch (j+1){

				case 1: record.setCarrierName(StringUtility.trim(splited[j]));
				break;
				case 2:
					record.setEisCode(StringUtility.trim(splited[j]));
					break;
				default:break;
				}
			}

		if(logger.isDebugEnabled()) {

			logger.debug(record.getCarrierName()
					+Constants.DOUBLE_PIPE+record.getEisCode());
		}
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

			logger.info(record.getCarrierName()
					+Constants.DOUBLE_PIPE+record.getEisCode());
		}
	}
}

