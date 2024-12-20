package com.run.SalaryPredictionWebApp.util;

import com.run.SalaryPredictionWebApp.model.Member;
import com.run.SalaryPredictionWebApp.Service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public class SessionUtils {

    public static void populateSessionData(
            HttpSession session,
            Model model,
            HttpServletRequest request,
            MemberService memberService
    ) {
        // Mengambil email pengguna dari sesi
        String email = (String) session.getAttribute("email");

        if (email == null) {
            throw new RuntimeException("User not found");
        }

        // Mendapatkan Member
        Member member = memberService.getCurrentMember(email);

        // Menambahkan data ke model
        model.addAttribute("members", member);

        // Menambahkan URI saat ini
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
    }
}
