package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.*;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import com.aventstack.extentreports.ExtentTest;

public class HomePageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;
    private ExtentTest test;

    /**
     * Initializes page objects before each test method.
     * Inherits driver from BaseClass.
     */
    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }
    @Test
    public void searchEmployeeById() {
        // Navigate to PIM tab
        homePage.clickPIM();
        
        // Search for employee ID 1436
        String[] employeeDetails = homePage.searchEmployeeById("1436");
        
        // Verify the employee details
        Assert.assertNotNull(employeeDetails);
        Assert.assertEquals(employeeDetails[0], "Employee Name");  // Expected employee name
        Assert.assertEquals(employeeDetails[1], "1436");  // Expected employee ID
    }

    /**
     * Test to verify if OrangeHRM logo is visible on the home page after login.
     */
    @Test(description = "Verify OrangeHRM logo is displayed after login")
    public void verifyOrangeHrmLogo() {
        // Start and register the Extent Report test
        test = ExtentManager.getReports().createTest("verifyOrangeHrmLogo", "Verify OrangeHRM logo is displayed after login");
        ExtentManager.setTest(test);

        try {
            test.info("Logging in with valid credentials: admin/admin123");
            loginPage.login("admin", "admin123");

            test.info("Verifying visibility of OrangeHRM logo on homepage");
            boolean logoVisible = homePage.verifyOrangeHrmLogo();

            // Assertion with custom error message
            Assert.assertTrue(logoVisible, "❌ Logo is not visible after login");

            // Log success with screenshot
            ExtentManager.logPassWithScreenshot(getDriver(), "✅ Logo is displayed successfully");

        } catch (AssertionError ae) {
            // Log failure with screenshot on assertion errors
            ExtentManager.logFailure(getDriver(), "❌ Assertion failed: " + ae.getMessage());
            throw ae; // rethrow to mark test failed
        } catch (Exception e) {
            // Log any other unexpected errors
            ExtentManager.logFailure(getDriver(), "❌ Unexpected error: " + e.getMessage());
            throw e;
        } finally {
            // Flush report after test
            ExtentManager.endtest();
        }
    }
}
