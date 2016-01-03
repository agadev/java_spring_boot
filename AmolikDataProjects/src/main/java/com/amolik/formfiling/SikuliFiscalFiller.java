package com.amolik.formfiling;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.sikuli.script.*;

import com.amolik.util.AmolikProperties;

public class SikuliFiscalFiller {

	private static final Logger logger = Logger.getLogger(SikuliFiscalFiller.class);
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

	public static void main(String[] args) {

		extractProperties();
		Screen s = new Screen();
		String recordImagePath = "C:\\Debtor2910\\FPDATA1110_8\\FPDATA1110img1415.jpeg";
		ImagePath.add(imageDir);
		try{

			openDebtorApplication(s);
			setDebtorApplicationDatabase(s);

			try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputDataFile))) {
				String line = null;
				while ((line = reader.readLine()) != null) {	

					fillDebtorApplicationImage(s, line);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}catch(FindFailed  e){
			e.printStackTrace();
		}
	}

	public static void fillDebtorApplicationImage(Screen s, String line) 
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

				s.type(splited[i]);

				if(i<splited.length-1){

					s.type(Key.TAB);
				}
			}

		}

	}

	public static void setDebtorApplicationRecordImage(
			Screen s, String recordImagePath) 
					throws FindFailed {

		s.type("i", KeyModifier.CTRL);
		s.click(new Pattern(fileNameImage).targetOffset(fileNameImageOffsetX,fileNameImageOffsetY));
		s.wait(waitTimeBeforeTypingRecordImagePath);
		s.type(recordImagePath);
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
		s.type(debtorDatabasePath);
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

		if(logger.isDebugEnabled()){

			logger.debug("imageDir="+imageDir);
			logger.debug("windowsTaskBarImage="+windowsTaskBarImage);
		}
	}
}

