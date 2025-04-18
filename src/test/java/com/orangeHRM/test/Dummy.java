package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.aventstack.extentreports.ExtentTest;

public class Dummy extends BaseClass {

    private ExtentTest test;

    @Test(description = "Dummy test to verify title of OrangeHRM login page")
    public void dummyTest() {
        // Start and register ExtentTest for reporting
        test = ExtentManager.getReports().createTest("dummyTest", "Verify title of OrangeHRM login page");
        ExtentManager.setTest(test);

        test.info("Navigating to OrangeHRM login page");

        try {
            // Fetch the page title
            String title = driver.get().getTitle();

            test.info("Captured page title: " + title);

            // Validate the title using TestNG assertion
            Assert.assertEquals(title, "OrangeHRM", "Test failed: Title mismatch");

            // Log success to ExtentReports with screenshot
            ExtentManager.logPassWithScreenshot(getDriver(), "Title matched successfully.");

            // Optional: log to console
            System.out.println("Test passed = " + title);
        } catch (AssertionError e) {
            // Log failure to ExtentReports with screenshot
            ExtentManager.logFailure(getDriver(), "Assertion failed: " + e.getMessage());
            throw e; // Rethrow to mark test as failed in TestNG
        } catch (Exception ex) {
            // Log unexpected exceptions
            ExtentManager.logFailure(getDriver(), "Unexpected exception occurred: " + ex.getMessage());
            throw ex;
        } finally {
            // Ensure the report is flushed even if exception occurs
            ExtentManager.endtest();
        }
    }
}
