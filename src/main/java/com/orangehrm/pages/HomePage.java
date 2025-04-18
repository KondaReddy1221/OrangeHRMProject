package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.orangehrm.actiondriver.ActionDriver;

/**
 * HomePage class represents the OrangeHRM Home/Dashboard page.
 * It uses the ActionDriver for performing Selenium actions.
 */
public class HomePage {

    // Instance of ActionDriver to perform user actions
    private ActionDriver actionDriver;

    // Locators for elements on the home page
    private By dashBoard = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[1]/i");
    private By adminTab = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/aside/nav/div[2]/ul/li[1]/a/span");
    private By userIdButton = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[3]/ul/li/span");
    private By orangeHrmLogo = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/aside/nav/div[1]/a/div[2]/img");
    private By PIM = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/aside/nav/div[2]/ul/li[2]");
    private By searchEmployeeField = By.xpath("//input[@placeholder='Employee Name or ID']");
    private By searchButton = By.xpath("//button[contains(text(),'Search')]");
    private By employeeName = By.xpath("//table[@class='oxd-table']/tbody/tr/td[2]");
    private By employeeId = By.xpath("//table[@class='oxd-table']/tbody/tr/td[1]");

    private By logoutButton = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[3]/ul/li/ul/li[4]/a");
    private By usernameField = By.xpath("//input[@name='username']");
    private By passwordField = By.xpath("//input[@name='password']");
    private By loginButton = By.xpath("//button[@type='submit']");

    public HomePage(WebDriver driver) {
        this.actionDriver = new ActionDriver(driver);
    }

    // Method to login to the application
    public void login(String username, String password) {
        actionDriver.type(usernameField, username);
        actionDriver.type(passwordField, password);
        actionDriver.click(loginButton);
    }

    // Other existing methods...
    public void DashBoard() {
        actionDriver.click(dashBoard);
    }

    public boolean isAdminTabVisible() {
        return actionDriver.isDisplayed(adminTab);
    }

    public boolean verifyOrangeHrmLogo() {
        return actionDriver.isDisplayed(orangeHrmLogo);
    }

    public void Logout() {
        actionDriver.click(userIdButton);
        actionDriver.click(logoutButton);
    }

    public void clickPIM() {
        actionDriver.click(PIM);
    }

    public String[] searchEmployeeById(String empId) {
        actionDriver.type(searchEmployeeField, empId);
        actionDriver.click(searchButton);

        String employeeNameText = actionDriver.getText(employeeName);
        String employeeIdText = actionDriver.getText(employeeId);

        return new String[]{employeeNameText, employeeIdText};
    }
}
