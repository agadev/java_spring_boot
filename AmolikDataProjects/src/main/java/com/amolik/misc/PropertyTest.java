package com.amolik.misc;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.swing.KeyStroke;

import com.amolik.util.AmolikProperties;
import com.amolik.util.Constants;
import com.amolik.util.ExcelUtil;

import javafx.scene.input.KeyCode;

public class PropertyTest {

	public static Random rand = new Random();
	public static final String fiscalDateFormat = "dd-MMM-yy hh:mm:ss a";
	public static SimpleDateFormat fiscalFormatter = new SimpleDateFormat(fiscalDateFormat);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		ExcelUtil excelUtil = new ExcelUtil();

		// 14-Jan-16 2:14:09 PM
		
		long startTime = System.currentTimeMillis();
		getListOfRandomTime(startTime,300);
		
		
		
//		String field1 = field.substring(0,indexOfSlash+1);
//		String field2 = field.substring(indexOfSlash+1);
//		System.out.println("field1="+field1);
//		System.out.println("field2="+field2);
		
		
//		System.out.println("Encoding was: "+System.getProperty ("file.encoding"));
//		String longString = "MODOC HALL USGS MS 3001 3020 STATE UNIV DR EAST";
//		String a_accent = "à";
//		char accent ='â';
//		char accent1='ô';
//		int keycode=KeyEvent.getExtendedKeyCodeForChar(accent);
//		
//		System.out.println(accent+ " "+keycode);
//		System.out.println(accent1+ " "+KeyEvent.getExtendedKeyCodeForChar(accent1));
//		System.out.println("a"+ " "+KeyEvent.getExtendedKeyCodeForChar('a'));
//		System.out.println("á"+ " "+KeyEvent.getKeyText(keycode));
//		
//		
//		System.out.println(a_accent.toUpperCase());
//		
//		System.out.println("\u00e0");
//		longString = longString.substring(0,44).substring(0, 
//				longString.lastIndexOf(" "));
//		System.out.println(longString);
//		longString = longString.substring(0, 
//				longString.lastIndexOf(" "));
//		System.out.println(longString);
//		
//		
//		String word = "LEGAL EXECUTÏVE";
//		for (int i = 0; i < word.length(); i++) {
//            int keyCode = KeyEvent.getExtendedKeyCodeForChar(word.charAt(i));
//            System.out.println(word.charAt(i)+"="+keyCode);
//        }
//
	}

	public static long getNextRandomTime(long startTime) {
		
		int randomNum = rand.nextInt(31)+160;
		long time1 = startTime+((randomNum)*1000);
		return time1;
	}

   public static List<String> getListOfRandomTime(long startTime,int listSize){
	   
	   List<String> timeList = new ArrayList<String>(listSize);
	   
	   for(int i=0;i<listSize;i++){
	   
		   long nextNumber = getNextRandomTime(startTime);
		   timeList.add(fiscalFormatter.format(nextNumber));
		   System.out.println("Date "+(i+1)+" ="+fiscalFormatter.format(nextNumber));
		   startTime = nextNumber;
	   }
	   return timeList;
   }
}

