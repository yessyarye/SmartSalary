package com.run.SalaryPredictionWebApp.Service;

import com.run.SalaryPredictionWebApp.model.Member;
import com.run.SalaryPredictionWebApp.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // Mengambil member berdasarkan ID
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null); // Mengambil member berdasarkan ID, atau null jika tidak ditemukan
    }
    public Member getCurrentMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
