package com.amolik.misc;

public class StringDoubleQuotesRemover {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String one = new String("\"1\",\"abc\",\"xyz\",\"\"Hyderabad\"\"");
		one = one.replaceAll("\",\"",",");
				//replace("[\"{1}]", "");
				//.replace("\"\"", "\""); // two double quote to single quote
				//replace("\"\""," \"");
		System.out.println(one);
	}

}
