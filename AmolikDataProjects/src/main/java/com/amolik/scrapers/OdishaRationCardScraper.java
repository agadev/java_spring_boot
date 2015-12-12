package com.amolik.scrapers;


import com.amolik.util.*;
import com.amolik.data.OdishaRationCardBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OdishaRationCardScraper {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OdishaRationCardScraper.class);
	public static ArrayList<String> districtsNameList = new ArrayList<String>();
	public static String baseDir =null;
	public static String excelTemplateFileName =null;
	public static String excelFileExtension=null;


	public static void main(String[] args) throws Exception {;


	// The Firefox driver supports javascript 
	WebDriver driver = new FirefoxDriver();


	driver.get(AmolikProperties.getProperty("odisha_ration.startUrl"));
	baseDir=AmolikProperties.getProperty("odisha_ration.baseDir");
	excelTemplateFileName=AmolikProperties.getProperty("odisha_ration.excelTemplateFileName");
	excelFileExtension=AmolikProperties.getProperty("odisha_ration.excelExtension");

	if(logger.isInfoEnabled()){

		logger.info("startUrl="+AmolikProperties.getProperty("odisha_ration.startUrl"));
	}

	processAllDistricts(driver );

	if(driver!=null){

		driver.close();

	}
	}

	public static void processAllDistricts(WebDriver driver ) throws InterruptedException{

		Select districtSelect = new Select(driver.findElement(By.name(Constants.DDL_DISTRICT)));
		List<WebElement> districtsList=districtSelect.getOptions();


		// Prepare List of names of districts
		for(WebElement districtElement:districtsList){

			districtsNameList.add(districtElement.getText());
		}

		int districtSize = districtsList.size();

		// Remove this during production
		//districtSize=2;
		int startDistrictIndex=new Integer(
				AmolikProperties.getProperty("odisha_ration.startDistrictIndex")).intValue();

		for (int districtIndex=startDistrictIndex;districtIndex<districtSize;districtIndex++){

			processDistrict(driver, districtIndex);
			//TimeUnit.SECONDS.sleep(3);
		} 
	}

	public static void processDistrict(WebDriver driver,int districtIndex ){


		Select districtSelect = new Select(driver.findElement(By.name(Constants.DDL_DISTRICT)));
		districtSelect.selectByIndex(districtIndex);
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int blockSize=getRefreshedBlockSelect(driver,districtIndex).getOptions().size();

		if (logger.isDebugEnabled()) {
			logger.debug("processDistrict blockSize=" + blockSize); //$NON-NLS-1$
		}


		// Remove during production
		//blockSize =2;

		int startBlockIndex=new Integer(
				AmolikProperties.getProperty("odisha_ration.startBlockIndex")).intValue();
		ArrayList<OdishaRationCardBean> beanList = new ArrayList();

		for (int blockIndex=startBlockIndex;blockIndex<blockSize;blockIndex++){

			Select blockSelect = getRefreshedBlockSelect(driver,districtIndex);
			List<WebElement>blocksList=blockSelect.getOptions();
			WebElement block = blocksList.get(blockIndex);

			
			if (logger.isDebugEnabled()) {

				logger.debug("processDistrict(WebDriver, int) - block=" + block.getText()); //$NON-NLS-1$
			}

			String blockValue = block.getAttribute(Constants.LOWERCASE_VALUE);

			if (logger.isDebugEnabled()) {

				logger.debug("processDistrict(WebDriver, int) - " 
						+ districtIndex + "|" + block.getAttribute(Constants.LOWERCASE_VALUE) 
						+ "|" + block.getText()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			processBlock(driver, districtIndex, blockIndex,blockValue,block.getText(),beanList);
		}

		// Create new Directory if not exists
		String districtName = districtsNameList.get(districtIndex);
		String excelDestDirName = AmolikProperties.getProperty("odisha_ration.excelOutputDir");
		new File(excelDestDirName).mkdirs();

		String excelDestFileName = excelDestDirName+System.getProperty("file.separator")
		+districtName+excelFileExtension;

		if (logger.isInfoEnabled()) {

			logger.info("writing to excel "+districtIndex + "|" + districtName 
					+"| toal recordCount=" 
					+ (beanList.size())); 
		}
		// Now write to excel
		try {
			ExcelUtil.generateExcelFromBean(beanList,
					excelTemplateFileName, excelTemplateFileName, excelDestFileName, "rationCard");

		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			logger.error("processBlock(WebDriver, int, int, String, String)", e); //$NON-NLS-1$
		}

	}


	public static Select getRefreshedBlockSelect(WebDriver driver,int districtIndex){

		Select districtSelect = new Select(driver.findElement(By.name(Constants.DDL_DISTRICT)));
		districtSelect.selectByIndex(districtIndex);

		if (logger.isDebugEnabled()) {

			logger.debug("getRefreshedBlockSelect(WebDriver, int) - selected district="
					+ districtSelect.getAllSelectedOptions().toString()); //$NON-NLS-1$
		}


		waitForBlock(driver,getDistrictValueFromIndex(districtIndex),Constants.SELECT_0);
		Select blockSelect = new Select(driver.findElement(By.name(Constants.DDL_BLOCK)));

		return blockSelect;
	}

	public static String getDistrictValueFromIndex(int districtIndex){

		StringBuffer districtValueBuffer = new StringBuffer();
		if(districtIndex>0 && districtIndex<10){

			districtValueBuffer.append(Constants.ZERO);
		}

		districtValueBuffer.append(districtIndex);
		return districtValueBuffer.toString();


	}


	public static void processBlock(WebDriver driver, int districtIndex, 
			int blockIndex,String blockValue,String blockName,ArrayList<OdishaRationCardBean> beanList) {
		// Select block based upon index

		Select blockSelect = getRefreshedBlockSelect(driver,districtIndex);
		blockSelect.selectByIndex(blockIndex);
		String districtName = districtsNameList.get(districtIndex);

		//		// Create new Directory if not exists
		//		String excelDestDirName = AmolikProperties.getProperty("odisha_ration.excelOutputDir")
		//				+System.getProperty("file.separator")
		//				+districtName;
		//
		//		new File(excelDestDirName).mkdirs();
		//
		//		String excelDestFileName = excelDestDirName
		//				+Constants.BACK_SLASH+blockName+".xlsx";

		driver.findElement(By.name(Constants.BTN_SHOW)).click();
		waitForSubmit(driver,Constants.NO_DETAILS_FOUND,blockValue);


		int rowCount = driver.findElements(By.xpath("//table[@id='gvDist']/tbody/tr")).size();

		if (logger.isInfoEnabled()) {

			logger.info(districtIndex + "|" + districtName 
					+"|" +blockIndex+"|"+ blockName+"| recordCount=" 
					+ (rowCount - 3)); //$NON-NLS-1$
		}

		// Remove during production
		//rowCount=7;

		if(rowCount>2){

			
			for(int i=2;i<rowCount-1;i++) {

				List<WebElement> columns = driver.findElements(By.xpath("//table[@id='gvDist']/tbody/tr["+
						(i+1)+
						"]/td")); 

				StringBuffer columnBuffer = new StringBuffer();

				// Temporary select max columns to process
				int columnToProcess = 5;
				columnToProcess = Math.min(columnToProcess, columns.size());

				OdishaRationCardBean bean = new OdishaRationCardBean();
				setBean(columns, columnToProcess, bean);
				beanList.add(bean);

			}
		}

	}

	public static void setBean(List<WebElement> columns, int columnToProcess, OdishaRationCardBean bean) {

		int currentColumn=0;

		// Set zero element
		if(currentColumn<columnToProcess){

			bean.setSiNo(StringUtility.getLowerCaseString(
					columns.get(currentColumn).getText()));
			currentColumn++;
		}

		// Set First Element
		if(currentColumn<columnToProcess){

			bean.setGpOrWardNo(StringUtility.getLowerCaseString(
					columns.get(currentColumn).getText()));
			currentColumn++;
		}

		// Set 2nd  Element
		if(currentColumn<columnToProcess){

			bean.setVillageOrLocality(StringUtility.getLowerCaseString(
					columns.get(currentColumn).getText()));
			currentColumn++;
		}

		// Set 3rd element
		if(currentColumn<columnToProcess){

			bean.setHamletOrPlotNo(StringUtility.getLowerCaseString(
					columns.get(currentColumn).getText()));
			currentColumn++;
		}

		// Set 4th element
		if(currentColumn<columnToProcess){

			bean.setLicenseHolderName(StringUtility.getLowerCaseString(
					columns.get(currentColumn).getText()));
			currentColumn++;
		}
	}

	/* @driver Selenium WebDriver
	 * @text   Text for which to wait
	 */
	public static void waitForBlock(WebDriver driver,String districtValue,String blockText) {

		By byText = By.xpath
				//				("//select["+
				//		        "[@id='ddlDistrict']/option[@value='"+districtValue+"']"+
				//		        " and "+
				//				" [@id='ddlBlock']/option[text()='"+blockText+"']"+	
				//				"]");
				("//select[@id='ddlBlock']/option[text()='"+blockText+"']");


		if (logger.isDebugEnabled()) {

			logger.debug("waitForBlock("
					+ "WebDriver, String, String) - xpath by string=" 
					+ byText.toString()); //$NON-NLS-1$
		}
		WebElement element= new WebDriverWait(driver, 20).until
				(ExpectedConditions.presenceOfElementLocated(byText));
	}

	public static void waitForSubmit(WebDriver driver,String emptyText,
			String dataText) {


		By byText = By.xpath(
				"//table[@id='gvDist']/tbody/tr[1]/td[1][text()='"+
						emptyText+"']"
						+
						"|" +
						"//table[@id='gvDist']/tbody/tr[3]/td[6][contains(text(),'"+dataText+"')]"
				);

		WebElement element= new WebDriverWait(driver, 10).until
				(ExpectedConditions.presenceOfElementLocated(byText));
	}
}
