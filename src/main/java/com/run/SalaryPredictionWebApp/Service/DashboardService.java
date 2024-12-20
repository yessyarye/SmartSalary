package com.run.SalaryPredictionWebApp.Service;

import com.run.SalaryPredictionWebApp.repository.MemberRepository;
import com.run.SalaryPredictionWebApp.repository.SalaryDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    private MemberRepository membersRepository;

    @Autowired
    private SalaryDataRepository salaryDatasRepository;

    public long getTotalMembersWithUserRole() {
        return membersRepository.countByRole("user");
    }

    public long getSalaryDataCount() {
        return salaryDatasRepository.count();
    }

    public long getTotalMembersCount() {
        return membersRepository.count();
    }
}
