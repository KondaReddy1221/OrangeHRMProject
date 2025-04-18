# 🧪 OrangeHRM Automation Framework

This repository contains a complete Selenium Test Automation Framework for testing the OrangeHRM web application. It is designed using Java, Maven, TestNG, and the Page Object Model (POM), and includes Excel-based data-driven testing, screenshot capture, and ExtentReports integration.

---

## 📌 Table of Contents
- [About the Project](#about-the-project)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [Reports & Screenshots](#reports--screenshots)
- [Contributing](#contributing)
- [License](#license)

---

## 📋 About the Project
A complete end-to-end automation framework for the [OrangeHRM Demo Site](https://opensource-demo.orangehrmlive.com/) with the following modules:

- Login functionality
- Leave module validation
- Admin/user module
- Directory/Recruitment modules
- Profile verification
- Screenshot capturing on failure
- HTML Extent Report generation

---

## 📁 Project Structure
```
src
├── main
│   ├── java
│   │   ├── base              # BaseClass for WebDriver setup
│   │   ├── actions           # Reusable ActionDriver methods
│   │   ├── pages             # Page classes using POM
│   │   ├── utilities         # ExcelReader, ScreenshotUtil, WaitHelper
│   │   └── listeners         # TestNG listeners for reporting
│
├── test
│   └── java
│       └── testcases         # All test classes (LoginTest, LeaveTest, etc.)
│
├── resources
│   ├── testdata              # Excel test data
│   ├── ExtentReport          # HTML report output
│   └── extent-config.xml     # Report design configuration
```

---

## 💻 Technologies Used
- Java
- Selenium WebDriver
- TestNG
- Apache POI (Excel reading)
- Maven
- ExtentReports
- Page Object Model

---

## 🚀 Getting Started

### ✅ Prerequisites
- Java JDK 11+
- Maven installed
- Chrome browser and compatible ChromeDriver




