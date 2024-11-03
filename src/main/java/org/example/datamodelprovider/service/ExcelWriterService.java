package org.example.datamodelprovider.service;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.datamodelprovider.excelData.ExcelDataModel.ExcelDataModel;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.util.List;

import static org.apache.poi.ss.usermodel.BorderStyle.THIN;
import static org.apache.poi.ss.usermodel.IndexedColors.*;

@Slf4j
@Component
public class ExcelWriterService {

    public <T extends ExcelDataModel> void writeDataToExcel(String filePath, String sheetName, T data) {
        if (data == null) {
            throw new IllegalArgumentException("Данные не должны быть null");
        }
        writeDataToExcel(filePath, sheetName, List.of(data));
    }

    public <T extends ExcelDataModel> void writeDataToExcel(String filePath, String sheetName, List<T> data) {
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

        String fullPath = createFullPath(filePath, sheetName);
        Try.withResources(() -> new FileOutputStream(fullPath))
                .of(fileOutputStream -> {
                    workbook.write(fileOutputStream);
                    return fileOutputStream;
                })
                .onFailure(e -> log.error("Ошибка при записи данных: {}", e.getMessage()))
                .andFinallyTry(workbook::close);
    }

    private static CellStyle createStyleToHeader(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(LAVENDER.getIndex());
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
        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Настройка границ для данных
        bodyStyle.setBorderTop(THIN);
        bodyStyle.setBorderBottom(THIN);
        bodyStyle.setBorderLeft(THIN);
        bodyStyle.setBorderRight(THIN);
        return bodyStyle;
    }

    private <T extends ExcelDataModel> void setDataInRow(List<T> data, Sheet sheet, int rowCount, int columnCount, Workbook workbook) {
        for (T obj : data) {
            Row row = sheet.createRow(rowCount++);
            for (int i = 1; i <= columnCount; i++) {

                Cell cell = row.createCell(i - 1);
                CellStyle styleToBody = createStyleToBody(workbook);    //Стиль для данных
                cell.setCellStyle(styleToBody);
                cell.getRow().setHeight((short) (10 * 45));
                Object value = obj.getFiledValue(i); // Получаем значение по индексу
                cell.setCellValue(value.toString());
            }
        }
    }

    private String createFullPath(String filePath, String sheetName) {
        return filePath + "\\" + sheetName + ".xlsx";
    }
}
