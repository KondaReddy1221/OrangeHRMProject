package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Utility class to manage ExtentReports for Selenium tests.
 * Includes screenshot support (Base64 and file), thread-safe test handling, and report configuration.
 */
public class ExtentManager {

    // Singleton instance of ExtentReports
    private static ExtentReports extent;

    // Thread-safe ExtentTest per thread (parallel execution safe)
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Map to store WebDriver instances per thread
    private static final Map<Long, WebDriver> driverMap = new HashMap<>();

    /**
     * Initializes and returns ExtentReports instance.
     * Ensures report is configured only once (singleton pattern).
     */
    public static ExtentReports getReports() {
        if (extent == null) {
            // Define report path
            String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";

            // Configure Spark (HTML) reporter
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
            reporter.config().setReportName("Automation Test Report");
            reporter.config().setDocumentTitle("OrangeHRM Automation Results");
            reporter.config().setTheme(Theme.DARK); // Options: DARK or STANDARD

            // Attach reporter and add system details
            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
        return extent;
    }

    /**
     * Register the WebDriver instance for the current thread.
     * Needed for thread-safe screenshot capture.
     */
    public static void registerDriver(WebDriver driver) {
        driverMap.put(Thread.currentThread().getId(), driver);
    }

    /**
     * Get the WebDriver for the current thread.
     */
    public static WebDriver getDriver() {
        return driverMap.get(Thread.currentThread().getId());
    }

    /**
     * Assign the ExtentTest instance to the current thread.
     */
    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    /**
     * Retrieve the current thread's ExtentTest instance.
     */
    public static ExtentTest getTest() {
        return test.get();
    }

    /**
     * Log a test PASS with a screenshot (embedded as Base64).
     */
    public static void logPassWithScreenshot(WebDriver driver, String message) {
        String base64Screenshot = captureScreenshotBase64(driver);
        getTest().pass(message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
    }

    /**
     * Log a test FAIL with a screenshot (embedded as Base64).
     */
    public static void logFailure(WebDriver driver, String message) {
        String base64Screenshot = captureScreenshotBase64(driver);
        getTest().fail(message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
    }

    /**
     * Log a test as SKIPPED with a message.
     */
    public static void logSkip(String message) {
        getTest().skip(message);
    }

    /**
     * Capture screenshot as Base64 string (used for embedding in reports).
     */
    public static String captureScreenshotBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Capture and save screenshot to disk under Screenshot folder with timestamp.
     */
    public static void saveScreenshotToDisk(WebDriver driver, String screenshotName) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Format timestamp and path
        String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String path = System.getProperty("user.dir") + "/src/test/resources/Screenshot/" + screenshotName + "_" + timestamp + ".png";

        File dest = new File(path);
        dest.getParentFile().mkdirs(); // Ensure directories exist

        try {
            Files.copy(src.toPath(), dest.toPath());
        } catch (IOException e) {
            e.printStackTrace(); // Could replace with logging framework
        }
    }

    /**
     * Capture screenshot as byte array. Useful for emails or attachments.
     */
    public static byte[] getScreenshotAsBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Finalize and write all test data to the ExtentReport.
     */
    public static void endtest() {
        if (extent != null) {
            extent.flush(); // Generate report HTML file
        }
    }
}
