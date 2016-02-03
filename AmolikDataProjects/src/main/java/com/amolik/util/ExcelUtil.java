package com.amolik.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.amolik.data.FiscalCorrectionField;
import com.amolik.data.FiscalRecord;

import net.sf.jett.transform.ExcelTransformer;

//import net.sf.jxls.report.ReportManager;
//import net.sf.jxls.report.ReportManagerImpl;
//import net.sf.jxls.transformer.XLSTransformer;

/**
 *  This class generates excel file based on given excel template file.
 *  You need to provide ArrayList of beans which can be accessed
 *  in template spreadsheet. Once program runs , all the fields
 *  in template spreadsheet are populated by actual value from beans.
 * 
 *
 */

public class ExcelUtil {
	public static SimpleDateFormat dateAndTimeFormat = 
			new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"); 

	public static SimpleDateFormat usDateFormat = 
			new SimpleDateFormat("MM/dd/yyyy");


	//  ---------------------------------------------------------------------------
	//  ---------------------------------------------------------------------------
	/**
	 *  Generates Excel with given formatFile.
	 *  @param beanList - Contains List of Data Bean
	 *  @param excelTemplateFileName - Template excel file which contains output format.
	 *  @param excelTemplateFileName - Template excel file which contains output format when no data.
	 *  @param excelDestFileName - Full Name including path of the output excel file.
	 *  @param beanName - Name of the bean used to access fields in spreadsheet.
	 */
	public static void generateExcelFromBean(List beanList,
			String excelTemplateFileName, 
			String noDataExcelTemplateFileName,
			String excelDestFileName,
			String beanName)
					throws IOException, InvalidFormatException {


		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put(beanName, beanList);
		java.util.Date currentDate = new java.util.Date(System.currentTimeMillis());
		String runDateTime = dateAndTimeFormat.format(currentDate);
		beans.put("runDateTime", runDateTime);

		//String businessDateString = usDateFormat.format(businessDate);
		//beans.put("businessDate", businessDateString);
		//beans.put("clientName", clientName);

		ExcelTransformer transformer = new ExcelTransformer();

		// Create excel file with some data
		if (beanList.size()>0) {
			transformer.transform(excelTemplateFileName, excelDestFileName,beans);
		}

		// Check if needs to create excel file using no data template
		else {

			File physicalNoDataExcelTemplateFileName = 
					new File(noDataExcelTemplateFileName);


			// Check if noDataExcelTemplateFileExists, if yes then create
			// excel file using that nodata excel template
			if(physicalNoDataExcelTemplateFileName!=null &&
					physicalNoDataExcelTemplateFileName.exists()) {

				transformer.transform(noDataExcelTemplateFileName, 
						excelDestFileName,beans);
			}

			// Create excel file using regular template
			else {

				transformer.transform(excelTemplateFileName,
						excelDestFileName,beans);
			}

		}
	}

	//  ---------------------------------------------------------------------------
	//  ---------------------------------------------------------------------------
	

	public static Set<String> getExcelContentByColumnIndex(String excelFilePath,
			int columnIndex){
		
		Set<String> columndata = new HashSet<String>(6000);
		try {
			File f = new File(excelFilePath);
			FileInputStream ios = new FileInputStream(f);
			
			// for xlsx use 2 lines below
			//XSSFWorkbook workbook = new XSSFWorkbook(ios);
			//XSSFSheet sheet = workbook.getSheetAt(0);
			
			// for xls use this
			org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(ios);
			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					if(row.getRowNum() > 0){ //To filter column headings
						if(cell.getColumnIndex() == columnIndex){// To match column index
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								
								Path p = Paths.get(cell.getStringCellValue());
								//System.out.println(p.getFileName().toString());
								columndata.add(p.getFileName().toString());
								break;
							}
						}
					}
				}
			}
			ios.close();
			//System.out.println(columndata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columndata;
	}
	//  ---------------------------------------------------------------------------
	//  ---------------------------------------------------------------------------  
	
	public static Map<String,FiscalRecord> getFiscalRecordsFromExcel(String excelFilePath){
		
		Map<String,FiscalRecord> fiscalRecordMap = new TreeMap<String,FiscalRecord>();
		try {
			File f = new File(excelFilePath);
			FileInputStream ios = new FileInputStream(f);
						
			// for xls use this
			org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(ios);
			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			

			while (rowIterator.hasNext()) {
				
				FiscalRecord record = new FiscalRecord();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					if(row.getRowNum() > 0){ //To filter column headings
					
						switch(cell.getColumnIndex()) {
						
						case 0:
							   break;
						case 1:record.setImageFileName(cell.getStringCellValue());
							   break;
							   
						case 2:record.setSrNo(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 3:record.setEmpIdNo(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 4:record.setOccuranceNo(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 5:record.setLoanFileNo(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 6:record.setLoanAmount(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 7:record.setRateOfInterest(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 8:record.setTenure(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 9:record.setTotalLoan(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 10:record.setEmi(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 11:record.setOtherLoans(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 12:record.setInitials(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 13:record.setEmpName(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 14:record.setAddress(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 15:record.setCity(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 16:record.setState(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 17:record.setZip(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 18:record.setCountry(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 19:record.setContactMode(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 20:record.setMaritalStatus(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 21:record.setRefName(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 22:record.setYearsOfEmployment(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 23:record.setDesignation(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 24:record.setDepartment(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 25:record.setPerformance(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 26:record.setBasicSalary(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 27:record.setCenterName(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 28:record.setIssuerBank(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
							   
						case 29:record.setEisCode(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
							   break;
						
						case 30:record.setCarrierName(
								FiscalEncrypterDecrypter.DecryptFiscalText(
								cell.getStringCellValue()));
						        
						        fiscalRecordMap.put(record.getImageFileName(),record);
						        break;
							   
						}
					}
				}
			}
			ios.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fiscalRecordMap;
	}

public static List<FiscalCorrectionField> getCorrectFiscalFieldsFromExcel(String excelFilePath){
		
		List<FiscalCorrectionField> fiscalCorrectionFieldList = 
				                   new ArrayList<FiscalCorrectionField>(6000);
		try {
			File f = new File(excelFilePath);
			FileInputStream ios = new FileInputStream(f);
						
			// for xls use this
			org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(ios);
			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			

			while (rowIterator.hasNext()) {
				
				FiscalCorrectionField record = new FiscalCorrectionField();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					if(row.getRowNum() > 3){ //To filter column headings
					
						switch(cell.getColumnIndex()) {
						
						case 0:
							   break;
						case 1:record.setImageName(
								StringUtility.trim(cell.getStringCellValue()));
							   break;
							   
						case 2:record.setFieldName(
								StringUtility.trim(cell.getStringCellValue()));
							   break;
							   
						case 3:record.setCorrectFieldValue(
							   StringUtility.trim(cell.getStringCellValue()));
						       fiscalCorrectionFieldList.add(record);
							   break;
							   
						case 4:
							  break;
							   
						}
					}
				}
			}
			ios.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fiscalCorrectionFieldList;
	}
	
}