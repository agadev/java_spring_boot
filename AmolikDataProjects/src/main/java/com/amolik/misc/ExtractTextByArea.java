/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amolik.misc;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

/**
 * This is an example on how to extract text from a specific area on the PDF document.
 *
 * @author Ben Litchfield
 */
public final class ExtractTextByArea
{
    private ExtractTextByArea()
    {
        //utility class and should not be constructed.
    }


    /**
     * This will print the documents text in a certain area.
     *
     * @param args The command line arguments.
     *
     * @throws IOException If there is an error parsing the document.
     */
    public static void main( String[] args ) throws IOException
    {
    	//args[0]= "E:\\Automation\\uphillit\\Fiscal_demo_data.pdf";
//        if( args.length != 1 )
//        {
//            usage();
//        }
//        else
//        {
            PDDocument document = null;
            try
            {
                document = PDDocument.load( new File("E:\\Automation\\uphillit\\Fiscal_demo_data.pdf") );
                int numberOfPages=document.getNumberOfPages();
                if(numberOfPages>0){
                	
                	PDPage page = (PDPage)document.getPages().get( 0 );
                	System.out.println(page.getContents());
                }
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition( true );
                Rectangle rect = new Rectangle( 3, 1, 600, 6000 );
                stripper.addRegion( "class1", rect );
                PDPage firstPage = document.getPage(0);
                stripper.extractRegions( firstPage );
                System.out.println( "Text in the area:" + rect );
                System.out.println( stripper.getTextForRegion( "class1" ) );
            }
            finally
            {
                if( document != null )
                {
                    document.close();
                }
            }
 //       }
    }

    /**
     * This will print the usage for this document.
     */
    private static void usage()
    {
        System.err.println( "Usage: java " + ExtractTextByArea.class.getName() + " <input-pdf>" );
    }

}
