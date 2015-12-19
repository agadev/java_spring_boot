package com.amolik.util;

import java.awt.image.BufferedImage;
import java.io.File;
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

public class FileUtility {

	int maxWidth          = 0;
	int maxWidthHt    =0;

	int maxHeight    = 0;
	int maxHeightWd  =0;
	String maxWidthFilename="";
	String maxHeightFilename="";
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String baseDir = "E:\\Automation\\KOMAL\\FISCAL\\RAW_DATA";
		//findMaxWidthImage(baseDir);
		//findMaxWidthImageUsingNio(Paths.get(baseDir));
		FileUtility fileUtil = new FileUtility();
		fileUtil.getRecursiveFileList(Paths.get(baseDir));
		System.out.println("Max Width Image="+fileUtil.maxWidthFilename
				+"|Height="+fileUtil.maxWidthHt
				+"|Width="+fileUtil.maxWidth);

		System.out.println("Max Height Image="+fileUtil.maxHeightFilename
				+"|Height="+fileUtil.maxHeight
				+"|Width="+fileUtil.maxHeightWd);
	}


	
	public List<Path> getRecursiveFileList(Path path) throws IOException {
		
	    Deque<Path> stack = new ArrayDeque<Path>();
	    final List<Path> files = new LinkedList<>();

	    stack.push(path);

	    while (!stack.isEmpty()) {
	        DirectoryStream<Path> stream = Files.newDirectoryStream(stack.pop());
	        for (Path entry : stream) {
	            if (Files.isDirectory(entry)) {
	                stack.push(entry);
	            }
	            else {
	            	
	            	File file = entry.toFile();
	                files.add(entry);
	                System.out.println(entry);
	                BufferedImage bimg = ImageIO.read(file);
					//System.out.println(file.getName());
					if(maxWidth<bimg.getWidth()){

						maxWidth        = bimg.getWidth();
						maxWidthHt         = bimg.getHeight();
						maxWidthFilename = file.getName();
					}

					if(maxHeight<bimg.getHeight()){

						maxHeight         = bimg.getHeight();
						maxHeightWd        = bimg.getWidth();
						maxHeightFilename = file.getName();
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
					//System.out.println(file.getName());
					if(maxWidth<bimg.getWidth()){

						maxWidth        = bimg.getWidth();
						maxWidthHt         = bimg.getHeight();
						maxWidthFilename = file.getName();
					}

					if(maxHeight<bimg.getHeight()){

						maxHeight         = bimg.getHeight();
						maxHeightWd        = bimg.getWidth();
						maxHeightFilename = file.getName();
					}

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Max Width Image="+maxWidthFilename
				+"|Height="+maxWidthHt
				+"|Width="+maxWidth);

		System.out.println("Max Height Image="+maxHeightFilename
				+"|Height="+maxHeight
				+"|Width="+maxHeightWd);
	}

}
