package com.amolik.scrapers;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.amolik.formfiling.FiscalEncryptedExcelGenerator;


public class SeleniumImageDownloader {
	
	private static final Logger logger = Logger.getLogger(SeleniumImageDownloader.class);

	public static void main(String args[]){

		try {
			getImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getImage() throws Exception{
		//      String[] commands = new String[]{};
		//      String AutoItScriptpath = "c:\test\saveImage.exe";
		//      commands = new String[]{AutoItScriptpath };
		//      Runtime.getRuntime().exec(commands);
		// 
		//      selenium = new DefaultSelenium("localhost", 4444,
		//      "googlechrome", "http://www.testingexcellence.com");
		//      selenium.start();
		//      selenium.open("/images/logo.jpg");
		//      selenium.windowMaximize();

		//saveImageJava();
		if(logger.isInfoEnabled()){
			logger.info("Starting downloading image");
		}
		saveAllImageJava(1,20);
		// saveImageUsingWebDriver();
	}

	public static void saveImageUsingWebDriver() throws AWTException {
		WebDriver driver = new FirefoxDriver();

		driver.get("http://www.easymakemoney4u.com/I2E_Admin/ClientImage/Img316.jpg");

		waitForImage(driver);
		//native key strokes for CTRL and S keys
		Robot robot = new  Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		//once the dialog box is displayed, the autoIt script
		//will fire off and send an Enter command.
		//TimeUnit.SECONDS.sleep(3);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);

		if(driver!=null){

			driver.close();

		}
	}


	public static void saveImageJava(){
		//E:\Automation\ARIZONA YELLOW  PAGES\images
		try(InputStream in = new URL("http://www.easymakemoney4u.com/I2E_Admin/ClientImage/Img316.jpg").openStream()){
			Files.copy(in, Paths.get("E:\\Automation\\NEOCAP\\images\\1.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveAllImageJava(int startPage,int endPage){

		for(int i=startPage;i<=endPage;i++) {

			if(logger.isInfoEnabled()){
				logger.info("downloading image "+i);
			}
			try(InputStream in = 
					new URL("http://www.realpageslive.com/Olive/AMDD/ATnT/server/GetContent.ashx?For=FlashPageImg&v=120103&DocHRef=AT2/1767/05/24&PageNo="
							+i
							+"&Resolution=200&FileKind=TextLayer&RepFormat=1.1&ImageExt=png").openStream()){
				Files.copy(in, Paths.get("E:\\Automation\\ARIZONA_YELLOW_PAGES\\images\\"
						+i
						+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/* @driver Selenium WebDriver
	 * @text   Text for which to wait
	 */
	public static void waitForImage(WebDriver driver) {

		By byText = By.xpath
				//				("//select["+
				//		        "[@id='ddlDistrict']/option[@value='"+districtValue+"']"+
				//		        " and "+
				//				" [@id='ddlBlock']/option[text()='"+blockText+"']"+	
				//				"]");
				("//img[@class='shrinkToFit transparent']");



		WebElement element= new WebDriverWait(driver, 20).until
				(ExpectedConditions.presenceOfElementLocated(byText));
	}

}