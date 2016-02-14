package com.amolik.misc;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amolik.util.Constants;
import com.amolik.util.StringUtility;

public class TestBigDecimal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Scanner s = new Scanner(System.in);
		//String line = s.nextLine();
		String line = "1212312312312312312312313 123432432432432423432423432";
		//"98765432109876543210 9876543210987654321   ";
		//		String[] splited = line.split("\\s+");
		//		
		//		for (int i=0;i<splited.length;i++) {
		//			
		//			if(splited[i].matches("[0-9]*")){
		//				
		//				BigDecimal num = new BigDecimal(splited[i]);
		//				System.out.println("num "+(i+1)+"="+num);
		//			}
		//		}
		String e = new String("E");
		//System.out.println("E".matches(e));

		String fileName="FPDATA1110_10-0.txt";
		//		int indexOfUnderscore = fileName.indexOf("_");
		//		String file= fileName.substring(0,indexOfUnderscore)+"img";
		//		fileName = fileName.substring(indexOfUnderscore+1);
		//		int indexOfNonNumber = fileName.indexOf("^[0-9]*");
		//int folderCount = StringUtility.getLastRecordFromFileName(fileName, 200);

		//		System.out.println("file="+fileName
		//				+"|record="+folderCount);

		String fourthLine =
				//"Email	Married	TABITHA.BUCKINGHAM 18th YEARS Legal Executive Not Available";
				//"Email	Married	TABITHA.BUCKINGHAM 18th YEARS Executive Not Available";
		//"Email	Married	ANT ONI 0_G ON ZALEZ	15th M0nths Legal Executive Distribution";
		//"Email	NA	COLLEEN.RUSCITO	5th years ASST MANAGER NA";
		"Email	Married	ANT ONI 0_G ON ZALEZ	15th M0nths Executive Distribution";
		System.out.println(fourthLine);
		String[] splited = fourthLine.split("\\s+",3);
		for (int i=0;i<splited.length;i++){

			switch (i+1){

			case 1:
				System.out.println(StringUtility.trim(splited[i]));
				break;

			case 2:
				System.out.println(StringUtility.trim(splited[i]));
				break;	

			case 3:
				fourthLine = (StringUtility.trim(splited[i]));
				break;

			}
		}
		//Pattern p = Pattern.compile("YEAR[S]?|MONTH[S]?");

		String yearsContainingString = "";
		String yearsAfterString="";
		splited = fourthLine.split("[Y,y][E,e][A,a][R,r][S,s]?|[M,m][O,o,0][N,n][T,t][H,h][S,s]?");
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
		splited = yearsAfterString.split("\\s+");
		String department="";
		String position="";
		for (int i=0;i<splited.length;i++){

			switch (i+1){

			case 1:
				position = StringUtility.trim(splited[i]);
				//System.out.println(position);

				break;

			case 2:
				if(splited.length==2){

					department = StringUtility.trim(splited[i]);
				}
				// check if second word is not 
				else if(splited.length>=3) {

					// if true that means it contains position field
					if(splited[i]!=null && !splited[i].trim().equalsIgnoreCase("NOT")){

						position = StringUtility.trim(splited[i-1]+Constants.SPACE+splited[i]);
					} 
					else {
						department=StringUtility.trim(splited[i]+Constants.SPACE+splited[i+1]);
					}
				}
				break;	

			case 3:
				if(splited.length==3){
					if(splited[i]!=null && !splited[i].trim().equalsIgnoreCase("AVAILABLE")){
						
						department=StringUtility.trim(splited[i]); 
						//System.out.println(department);
					}
				}
				break;

			case 4:
				department=StringUtility.trim(splited[i-1]+Constants.SPACE+splited[i]); 
				//	System.out.println(department);
				break;

			}
		}

		System.out.println("position="+position+"|department="+department);

		yearsContainingString = StringUtility.trim(
				fourthLine.substring(0,fourthLine.indexOf(yearsAfterString))
				);
		//System.out.println("yearsContaining="+yearsContainingString);
		//System.out.println("yearsContaining="+yearsContainingString.replace("\\s{2,}", "\\s"));

		splited = yearsContainingString.split("\\s+");
		String experience="";
		if(splited.length>2){

			experience = splited[splited.length-2]+Constants.SPACE+splited[splited.length-1];
			System.out.println("experience="+experience);
		}
		StringBuffer nameBuffer= new StringBuffer();
		for(int i=0;i<splited.length-2;i++){
			
			nameBuffer.append(StringUtility.trim(splited[i]));
		}
		
		System.out.println("name="+nameBuffer.toString());
	}	

}

