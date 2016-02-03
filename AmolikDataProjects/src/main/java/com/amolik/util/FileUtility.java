package com.amolik.util;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.amolik.data.FiscalRecord;
import com.amolik.formfiling.FiscalProcessor;

public class FileUtility {

	private static final Logger logger = Logger.getLogger(FileUtility.class);

	int maxWidth          = 0;
	int maxWidthHt    =0;

	int maxHeight    = 115;
	int maxHeightWd  =0;
	String maxWidthFilename="";
	String maxHeightFilename="";


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String baseDir = "E:\\FISCAL_DATA\\FEB2016";
		//findMaxWidthImage(baseDir);
		//findMaxWidthImageUsingNio(Paths.get(baseDir));
		FileUtility fileUtil = new FileUtility();
		fileUtil.getRecursiveFileList(Paths.get(baseDir));

		if(logger.isInfoEnabled()) {
			logger.info("Max Width Image="+fileUtil.maxWidthFilename
					+"|Height="+fileUtil.maxWidthHt
					+"|Width="+fileUtil.maxWidth);

//			logger.info("Max Height Image="+fileUtil.maxHeightFilename
//					+"|Height="+fileUtil.maxHeight
//					+"|Width="+fileUtil.maxHeightWd);
		}
	}

	public static void writeDelimitedRecordsToFile(String outputFilePath,
			String outputFileDelimiter,List<FiscalRecord> recordList) {

		if(logger.isInfoEnabled()){

			logger.info("Writing record to file="+outputFilePath);
		}
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {

			for (FiscalRecord record : recordList) {
				// System.out.println(name);
				writer.write(record.getImageFileName()+"\n"
						+record.getSrNo()
						+outputFileDelimiter+record.getEmpIdNo()
						+outputFileDelimiter+record.getOccuranceNo()
						+outputFileDelimiter+record.getLoanFileNo()
						+outputFileDelimiter+record.getLoanAmount()
						+outputFileDelimiter+record.getRateOfInterest()
						+outputFileDelimiter+record.getTenure()+outputFileDelimiter+"\n"
						+record.getTotalLoan()
						+outputFileDelimiter+record.getEmi()
						+outputFileDelimiter+record.getOtherLoans()
						+outputFileDelimiter+record.getInitials()
						+outputFileDelimiter+record.getEmpName()+outputFileDelimiter+"\n"
						+record.getAddress()
						+outputFileDelimiter+record.getCity()
						+outputFileDelimiter+record.getState()
						+outputFileDelimiter+record.getZip()
						+outputFileDelimiter+record.getCountry()+outputFileDelimiter+"\n"
						+record.getContactMode()
						+outputFileDelimiter+record.getMaritalStatus()
						+outputFileDelimiter+record.getRefName()
						+outputFileDelimiter+record.getYearsOfEmployment()
						+outputFileDelimiter+record.getDesignation()
						+outputFileDelimiter+record.getDepartment()+outputFileDelimiter+"\n"
						+record.getPerformance()
						+outputFileDelimiter+record.getBasicSalary()
						+outputFileDelimiter+record.getCenterName()
						+outputFileDelimiter+record.getIssuerBank()+outputFileDelimiter+"\n"
						+record.getCarrierName()
						+outputFileDelimiter+record.getEisCode()+outputFileDelimiter+"\n"
						+"================================================================================================="+"\n"
						);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		 
	}


	public List<Path> getRecursiveFileList(Path path) throws IOException {

		Deque<Path> stack = new ArrayDeque<Path>();
		final List<Path> files = new LinkedList<>();

		stack.push(path);

		while (!stack.isEmpty()) {
			DirectoryStream<Path> stream = Files.newDirectoryStream(stack.pop());

			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					logger.info("Traversing directory="+entry.getFileName());
					stack.push(entry);
				}
				else {
					if(entry!=null && entry.toString().contains("jpeg")){
						File file = entry.toFile();
						files.add(entry);
						//logger.info(entry);
						BufferedImage bimg = ImageIO.read(file);
						//logger.info(file.getName());
						if(maxWidth<bimg.getWidth()){

							maxWidth        = bimg.getWidth();
							maxWidthHt         = bimg.getHeight();
							maxWidthFilename = file.getName();
						}

						if(maxHeight<bimg.getHeight()){

							//maxHeight         = bimg.getHeight();
							//maxHeightWd        = bimg.getWidth();
							//maxHeightFilename = file.getName();
							if(logger.isInfoEnabled()){
								
								logger.info("Height>115 Image="+file.getName()
								+"|Height="+bimg.getHeight()
								+"|Width="+bimg.getWidth());
							}
						}
					}
				}
			}
			stream.close();
		}

		return files;
	}


	public void findMaxWidthImage(String baseDir) {
		File folder = new File(baseDir);
		File[] listOfFiles = folder.listFiles();


		try {
			for (File file : listOfFiles) {
				if (file.isFile()) {

					BufferedImage bimg = ImageIO.read(file);
					//logger.info(file.getName());
					if(maxWidth<bimg.getWidth()){

						maxWidth        = bimg.getWidth();
						maxWidthHt         = bimg.getHeight();
						maxWidthFilename = file.getName();
					}

					if(maxHeight<bimg.getHeight()){

						maxHeight         = bimg.getHeight();
						maxHeightWd        = bimg.getWidth();
						maxHeightFilename = file.getName();
						if(logger.isInfoEnabled()) {

							logger.info("Max Height Image="+maxHeightFilename
									+"|Height="+maxHeight
									+"|Width="+maxHeightWd);
						}
					}

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Max Width Image="+maxWidthFilename
				+"|Height="+maxWidthHt
				+"|Width="+maxWidth);

		logger.info("Max Height Image="+maxHeightFilename
				+"|Height="+maxHeight
				+"|Width="+maxHeightWd);
	}

}
