package com.run.SalaryPredictionWebApp.controller;

import com.run.SalaryPredictionWebApp.Service.MemberService;
import com.run.SalaryPredictionWebApp.Service.SalaryService;
import com.run.SalaryPredictionWebApp.util.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class DashboardUserController {

    private final SalaryService salaryDataService;
    private final MemberService memberService;

    public DashboardUserController(SalaryService salaryDataService, MemberService memberService) {
        this.salaryDataService = salaryDataService;
        this.memberService = memberService;
    }

    @GetMapping("/dashboard")
    public String showUserDashboard(HttpSession session, Model model, HttpServletRequest request) {
        // Menggunakan SessionUtils untuk menambahkan data sesi ke model
        SessionUtils.populateSessionData(session, model, request, memberService);

        return "DashboardUser"; // Tampilkan halaman dashboarduser.html
    }

    @GetMapping("/viewdata")
    public String showViewData(HttpSession session, Model model, HttpServletRequest request) {
        // Menggunakan SessionUtils untuk menambahkan data sesi ke model
        SessionUtils.populateSessionData(session, model, request, memberService);

        // Menambahkan data tambahan ke model
        model.addAttribute("salaryData", salaryDataService.getSalaryData());

        return "UserViewDatas";
    }

    @GetMapping("/prediksi")
    public String showPredictionPage(HttpSession session, Model model, HttpServletRequest request) {

        SessionUtils.populateSessionData(session, model, request, memberService);

        return "UserPrediksi";
    }

    @PostMapping("/prediksi")
    public String handlePrediction(
            HttpSession session,
            @RequestParam("age") int age,
            @RequestParam("gender") String gender,
            @RequestParam("educationLevel") String educationLevel,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("yearsExperience") int yearsExperience,
            Model model,
            HttpServletRequest request) {

        // Menggunakan SessionUtils untuk menambahkan data sesi ke model
        SessionUtils.populateSessionData(session, model, request, memberService);

        // Masukkan kembali nilai input ke model
        model.addAttribute("age", age);
        model.addAttribute("gender", gender);
        model.addAttribute("educationLevel", educationLevel);
        model.addAttribute("jobTitle", jobTitle);
        model.addAttribute("yearsExperience", yearsExperience);

        // Siapkan payload untuk Flask API
        Map<String, Object> payload = Map.of(
                "age", age,
                "gender", gender,
                "educationLevel", educationLevel,
                "jobTitle", jobTitle,
                "yearsExperience", yearsExperience
        );

        // Panggil Flask API
        String flaskApiUrl = "http://localhost:6000/";
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map response = restTemplate.postForObject(flaskApiUrl, payload, Map.class);
            if (response != null && response.containsKey("predictedSalary")) {
                model.addAttribute("predictedSalary", response.get("predictedSalary"));
            } else {
                model.addAttribute("error", "No response from Flask API.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error calling Flask API: " + e.getMessage());
        }

        return "UserPrediksi"; // Tetap di halaman prediksi
    }

    @GetMapping("/grafik")
    public String showGrafikPage(HttpSession session, Model model, HttpServletRequest request) {

        SessionUtils.populateSessionData(session, model, request, memberService);

        return "Grafik";
    }

}