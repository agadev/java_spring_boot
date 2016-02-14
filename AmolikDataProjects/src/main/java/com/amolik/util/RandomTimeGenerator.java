package com.amolik.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.amolik.formfiling.FiscalProcessor;
import com.amolik.util.AmolikProperties;
import com.amolik.util.Constants;
import com.amolik.util.ExcelUtil;


public class RandomTimeGenerator {

	private static final Logger logger = Logger.getLogger(RandomTimeGenerator.class);
	public static Random rand = new Random();
	public static final String fiscalDateFormat = "dd-MMM-yy hh:mm:ss a";
	public static final String offDateFormat = "dd-MMM-yy";
	public static SimpleDateFormat fiscalFormatter = new SimpleDateFormat(fiscalDateFormat);
	public static SimpleDateFormat offDateFormatter = new SimpleDateFormat(offDateFormat);

	public static void main(String[] args) {
		// 14-Jan-16 2:14:09 PM

		long startTime = System.currentTimeMillis();
		System.out.println(offDateFormatter.format(startTime));
		String dayStartTimeString = offDateFormatter.format(startTime)+ " 8:00:00 AM";
		System.out.println(dayStartTimeString);
		Date dayStartTime=new Date();
		try {
			dayStartTime = fiscalFormatter.parse(dayStartTimeString);
			long dayStartMilliseconds = dayStartTime.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dayStartTime);
		//getListOfRandomTime(startTime,300);

	}

	public static long getNextRandomTime(long startTime) {

		long time1 = getNextRandomTime(startTime,160,190);
		return time1;
	}
	
	public static long getNextRandomTime(long startTime,int min,int max) {

		int randomNum = rand.nextInt(max-min+1)+min;
		long time1 = startTime+((randomNum)*1000);
		return time1;
	}

	public static List<String> getListOfRandomTime(long startTime,int listSize){

		List<String> timeList = new ArrayList<String>(listSize);

		for(int i=0;i<listSize;i++){

			long nextNumber = getNextRandomTime(startTime);
			timeList.add(fiscalFormatter.format(nextNumber));
			
			if (logger.isDebugEnabled()) {
				
				logger.debug("Date "+(i+1)
						+" ="+fiscalFormatter.format(nextNumber));
			}
			startTime = nextNumber;
		}
		return timeList;
	}
}

