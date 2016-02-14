package com.amolik.misc;
import java.text.Normalizer;
import java.util.regex.Pattern;

import com.amolik.util.*;

public class TestHindi {

	public static void main (String args[]) {

		String hindiString = "ऑनलाइन मुक्त";
		String englishString= "english string";
		
		 String imageName ="FPDATA1110img1401.jpeg";
		 
	       int indexOfimg = imageName.indexOf("img");
	       String imageNumberString = imageName.substring(indexOfimg+3, indexOfimg+7);
	       int imageNumber = new Integer(imageNumberString);
	       int folderNumber = (imageNumber/200)+1;
	       String folderPrefix = imageName.substring(0,indexOfimg);
	       System.out.println("Imagenum="+imageNumberString);
	       System.out.println("folderPrefix="+folderPrefix);
	       String imagePath = "C:\\Debtor2910"+"\\"+folderPrefix+"_"+folderNumber+"\\"+imageName;
	       System.out.println("imagePath="+imagePath);
		
//		System.out.println(hindiString+" is hindiString="
//				+new Boolean(StringUtility.isHindi(hindiString)).toString());
//
//		System.out.println(englishString+" is hindiString="
//				+new Boolean(StringUtility.isHindi(englishString)).toString());
//		
//
//		System.out.println(hindiString+" hindi lowercase="
//				+hindiString.toLowerCase());
//
//        String field = "4,LSO.00";
//        System.out.println("before field="+field);
//		field=field
//				.replaceAll("(?i)L|(?i)I", "1")
//				.replaceAll("(?i)o", "0")
//				.replaceAll("(?i)s", "5")
//				;
//		//field=field.replaceAll("[O,o]", "0");
//		//field=field.replaceAll("[S,s]", "5");
//		
//		System.out.println("field="+field);
//		String str = " á ó ú ñ é í";
//		System.out.println("beforeString="+str);
//		 String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
//		 Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//		 nfdNormalizedString=pattern.matcher(nfdNormalizedString).replaceAll("");
//		 System.out.println("afterString="+nfdNormalizedString);
//		 
//		 long startTime = System.nanoTime();
//		 String zipCodeString ="11434";
//		 int zipCode =new Integer(zipCodeString);
//		 System.out.println(zipCode/10000);
//		 
//		 switch ((int) zipCode/10000) {
//		 
//		    case 0:
//		        System.out.println("Zipcode="+zipCodeString+
//		        		"|city="+ZipCodeCity1.getCity(zipCodeString));
//		        break;
//		    case 1:
//		    	 System.out.println("Zipcode="+zipCodeString+
//			        		"|city="+ZipCodeCity2.getCity(zipCodeString));
//		        break;
//		    case 3:
//		        System.out.println("30-39");
//		        break;
//		    case 4:
//		        System.out.println("40-49");
//		        break;
//		    default:
//		        break;
//		}
//		 
//		 long endTime = System.nanoTime();
//		 System.out.println("Time taken for zip lookup="+(endTime-startTime)/1000000+" ms");
	}
}
