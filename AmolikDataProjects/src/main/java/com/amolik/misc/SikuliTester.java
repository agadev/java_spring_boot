package com.amolik.misc;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.sikuli.basics.Settings;
import org.sikuli.script.*;

import com.amolik.util.AmolikProperties;

public class SikuliTester {

	private static final Logger logger = Logger.getLogger(SikuliTester.class);
	private static String imageDir;
	private static String inputDataFile;
	private static String fileSeparator;
	private static String windowsTaskBarImage;
	private static String debtorIconImage;
	private static String fileNameImage;
	private static String srNoImage;
	private static String debtorDatabasePath;
	private static int windowTaskBarImageOffsetX;
	private static int windowTaskBarImageOffsetY;
	private static int debtorIconImageOffsetX;
	private static int debtorIconImageOffsetY;
	private static int fileNameImageOffsetX;
	private static int fileNameImageOffsetY;
	private static int srNoImageOffsetX;
	private static int srNoImageOffsetY;
	private static float waitTimeAfterClickingDebtorIcon;
	private static float waitTimeBeforeTypingDebtorDatabasePath;
	private static float waitTimeAfterTypingDebtorDatabasePath;
	private static float waitTimeBeforeTypingRecordImagePath;
	private static float waitTimeAfterTypingRecordImagePath;
	private static String inputDataImageDir;
	private static String inputFileDelimiter;
	private static int lastRecordProcessed;

	public static void main(String[] args) {

		long startTime=System.currentTimeMillis();
		if(logger.isInfoEnabled()) {

			logger.info("Starting Sikuli form filling process");
		}
		extractProperties();
		Screen s = new Screen();

		// Turn off sikuli loggind
		Settings.ActionLogs=false;
		Settings.InfoLogs=false;
		Settings.DebugLogs=false;
		Settings.MoveMouseDelay=(float) 0.1;
		
		
		ImagePath.add(imageDir);
		try{

			openDebtorApplication(s);
			setDebtorApplicationDatabase(s);

			if(logger.isInfoEnabled()){

				logger.info("Loaded database in "+(
						System.currentTimeMillis()-startTime)+" ms");
			}
			startTime=System.currentTimeMillis();
			s.click(new Pattern(srNoImage).targetOffset(srNoImageOffsetX,srNoImageOffsetY));
			//s.type(KeyEvent.VK_DEAD_ACUTE);
			
			}catch(FindFailed  e){
			e.printStackTrace();
		}
	}

