package com.amolik.formfiling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.amolik.data.FiscalRecord;
import com.amolik.util.AmolikProperties;
import com.amolik.util.Constants;

public class ExcelOnlineForm {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExcelOnlineForm.class);

	public static void main(String[] args) {

		String fileLoc=AmolikProperties.getProperty("excelOnline.inputFileDir");
		String fileSeparator=System.getProperty("file.separator");
		String fileName=AmolikProperties.getProperty("excelOnline.inputfileName");
		String fullFileName=fileLoc+fileSeparator +fileName;

		if(fileLoc==null){

			System.out.println("amolik.properties file not found, shutting down system");
			System.exit(1);
		}


		long startTime=System.currentTimeMillis();

		if(logger.isInfoEnabled()) {

			logger.info("reading data file="+fullFileName);

		}

		ExcelOnlineForm excelOnline = new ExcelOnlineForm();
		excelOnline.extractDataFromFile(fullFileName); 



		long endTime=System.currentTimeMillis();
		long timeTaken=endTime-startTime;

		if (logger.isInfoEnabled()) {

			logger.info("time taken in miliseconds=" 
					+ new Long(timeTaken).toString()); //$NON-NLS-1$
		}

	}

	public List<String[]> extractDataFromFile(String fullFileName) {

		int[] lineNum = { 0 };

		List<String []> recordList = new ArrayList<String []>();
		String[][] record = new String[1][16];
		int[] fieldIndex = {0};


		try (Stream<String> stream = Files.lines(Paths.get(fullFileName))) {


			stream.forEach((line)->{
				lineNum[0]++;

				setRecordFromLine(line,record[0],0);
				recordList.add(record[0]);


			});
			stream.close();
		} catch (IOException ex) {
			// do something with exception
			logger.error("main(String[])", ex); //$NON-NLS-1$
		}

		return recordList;
	}

	public void setRecordFromLine(String line,String[] record,int fieldIndex){

		if (logger.isDebugEnabled()) {

			logger.debug(line); //$NON-NLS-1$
		}


		String[] splited = line.split("\\s+");
		for (int i=0;i<splited.length;i++) {

			record[i]=splited[i];
		}

		printRecord(record, 0,splited.length);

		if (logger.isDebugEnabled()) {
			logger.debug("setRecordFromLine1(String, String[], int) - fieldIndex="
					+ fieldIndex); //$NON-NLS-1$
		}
	}

	public void printRecord(String[] record, int startIndex, int endIndex) {

		StringBuffer printString = new StringBuffer();

		for(int k=startIndex;k<endIndex;k++){

			printString.append(record[k]+Constants.DOUBLE_PIPE);

		}
		if (logger.isInfoEnabled()) {

			logger.info( printString.toString()); //$NON-NLS-1$
		}
	}
}