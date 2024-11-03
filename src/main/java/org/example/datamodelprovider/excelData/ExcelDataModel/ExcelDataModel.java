package org.example.datamodelprovider.excelData.ExcelDataModel;

import org.example.datamodelprovider.annotation.ExcelColumnPresenter;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

public abstract class ExcelDataModel {

    // Метод для получения значения по индексу
    public Object getFiledValue(int index) {
        Field[] fields = this.getClass().getDeclaredFields();

        Field field = fields[index - 1]; // Индекс начинается с 1, поэтому вычитаем 1
        field.setAccessible(true); // Делаем поле доступным, если оно приватное
        try {
            return field.get(this); // Получаем значение поля
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Ошибка доступа к полю: " + field.getName(), e);
        }
    }

    public List<String> getColumnNames() {
        Field[] fields = this.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .filter(it -> it.isAnnotationPresent(ExcelColumnPresenter.class))
                .map(field -> field.getAnnotation(ExcelColumnPresenter.class).name())
                .toList();

    }

}