	public static void fillDebtorApplicationFromImage(Screen s, String line,int currentRecord) 
			throws FindFailed {

		if(logger.isInfoEnabled()){

			logger.info(line);
		}
		String[] splited = line.split(inputFileDelimiter);


		for (int i=0;i<splited.length;i++){

			if(i==0){
				String inputDataImagePath= inputDataImageDir+fileSeparator+splited[i];


				setDebtorApplicationRecordImage(s, inputDataImagePath);
			}
			else {

				try {
					String field= splited[i];
					field="·ÈÌÛ˙¡…Õ”⁄";
					
					if(logger.isDebugEnabled()){
						
						logger.debug("fieldValue="+field);
					}

					// Trim if greater than 45
					if(field.length()>45){

						field = field.substring(0,44).substring(0, 
								field.lastIndexOf(" "));
						field="·ÈÌÛ˙¡…Õ”⁄";	
					}
					s.type(field);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(i<splited.length-1){
					s.type(Key.TAB);
					
				}
			}
		}
		System.exit(0);
		s.type("s",KeyModifier.CTRL);
		AmolikProperties.setProperty("sikuliFiller.lastRecordProcessed",
				Integer.toString(currentRecord));
		AmolikProperties.saveProperties();

	}

	public static void setDebtorApplicationRecordImage(
			Screen s, String recordImagePath) 
					throws FindFailed {

		s.type("i", KeyModifier.CTRL);
		s.click(new Pattern(fileNameImage).targetOffset(fileNameImageOffsetX,fileNameImageOffsetY));
		s.wait(waitTimeBeforeTypingRecordImagePath);
		s.paste(recordImagePath);
		s.type(Key.ENTER);
		s.wait(waitTimeAfterTypingRecordImagePath);
		s.click(new Pattern(srNoImage).targetOffset(srNoImageOffsetX,srNoImageOffsetY));
	}

	public static void setDebtorApplicationDatabase(Screen s) 
			throws FindFailed {

		s.type("o", KeyModifier.CTRL);
		s.click(new Pattern(fileNameImage)
				.targetOffset(fileNameImageOffsetX,fileNameImageOffsetY));

		s.wait(waitTimeBeforeTypingDebtorDatabasePath);
		s.paste(debtorDatabasePath);
		s.type(Key.ENTER);
		s.wait(waitTimeAfterTypingDebtorDatabasePath);
	}

	public static void openDebtorApplication(Screen s) 
			throws FindFailed {

		s.click(new Pattern(windowsTaskBarImage)
				.targetOffset(windowTaskBarImageOffsetX,
						windowTaskBarImageOffsetY));

		s.type("d",Key.WIN);
		s.doubleClick(debtorIconImage);
		s.wait(waitTimeAfterClickingDebtorIcon);
	}

	public static void extractProperties() {

		imageDir = AmolikProperties.getProperty("sikuliFiller.imageDir");
		fileSeparator = System.getProperty("file.separator");

		windowsTaskBarImage = AmolikProperties.getProperty("sikuliFiller.windowsTaskBarImage");

		debtorIconImage = AmolikProperties.getProperty("sikuliFiller.debtorIconImage");
		fileNameImage = AmolikProperties.getProperty("sikuliFiller.fileNameImage");
		srNoImage = AmolikProperties.getProperty("sikuliFiller.srNoImage");
		debtorDatabasePath = AmolikProperties.getProperty("sikuliFiller.debtorDatabasePath");

		windowTaskBarImageOffsetX = new Integer(
				AmolikProperties.getProperty("sikuliFiller.windowsTaskBarImage.offset.x"));

		windowTaskBarImageOffsetY = new Integer(
				AmolikProperties.getProperty("sikuliFiller.windowsTaskBarImage.offset.y"));

		debtorIconImageOffsetX = new Integer(
				AmolikProperties.getProperty("sikuliFiller.debtorIconImage.offset.x"));

		debtorIconImageOffsetY = new Integer(
				AmolikProperties.getProperty("sikuliFiller.debtorIconImage.offset.y"));

		fileNameImageOffsetX = new Integer(
				AmolikProperties.getProperty("sikuliFiller.fileNameImage.offset.x"));

		fileNameImageOffsetY = new Integer(
				AmolikProperties.getProperty("sikuliFiller.fileNameImage.offset.y"));

		srNoImageOffsetX = new Integer(
				AmolikProperties.getProperty("sikuliFiller.srNoImage.offset.x"));

		srNoImageOffsetY = new Integer(
				AmolikProperties.getProperty("sikuliFiller.srNoImage.offset.y"));

		waitTimeAfterClickingDebtorIcon = new Float(
				AmolikProperties.getProperty("sikuliFiller.afterClick.debtorIcon.waitTime"));

		waitTimeBeforeTypingDebtorDatabasePath = new Float(
				AmolikProperties.getProperty("sikuliFiller.beforeTyping.debtorDatabasePath.waitTime"));

		waitTimeAfterTypingDebtorDatabasePath = new Float(
				AmolikProperties.getProperty("sikuliFiller.afterTyping.debtorDatabasePath.waitTime"));

		waitTimeBeforeTypingRecordImagePath = new Float(
				AmolikProperties.getProperty("sikuliFiller.beforeTyping.recordImagePath.waitTime"));

		waitTimeAfterTypingRecordImagePath = new Float(
				AmolikProperties.getProperty("sikuliFiller.afterTyping.recordImagePath.waitTime"));

		inputDataFile = AmolikProperties.getProperty("sikuliFiller.inputDataFileDir")
				+fileSeparator
				+AmolikProperties.getProperty("sikuliFiller.inputDatafileName");

		inputDataImageDir = AmolikProperties.getProperty("sikuliFiller.inputDataImageBaseDir")
				+fileSeparator
				+AmolikProperties.getProperty("sikuliFiller.inputDatafileName");

		inputFileDelimiter = AmolikProperties.getProperty("sikuliFiller.inputFileDelimiter");

		lastRecordProcessed = new Integer(
				AmolikProperties.getProperty("sikuliFiller.lastRecordProcessed"));

		if(logger.isDebugEnabled()){

			logger.debug("imageDir="+imageDir);
			logger.debug("windowsTaskBarImage="+windowsTaskBarImage);
		}
	}
}

