package org.example.datamodelprovider.excelModel;

import lombok.*;
import org.example.datamodelprovider.annotation.ExcelPresenter;
import org.example.datamodelprovider.excelData.ExcelDataModel.ExcelDataModel;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositExcelModel extends ExcelDataModel {

    @ExcelPresenter(name = "Номер заявки")
    private Long requestId;
    @ExcelPresenter(name = "Email")
    private String email;
}
