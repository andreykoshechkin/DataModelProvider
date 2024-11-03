package org.example.datamodelprovider.excelData.ExcelDataModel;

import org.example.datamodelprovider.annotation.ExcelPresenter;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

public abstract class ExcelDataModel {

    // Метод для получения значения по индексу
    public Object getValue(int index) {
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
                .filter(it -> it.isAnnotationPresent(ExcelPresenter.class))
                .map(field -> field.getAnnotation(ExcelPresenter.class).name())
                .toList();

    }

}