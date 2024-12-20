package com.run.SalaryPredictionWebApp.repository;

import com.run.SalaryPredictionWebApp.model.SalaryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryDataRepository extends JpaRepository<SalaryData, Long> {

}
