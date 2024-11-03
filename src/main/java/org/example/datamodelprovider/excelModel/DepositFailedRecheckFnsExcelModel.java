package org.example.datamodelprovider.excelModel;

import lombok.*;
import org.example.datamodelprovider.annotation.ExcelColumnPresenter;
import org.example.datamodelprovider.excelData.ExcelDataModel.ExcelDataModel;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositFailedRecheckFnsExcelModel extends ExcelDataModel {

    @ExcelColumnPresenter(name = "Номер заявки")
    private Long requestId;
    @ExcelColumnPresenter(name = "Email")
    private String email;
}
