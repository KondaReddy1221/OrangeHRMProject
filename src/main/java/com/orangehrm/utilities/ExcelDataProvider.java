package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

public class ExcelDataProvider {

    // Method to read data from Excel file
    public static List<String[]> readExcelData(String filePath, String sheetName) {
        List<String[]> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getPhysicalNumberOfRows();

            // Loop through rows (skip header row)
            for (int i = 1; i < rowCount; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) continue;

                int cellCount = row.getLastCellNum();
                String[] rowData = new String[cellCount];

                // Loop through cells in each row
                for (int j = 0; j < cellCount; j++) {
                    XSSFCell cell = row.getCell(j);
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        rowData[j] = cell.getStringCellValue();
                    } else {
                        rowData[j] = "";
                    }
                }
                dataList.add(rowData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
