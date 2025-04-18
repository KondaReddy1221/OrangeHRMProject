package com.orangehrm.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

/**
 * TestListener implements TestNG's ITestListener interface
 * to integrate ExtentReports with test execution.
 */
public class TestListener extends BaseClass implements ITestListener {

    /**
     * Triggered once before any test starts (i.e., suite level).
     * Initializes ExtentReports instance.
     */
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Suite started: " + context.getName());
        ExtentManager.getReports(); // Creates or returns ExtentReports instance
    }

    /**
     * Triggered when each individual test starts.
     * Creates a new ExtentTest instance for reporting this test.
     */
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        // Create test entry in ExtentReports
        ExtentTest test = ExtentManager.getReports().createTest(testName);
        
        // Set the test instance to ThreadLocal (for parallel support)
        ExtentManager.setTest(test);

        // Log info message to report
        test.info("Test started: " + testName);
    }

    /**
     * Triggered when a test passes.
     * Logs the status and captures screenshot.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        // Log pass status
        ExtentManager.getTest().pass("Test passed: " + testName);
        
        // Capture and attach screenshot
        ExtentManager.logPassWithScreenshot(getDriver(), "Test passed and screenshot captured");
    }

    /**
     * Triggered when a test fails.
     * Logs the failure reason and captures screenshot.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMsg = result.getThrowable().getMessage(); // Error message from exception
        
        // Log fail status and reason
        ExtentManager.getTest().fail("Test failed: " + testName);
        ExtentManager.getTest().fail("Reason: " + errorMsg);
        
        // Capture and attach failure screenshot
        ExtentManager.logFailure(getDriver(), "Failure screenshot attached");
    }

    /**
     * Triggered when a test is skipped.
     * Logs the skip reason (if any).
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        // Log skip status
        ExtentManager.getTest().skip("Test skipped: " + testName);
        ExtentManager.logSkip("Test skipped and logged");
    }

    /**
     * Triggered once all tests in the suite have run.
     * Flushes (writes) the Extent report to disk.
     */
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Suite finished: " + context.getName());
        
        // Write all data to ExtentReports
        ExtentManager.endtest(); // Calls flush()
    }
}
