package org.example.datamodelprovider.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.datamodelprovider.excelData.ExcelDataModel.ExcelDataModel;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.apache.poi.ss.usermodel.BorderStyle.THIN;
import static org.apache.poi.ss.usermodel.IndexedColors.LIGHT_YELLOW;

@Component
public class ExcelWriterService {

    public <T extends ExcelDataModel> void writeDataToExcel(String filePath, String sheetName, T data) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("Данные не должны быть null");
        }
        writeDataToExcel(filePath, sheetName, List.of(data));
    }

    public <T extends ExcelDataModel> void writeDataToExcel(String filePath, String sheetName, List<T> data) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("Данные не должны быть null");
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        int rowCount = 0;

        Row headerRow = sheet.createRow(rowCount++);
        headerRow.setHeight((short) (20 * 30)); // Настройка высоты header

        // Получаем количество колонок от первого объекта
        List<String> columns = data.get(0).getColumnNames();

        CellStyle styleToHeader = createStyleToHeader(workbook);
        // Создаем заголовки столбцов
        for (int i = 1; i <= columns.size(); i++) {
            Cell cell = headerRow.createCell(i - 1);
            cell.setCellValue(columns.get(i - 1)); // Или используйте реальные имена столбцов
            cell.setCellStyle(styleToHeader);
        }

        // Заполняем данные
        setDataInRow(data, sheet, rowCount, columns.size(), workbook); // Передаем стиль для данных

        // Автоматическая подгонка ширины столбцов
        for (int i = 0; i < columns.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Сохраняем файл по указанному пути
        try (FileOutputStream fileOutputStream = new FileOutputStream(getFilePath(filePath, sheetName))) {
            workbook.write(fileOutputStream);
        } finally {
            workbook.close();
        }
    }

    private String getFilePath(String filePath, String sheetName) {
        return filePath + "\\" + sheetName + ".xlsx"; // Правильный способ добавления обратного слэша
    }

    private static CellStyle createStyleToHeader(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(LIGHT_YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);

        // Устанавливаем шрифт для заголовка
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setFontName("Arial");

        // Настройка границ для заголовка
        headerStyle.setBorderTop(THIN);
        headerStyle.setBorderBottom(THIN);
        headerStyle.setBorderLeft(THIN);
        headerStyle.setBorderRight(THIN);
        return headerStyle;
    }

    private static CellStyle createStyleToBody(Workbook workbook) {
        // Создаем стиль для данных с границами
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Настройка границ для данных
        dataStyle.setBorderTop(THIN);
        dataStyle.setBorderBottom(THIN);
        dataStyle.setBorderLeft(THIN);
        dataStyle.setBorderRight(THIN);
        return dataStyle;
    }


    private <T extends ExcelDataModel> void setDataInRow(List<T> data, Sheet sheet, int rowCount, int columnCount, Workbook workbook) {
        for (T obj : data) {
            Row row = sheet.createRow(rowCount++);
            for (int i = 1; i <= columnCount; i++) {

                Cell cell = row.createCell(i - 1);
                CellStyle styleToBody = createStyleToBody(workbook);    //Стиль для данных
                cell.setCellStyle(styleToBody);
                Object value = obj.getFiledValue(i); // Получаем значение по индексу

                if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue());
                } else if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                } else {
                    cell.setCellValue(value != null ? value.toString() : "");
                }
            }
        }
    }
}
