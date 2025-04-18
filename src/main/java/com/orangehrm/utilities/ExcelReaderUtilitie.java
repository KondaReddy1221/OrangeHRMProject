package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtilitie {

    // List to hold all row data as array of Strings
    public static List<String[]> readExcelData(String filePath, String sheetName) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet '" + sheetName + "' not found in the Excel file.");
                return data;
            }

            for (Row row : sheet) {
                int lastCellNum = row.getLastCellNum();
                String[] rowData = new String[lastCellNum];

                for (int i = 0; i < lastCellNum; i++) {
                    Cell cell = row.getCell(i);
                    rowData[i] = getCellValueAsString(cell);
                }

                data.add(rowData); // Add this row to the list
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Helper method to convert any cell type to String
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        CellType type = cell.getCellType();

        switch (type) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());
                }

            case BLANK:
                return "";

            default:
                return "";
        }
    }
}
