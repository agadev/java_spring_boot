package com.amolik.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {

	public static final Pattern HINDI_CHARACTERS = Pattern.compile("[\u0900-\u097F]+");
	public static final Pattern removeDiacriticalMarksPattern = 
			Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

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

	public static String trim(String s){

		if(s != null||!s.equals("null")) {

			s = s.trim();
		}

		return s;

	}

	public static int getLastRecordFromFileName(String fileName,int folderSize){


		Matcher matcher = Pattern.compile("_[0-9]+").matcher(fileName);
		int folderCount=0;
		if (matcher.find()) {

			//System.out.println("matcher="+matcher.group());
			folderCount = Integer.valueOf(matcher.group().replace("_", ""));
		}

		if(folderCount>0){
			folderCount--;
		}
		return folderCount*folderSize;
	}

	public static String getFileNameBeforeUnderScore(String fileName){

		int indexOfUnderscore = fileName.indexOf("_");
		String baseFile= fileName.substring(0,indexOfUnderscore);
		return baseFile;
	}

	public static String getDeAccentedString(String str) {

		if(str!=null && !str.equals("")) {

			String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 

			return removeDiacriticalMarksPattern.matcher(nfdNormalizedString).replaceAll("");
		}

		return str;
	}
}
