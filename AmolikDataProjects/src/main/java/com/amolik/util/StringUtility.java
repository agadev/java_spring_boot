package com.amolik.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {

	public static final Pattern HINDI_CHARACTERS = Pattern.compile("[\u0900-\u097F]+");

	public static boolean isHindi(String compareString){

		Matcher matcher = HINDI_CHARACTERS.matcher(compareString);

		if(matcher.find()){
			return true;  // it's RTL
		} 

		return false;
	}
	
	public static String getLowerCaseString(String column){
		
		
		if(column!=null && !column.equals(Constants.SPACE_0)) {

			column=column.toLowerCase(Locale.ROOT).trim();
		}
		
		return column;
	}
}
