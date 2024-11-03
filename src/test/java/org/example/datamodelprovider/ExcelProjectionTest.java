package org.example.datamodelprovider;

import org.example.datamodelprovider.data.CandidateRecheckFnsServiceProjection;
import org.example.datamodelprovider.repository.ExcelDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class ExcelProjectionTest {


    @Autowired
    private ExcelDataRepository excelDataRepository;

    @Test
    void test() {
        LocalDate lastDay = LocalDate.now().minusDays(1); // Получение даты предыдущего дня
        String serviceType = "V";
        String requestType = "X";

        List<CandidateRecheckFnsServiceProjection> excelDataProjection = excelDataRepository.getExcelDataProjection(serviceType, requestType, lastDay);

        for (CandidateRecheckFnsServiceProjection dataProjection : excelDataProjection) {
            System.out.println(dataProjection);
        }

    }
}