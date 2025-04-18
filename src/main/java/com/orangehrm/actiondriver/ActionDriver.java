package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;

public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;

    // Logger shared from BaseClass
    public static final Logger logger = BaseClass.getLogger();

    // Constructor initializes WebDriver and explicit wait time
    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        int explicitWait = Integer.parseInt(BaseClass.getprop().getProperty("explicitwait", "30"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        logger.info("ActionDriver initialized with wait: " + explicitWait + "s.");
    }

    // ===============================
    // Click on an element after wait
    // ===============================
    public void click(By by) {
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            logger.info("Clicked element: " + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to click: " + by, e);
        }
    }

    // =====================================
    // Enter text into an input element
    // =====================================
    public void enterText(By by, String value) {
        try {
            waitForElementVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Entered text: '" + value + "' in " + by);
        } catch (Exception e) {
            logger.error("Text entry failed for: " + by, e);
        }
    }

    // ========================================
    // Get text from an element after wait
    // ========================================
    public String getText(By by) {
        try {
            waitForElementVisible(by);
            String text = driver.findElement(by).getText();
            logger.info("Text from element " + by + ": " + text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from " + by, e);
            return "";
        }
    }

    // =================================================
    // Compare element text with expected text
    // =================================================
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementVisible(by);
            String actual = driver.findElement(by).getText();
            boolean match = actual.equals(expectedText);
            logger.info("Comparing text: actual='" + actual + "' expected='" + expectedText + "'");
            return match;
        } catch (Exception e) {
            logger.error("Text comparison failed for " + by, e);
            return false;
        }
    }

    // =============================================
    // Check if an element is displayed or not
    // =============================================
    public boolean isDisplayed(By by) {
        try {
            waitForElementVisible(by);
            boolean visible = driver.findElement(by).isDisplayed();
            logger.info("Element visibility: " + by + " => " + visible);
            return visible;
        } catch (Exception e) {
            logger.error("Visibility check failed for " + by, e);
            return false;
        }
    }

    // ==========================================
    // Wait until the page is fully loaded
    // ==========================================
    public void waitForPageLoad(int timeoutInSec) {
        try {
            wait.withTimeout(Duration.ofSeconds(timeoutInSec)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete")
            );
            logger.info("Page fully loaded.");
        } catch (Exception e) {
            logger.error("Page load timeout after " + timeoutInSec + " seconds.", e);
        }
    }

    // =============================================
    // Scroll to a specific element using JS
    // =============================================
    public void scrollToElement(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to: " + by);
        } catch (Exception e) {
            logger.error("Scroll failed for: " + by, e);
        }
    }

    // ============================================================
    // Highlight an element with the provided color (for debugging)
    // ============================================================
    public void highlightElement(By by, String color) {
        try {
            waitForElementVisible(by);
            WebElement element = driver.findElement(by);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid " + color + "'", element);
            logger.info("Element highlighted with color " + color + ": " + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Failed to highlight element: " + by, e);
        }
    }

    // Overloaded method: default highlight color = yellow
    public void highlightElement(By by) {
        highlightElement(by, "yellow");
    }

    // ===============================
    // Wait for element to be clickable
    // ===============================
    private void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.warn("Element not clickable: " + by, e);
        }
    }

    // ===============================
    // Wait for element to be visible
    // ===============================
    private void waitForElementVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.warn("Element not visible: " + by, e);
        }
    }

    // ==================================================
    // Get useful description of the element for logging
    // ==================================================
    public String getElementDescription(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return String.format("Tag: %s, ID: %s, Name: %s, Text: %s",
                    element.getTagName(),
                    element.getAttribute("id"),
                    element.getAttribute("name"),
                    element.getText());
        } catch (Exception e) {
            logger.error("Failed to get element description: " + locator, e);
            return "Unknown Element";
        }
    }

	public void type(By searchEmployeeField, String empId) {
		// TODO Auto-generated method stub
		
	}
}
