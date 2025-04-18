package com.orangehrm.utilities;

import java.sql.*;

public class MySQLConnectExample {

    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/orangehrm"; // Replace with your DB name
    private static final String USERNAME = "root"; // Replace with your DB username
    private static final String PASSWORD = "your_password"; // Replace with your DB password

    // Method to get employee name by ID
    public static String getEmployeeNameFromDB(String empId) {
        String query = "SELECT first_name, middle_name, last_name FROM hs_hr_employee WHERE employee_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, empId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String middleName = rs.getString("middle_name");
                String lastName = rs.getString("last_name");

                return firstName + " " + (middleName != null ? middleName + " " : "") + lastName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Method to insert employee data into the database
    public static boolean insertEmployeeData(String empId, String firstName, String middleName, String lastName, String jobTitle) {
        String query = "INSERT INTO hs_hr_employee (employee_id, first_name, middle_name, last_name, job_title_code) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameters for the query
            stmt.setString(1, empId);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            stmt.setString(4, lastName);
            stmt.setString(5, jobTitle);

            // Execute the insert query
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if data was inserted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if insertion failed
    }

    public static void main(String[] args) {
        // Example data to be inserted into the database
        String empId = "1436"; // Example Employee ID
        String firstName = "John";
        String middleName = "A.";
        String lastName = "Doe";
        String jobTitle = "Software Engineer";

        // Insert employee data
        boolean isInserted = insertEmployeeData(empId, firstName, middleName, lastName, jobTitle);

        if (isInserted) {
            System.out.println("✅ Employee data inserted successfully!");
        } else {
            System.out.println("❌ Failed to insert employee data.");
        }

        // Example: Retrieve employee data by ID (for validation)
        String employeeName = getEmployeeNameFromDB("1436");
        System.out.println("Employee name fetched from DB: " + employeeName);
    }
}
