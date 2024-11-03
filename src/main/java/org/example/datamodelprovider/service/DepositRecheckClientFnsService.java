package org.example.datamodelprovider.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.datamodelprovider.data.CandidateRecheckFnsServiceProjection;
import org.example.datamodelprovider.excelModel.DepositFailedRecheckFnsExcelModel;
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
public class DepositRecheckClientFnsService {

    private final ExcelDataRepository excelDataRepository;
    private final ExcelWriterService excelWriterService;
    private int page = 0;
    private int currnetTotalElements = 0;

    private void checkSysParamIgnoreChecksAndChangeValue(){

    }


    private void recheckClientsServiceOkFns(){

    }

    private void notifyFailedRecheckClients(){

    };

    public void exportYesterdayDeposits() {
        LocalDate previousDay = LocalDate.now().minusDays(1);
        String serviceType = "V";
        String requestType = "X";

        Page<CandidateRecheckFnsServiceProjection> dataPage = excelDataRepository
                .getExcelDataProjectionPage(previousDay, serviceType, requestType, PageRequest.of(page, 98));

        long totalElements = dataPage.getTotalElements(); // Общее количество элементов в запросе

        if (currnetTotalElements == totalElements) {
            //все кандидаты на повторную проверку обработаны
            log.warn("All candidates for re-verification have been processed: {}", totalElements);
            page = 0;
            return;
        }

        List<DepositFailedRecheckFnsExcelModel> depositModels = dataPage.getContent()
                .stream()
                .map(this::mapToDepositExcelModel)
                .collect(Collectors.toList());

        writeDepositsToExcel(depositModels);

        int currentBatchSize = dataPage.getNumberOfElements();
        currnetTotalElements += currentBatchSize;

        if (currnetTotalElements != totalElements || currnetTotalElements == totalElements) {
            // log.info("Итерация: '{}', текущее количество клиентов на повторную проверку:
            // {}, Общее количество отправленных заявок на повторную проверку: {}",
            log.info("Iteration: ({}). Current number of clients sent recheck 'serviceOK (FNS)': {}. Total number of clients: {}",
                    page, currentBatchSize, currnetTotalElements);
            page++;
        } else {
            //Процесс завершен, количество клиентов обработано
            log.info("Process completed. Total number of clients processed: {}", currnetTotalElements);
            page = 0;
        }
    }


    //Отправка почты
    private void writeDepositsToExcel(List<DepositFailedRecheckFnsExcelModel> depositModels) {
        try {
            excelWriterService.writeDataToExcel("C:\\Users\\Andrey\\Desktop\\IdeaProject", "deposit", depositModels);
            //log.info("Данные успешно записаны в Excel");
          //  log.info("Data successfully written to Excel");
        } catch (IOException e) {
            //  log.error("Ошибка при записи данных в Excel", e);
            log.error("Error writing data to Excel", e);
            throw new RuntimeException("Не удалось записать данные в Excel", e);
        }
    }

    private DepositFailedRecheckFnsExcelModel mapToDepositExcelModel(CandidateRecheckFnsServiceProjection dataProjection) {
        return DepositFailedRecheckFnsExcelModel.builder()
                .email(dataProjection.getEmail())
                .requestId(dataProjection.getId())
                .build();
    }
}
