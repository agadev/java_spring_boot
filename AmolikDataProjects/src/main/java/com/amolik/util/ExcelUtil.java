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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

	
}