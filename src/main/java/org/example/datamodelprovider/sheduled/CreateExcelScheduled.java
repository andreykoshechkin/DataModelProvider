package org.example.datamodelprovider.sheduled;

import lombok.RequiredArgsConstructor;
import org.example.datamodelprovider.service.ExcelExportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateExcelScheduled {

    private final ExcelExportService excelExportService;


    @Scheduled(cron = "0 */1 * * * *")
    public void createExcel() {
        excelExportService.exportYesterdayDeposits();
    }
}
