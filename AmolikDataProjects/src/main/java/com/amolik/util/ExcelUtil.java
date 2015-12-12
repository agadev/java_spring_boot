package com.amolik.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

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
    /**
     *  Generates Excel with given formatFile with contains jxls based sql query.
     *  The fields in query are accessed using rm prefix. eg rm.price,rm.qty etc.
     *  @param excelTemplateFileName - Template excel file which contains output format.
     *  @param excelDestFileName - Full Name including path of the output excel file.
     *  @param businessDate - Formatted business date string. 
     *  @parma connectionType - Either oracle or db2.
     */
//    private void generateExcel(
//	    String excelTemplateFileName,
//	    String excelDestFileName,
//	    String businessDate,
//	    String connectionType) 
//    throws IOException {
//
//	Connection dbConnection = null;
//	try {
//	    
//	    dbConnection = DbUtil.getConnection(connectionType);
//	    Map beans = new HashMap();
//	    ReportManager reportManager = new ReportManagerImpl( dbConnection, beans );
//	    beans.put("rm", reportManager);
//	    java.util.Date currentDate = new java.util.Date(System.currentTimeMillis());
//	    String runDateTime = dateAndTimeFormat.format(currentDate);
//	    beans.put("runDateTime", runDateTime);
//	    beans.put("businessDate", businessDate);
//	    XLSTransformer transformer = new XLSTransformer();
//	    transformer.transformXLS(excelTemplateFileName, beans, excelDestFileName);
//	} catch (SQLException e) {
//	   
//	    e.printStackTrace();
//	}
//
//	finally {
//	    
//	   
//	}
//    }
//  ---------------------------------------------------------------------------
//  ---------------------------------------------------------------------------   
}