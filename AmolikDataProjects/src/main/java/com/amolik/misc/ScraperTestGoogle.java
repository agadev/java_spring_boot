package com.amolik.misc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class ScraperTestGoogle  {
    public static void main(String[] args) {
        // Create a new instance of the html unit driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
        WebDriver driver = new HtmlUnitDriver();

        // And now use this to visit Google
        driver.get("http://cg.nic.in/nagarsuraj/Reports/Curr_Dept_Total_PC.aspx?dept=D110046&dist=11&did=02&st=C&bd=B028&sb=S001");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("ContentPlaceHolder1_GridView1"));

        // Enter something to search for
         String text =element.getText();

        // Now submit the form. WebDriver will find the form for us from the element
        //element.submit();

        // Check the title of the page
        System.out.println("Page title is: " + text);

        driver.quit();
    }
}