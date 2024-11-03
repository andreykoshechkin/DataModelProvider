package org.example.datamodelprovider.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.datamodelprovider.data.ExcelDataProjection;
import org.example.datamodelprovider.excelModel.DepositExcelModel;
import org.example.datamodelprovider.repository.ExcelDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final ExcelDataRepository excelDataRepository;
    private final ExcelWriterService excelWriterService;
    private int page = 0;
    private int currnetTotalElements = 0;

    public void exportYesterdayDeposits() {
        LocalDate previousDay = LocalDate.now().minusDays(1);
        String serviceType = "V";
        String requestType = "X";

        Page<ExcelDataProjection> dataPage = excelDataRepository
                .getExcelDataProjectionPage(previousDay, serviceType, requestType, PageRequest.of(page, 19));

        long totalElements = dataPage.getTotalElements(); // Общее количество элементов в запросе

        if (currnetTotalElements == totalElements) {
            log.warn("Заявок больше нет для обработки. Количество обработанных заявок: {}", totalElements);
            page = 0;
            return;
        }

        List<DepositExcelModel> depositModels = dataPage.getContent()
                .stream()
                .map(this::mapToDepositExcelModel)
                .collect(Collectors.toList());

        writeDepositsToExcel(depositModels);

        int currentBatchSize = dataPage.getNumberOfElements();
        currnetTotalElements += currentBatchSize;

        if (currnetTotalElements != totalElements) {
            log.info("Итерация: '{}', обработано заявок на текущей странице: {}, всего обработано: {}", page, currentBatchSize, currnetTotalElements);
            page++;
        } else {
            log.info("Обработка завершена. Количество обработанных заявок: {}", currnetTotalElements);
            page = 0;
        }
    }


    private void writeDepositsToExcel(List<DepositExcelModel> depositModels) {
        try {
            excelWriterService.writeDataToExcel("C:\\Users\\Andrey\\Desktop\\IdeaProject", "deposit", depositModels);
            log.info("Данные успешно записаны в Excel. Записано {} заявок", depositModels.size());
        } catch (IOException e) {
            log.error("Ошибка при записи данных в Excel", e);
            throw new RuntimeException("Не удалось записать данные в Excel", e);
        }
    }

    private DepositExcelModel mapToDepositExcelModel(ExcelDataProjection dataProjection) {
        return DepositExcelModel.builder()
                .email(dataProjection.getEmail())
                .requestId(dataProjection.getId())
                .build();
    }
}
