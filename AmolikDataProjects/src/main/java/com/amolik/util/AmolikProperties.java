package com.amolik.util;


import java.util.*;
import java.io.*;

public class AmolikProperties	{


	protected static Properties amolikProp	=	null;
	protected static String propertiesFilePath = null;

	private AmolikProperties(){
		initialize();
	}

	private static void initialize(){

		amolikProp=new Properties();
		//System.out.println("Amolik Properties int method");

		if( propertiesFilePath == null){
			//System.out.println("Amolik Properties file path is null");

			try{
				File global = new File(System.getProperty("propertiesFile"));
				FileInputStream aFin=new FileInputStream(global);
				amolikProp.load(aFin);

				File local = new File(propertiesFilePath+".local");
				if( local.exists()){
					FileInputStream aFin2=new FileInputStream(local);
					amolikProp.load(aFin2);
				}

				System.out.println("PropertiesFile="
						+System.getProperty("propertiesFile"));
			}
			catch(FileNotFoundException fnfe){
				System.out.println("Problem starting AmolikProperties: File not found "+fnfe);
			}
			catch(IOException ioe){
				System.out.println("Problem starting AmolikProperties: Error reading file "+ioe);
			}
		}

		String log4jPropertyFile = AmolikProperties.getProperty("log4jFile");
		Properties log4jProperties = new Properties();

	}

	public static void setProperties( String filePath){
		System.out.println("start loading properties: "+filePath);

		propertiesFilePath  = filePath;
		initialize();
	}

	public static void reset(){
		System.out.println("Reset properties.");
		initialize();
	}

	public static Properties getProperties(){
		if(amolikProp == null)	
			initialize();

		return amolikProp;
	}

	public static String getProperty(String key){

		if(amolikProp == null)	
			initialize();

		return amolikProp.getProperty(key);
	}


	public static String getProperty( String _name, String _default ) {
		if (amolikProp == null)	
			initialize();

		String ret = amolikProp.getProperty(_name,_default);

		return( ret );
	}


	public static void setProperty(String name,String value){
		if (amolikProp == null)	
			initialize();
		amolikProp.setProperty(name,value);
	}
	
	public static void saveProperties()
    {
            try {
				FileOutputStream fr = new FileOutputStream(new File(System.getProperty("propertiesFile")));
				amolikProp.store(fr,"");
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
    }

	public static Enumeration propertyNames(){
		if(amolikProp == null) initialize();

		return amolikProp.propertyNames();
	}

}



