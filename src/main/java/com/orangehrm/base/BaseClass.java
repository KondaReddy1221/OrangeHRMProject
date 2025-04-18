package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

    // Load configuration properties
    protected static Properties prop;

    // Thread-safe WebDriver and ActionDriver instances
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

    // Logger for logging execution details
    private static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    // =============================
    // Runs once before entire suite
    // =============================
    @BeforeSuite
    public void loadConfig() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/Config.properties");
        prop.load(fis);
        logger.info("Config.properties file loaded successfully.");
    }

    // ==================================
    // Runs before every @Test method
    // ==================================
    @BeforeMethod
    public synchronized void setUp() {
        logger.info("Starting test setup for class: " + this.getClass().getSimpleName());

        launchBrowser();         // Step 1: Launch browser
        configureBrowser();      // Step 2: Setup browser
        staticWait(3);           // Step 3: Small wait (optional)

        // Register WebDriver with Extent Report
        ExtentManager.registerDriver(getDriver());
        ExtentManager.getReports();

        // Initialize ActionDriver if not already set
        if (actionDriver.get() == null) {
            synchronized (this) {
                if (actionDriver.get() == null) {
                    actionDriver.set(new ActionDriver(driver.get()));
                    logger.info("ActionDriver instance created for thread.");
                }
            }
        }
    }

    // ==================================
    // Launch browser based on config
    // ==================================
    private synchronized void launchBrowser() {
        String browser = prop.getProperty("browser", "chrome");

        switch (browser.toLowerCase()) {
            case "chrome":
                driver.set(new ChromeDriver());
                logger.info("Chrome browser launched.");
                break;
            case "firefox":
                driver.set(new FirefoxDriver());
                logger.info("Firefox browser launched.");
                break;
            case "edge":
                driver.set(new EdgeDriver());
                logger.info("Edge browser launched.");
                break;
            default:
                logger.error("Unsupported browser: " + browser);
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    // ==================================
    // Setup browser settings and open URL
    // ==================================
    private synchronized void configureBrowser() {
        WebDriver currentDriver = driver.get();
        currentDriver.manage().window().maximize();
        currentDriver.manage().deleteAllCookies();

        int waitTime = Integer.parseInt(prop.getProperty("implicitWait", "10"));
        currentDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTime));

        try {
            String url = prop.getProperty("url");
            currentDriver.get(url);
            logger.info("Navigated to URL: " + url);
        } catch (Exception e) {
            logger.error("Failed to open URL", e);
        }
    }

    // ==================================
    // Runs after each @Test method
    // ==================================
    @AfterMethod
    public synchronized void tearDown() {
        if (driver.get() != null) {
            try {
                driver.get().quit();
                logger.info("WebDriver quit successfully.");
            } catch (Exception e) {
                logger.error("Unable to quit WebDriver", e);
            }
        }
        // Cleanup ThreadLocal instances
        driver.remove();
        actionDriver.remove();
        logger.info("Test teardown completed.");
    }

    // ================================
    // Static wait using LockSupport
    // (Avoid in real frameworks)
    // ================================
    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
        logger.info("Static wait for " + seconds + " seconds.");
    }

    // ==========================
    // Getter methods
    // ==========================
    public static WebDriver getDriver() {
        return driver.get();
    }

    public static ActionDriver getActionDriver() {
        return actionDriver.get();
    }

    public static Properties getprop() {
        return prop;
    }

    public static Logger getLogger() {
        return logger;
    }
}
