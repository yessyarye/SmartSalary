package com.run.SalaryPredictionWebApp.repository;

import com.run.SalaryPredictionWebApp.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);  // Method untuk memeriksa apakah email sudah ada
}
