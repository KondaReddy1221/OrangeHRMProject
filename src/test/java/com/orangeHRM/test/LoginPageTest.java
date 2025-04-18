package com.orangeHRM.test;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExcelDataProvider;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;
    private ExtentTest test;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    // Optional visual check - highlight fields
    @Test(description = "Highlight login input fields")
    public void highlightLoginFields() {
        getDriver().get(getprop().getProperty("url"));
        ActionDriver actionDriver = new ActionDriver(getDriver());
        actionDriver.highlightElement(By.name("username"));
        actionDriver.highlightElement(By.cssSelector("input[type='password']"), "red");
    }

    @Test(description = "Valid login test with hardcoded credentials")
    public void verifyValidLoginTest() {
        test = ExtentManager.getReports().createTest("verifyValidLoginTest", "Valid Login Test");
        ExtentManager.setTest(test);

        try {
            loginPage.login("Admin", "admin123");
            boolean visible = homePage.isAdminTabVisible();
            Assert.assertTrue(visible, "❌ Admin tab not visible after login.");
            ExtentManager.logPassWithScreenshot(getDriver(), "✅ Login successful");
            homePage.Logout();
        } catch (AssertionError | Exception e) {
            ExtentManager.logFailure(getDriver(), e.getMessage());
            throw e;
        }
    }

    @Test(description = "Invalid login test")
    public void inValidLoginTest() {
        test = ExtentManager.getReports().createTest("inValidLoginTest", "Invalid Login Test");
        ExtentManager.setTest(test);

        try {
            loginPage.login("admin", "wrongpass");
            boolean errorDisplayed = loginPage.verifyErrorMessage("Invalid credentials");
            Assert.assertTrue(errorDisplayed, "❌ Error message not shown.");
            ExtentManager.logPassWithScreenshot(getDriver(), "✅ Error message correctly displayed.");
        } catch (AssertionError | Exception e) {
            ExtentManager.logFailure(getDriver(), e.getMessage());
            throw e;
        }
    }

    // Data-driven test for login using Excel data
    @Test(dataProvider = "loginData")
    public void loginTest(String username, String password) {
        test = ExtentManager.getReports().createTest("Login Test - Username: " + username, "Data-driven login");
        ExtentManager.setTest(test);

        try {
            loginPage.login(username, password);
            if (homePage.isAdminTabVisible()) {
                ExtentManager.logPassWithScreenshot(getDriver(), "✅ Login success for: " + username);
                homePage.Logout();
            } else {
                boolean error = loginPage.verifyErrorMessage("Invalid credentials");
                Assert.assertTrue(error, "❌ No error shown for invalid login");
                ExtentManager.logPassWithScreenshot(getDriver(), "✅ Login failed correctly for: " + username);
            }
        } catch (Exception e) {
            ExtentManager.logFailure(getDriver(), "❌ Test failed: " + e.getMessage());
            throw e;
        }
    }

    // Data provider to fetch login data from Excel
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/TestData/TestData.xlsx";
        List<String[]> data = ExcelDataProvider.readExcelData(filePath, "Sheet1");

        // Convert List<String[]> to Object[][] format for TestNG
        Object[][] testData = new Object[data.size()][]; 
        for (int i = 0; i < data.size(); i++) {
            testData[i] = data.get(i);
        }

        // Debugging output (optional)
        System.out.println("Test Data: ");
        for (Object[] row : testData) {
            System.out.println(Arrays.toString(row));
        }
        return testData;
    }

    @AfterMethod
    public void tearDownTest() {
        ExtentManager.endtest();
    }
}
