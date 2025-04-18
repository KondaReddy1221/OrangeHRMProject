package com.orangeHRM.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.MySQLConnectExample;

public class EmployeeSearchTest extends BaseClass {

    //HomePage homePage; 
	ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setup() {
        // Initialize homePage object before each test method
        homePage = new HomePage((WebDriver) driver);
        WebDriver localDriver = new ChromeDriver(); // Example: using ChromeDriver
        driver.set(localDriver);

        // Now you can use driver.get() to navigate
        driver.get().get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
       // driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        
        // Implement the login logic
        homePage.login("Admin", "admin123"); // Example credentials, replace with actual ones
    }

    @Test
    public void searchEmployeeById() {
        // Navigate to PIM tab
        homePage.clickPIM();
        
        // Search for employee ID 1436
        String[] employeeDetails = homePage.searchEmployeeById("1436");
        
        // Verify the employee details
        Assert.assertNotNull(employeeDetails);
        Assert.assertEquals(employeeDetails[1], "1436");  // Expected employee ID

        // Now, get the employee details from the database to compare
        String dbEmployeeName = MySQLConnectExample.getEmployeeNameFromDB("1436");
        Assert.assertEquals(employeeDetails[0], dbEmployeeName);  // Compare with DB name
    }
}
