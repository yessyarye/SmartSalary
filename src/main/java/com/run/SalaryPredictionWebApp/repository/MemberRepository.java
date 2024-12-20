package com.run.SalaryPredictionWebApp.repository;

import com.run.SalaryPredictionWebApp.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); // Mencari member berdasarkan email

    long countByRole(String role);

}