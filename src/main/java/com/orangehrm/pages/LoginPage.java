package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.orangehrm.actiondriver.ActionDriver;

/**
 * This class represents the Login Page of OrangeHRM.
 * It contains locators and actions that can be performed on the login screen.
 */
public class LoginPage {

    // ActionDriver instance to perform reusable actions like click, sendText, etc.
    private ActionDriver actionDriver;

    // Locators for login page elements
    private By userNameField = By.name("username"); // Username input field
    private By passwordField = By.cssSelector("input[type='password']"); // Password input field
    private By loginButton = By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[3]/button"); // Login button
    private By errorMessage = By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/div/div[1]/div[1]/p"); // Error message element

    /**
     * Constructor: Initializes ActionDriver with the WebDriver instance.
     * @param driver WebDriver passed from test or base class.
     */
    public LoginPage(WebDriver driver) {
        this.actionDriver = new ActionDriver(driver);
    }

    /**
     * This method performs the login action.
     * @param username Username to enter
     * @param password Password to enter
     */
    public void login(String username, String password) {
        actionDriver.enterText(userNameField, username); // Type username
        actionDriver.enterText(passwordField, password); // Type password
        actionDriver.click(loginButton); // Click on login button
    }

    /**
     * Checks whether the error message is displayed on login failure.
     * @return true if error message is visible, false otherwise.
     */
    public boolean isErrorMessageDisplayed() {
        return actionDriver.isDisplayed(errorMessage);
    }

    /**
     * Returns the text of the error message if displayed.
     * @return Error message string
     */
    public String getErrorMessageText() {
        return actionDriver.getText(errorMessage);
    }

    /**
     * Verifies if the error message matches the expected string.
     * @param expectedError Expected error message text
     * @return true if the actual message matches expected, else false
     */
    public boolean verifyErrorMessage(String expectedError) {
        return actionDriver.compareText(errorMessage, expectedError);
    }
}
