package org.example.datamodelprovider.repository;

import org.example.datamodelprovider.data.Deposit;
import org.example.datamodelprovider.data.CandidateRecheckFnsServiceProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface ExcelDataRepository extends JpaRepository<Deposit, Long> {

    @Query("select new org.example.datamodelprovider.data.CandidateRecheckFnsServiceProjection(dep.id, dep.email) " +
            "from Deposit dep " +
            "where dep.dtOpen = :yesterday " +
            "and dep.serviceType = :serviceType " +
            "and dep.requestType = :requestType ")
    List<CandidateRecheckFnsServiceProjection> getExcelDataProjection(@Param("serviceType") String serviceType,
                                                                      @Param("requestType") String requestType,
                                                                      @Param("yesterday") LocalDate yesterday);


    @Query("select new org.example.datamodelprovider.data.CandidateRecheckFnsServiceProjection(dep.id, dep.email) " +
            "from Deposit dep " +
            "where dep.dtOpen = :yesterday " +
            "and dep.serviceType = :serviceType " +
            "and dep.requestType = :requestType")
    Page<CandidateRecheckFnsServiceProjection> getExcelDataProjectionPage(@Param("yesterday") LocalDate yesterday,
                                                                          @Param("serviceType") String serviceType,
                                                                          @Param("requestType") String requestType,
                                                                          Pageable pageable);

}
