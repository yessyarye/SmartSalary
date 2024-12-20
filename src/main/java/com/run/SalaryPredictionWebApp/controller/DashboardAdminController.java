package com.run.SalaryPredictionWebApp.controller;

import com.opencsv.CSVWriter;
import com.run.SalaryPredictionWebApp.Service.CsvService;
import com.run.SalaryPredictionWebApp.Service.DashboardService;
import com.run.SalaryPredictionWebApp.Service.MemberService;
import com.run.SalaryPredictionWebApp.Service.SalaryService;
import com.run.SalaryPredictionWebApp.model.Member;
import com.run.SalaryPredictionWebApp.model.SalaryData;
import com.run.SalaryPredictionWebApp.repository.SalaryDataRepository;
import com.run.SalaryPredictionWebApp.util.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class DashboardAdminController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private CsvService csvService;

    @Autowired
    private SalaryDataRepository salaryDataRepository;

    @Autowired
    private final SalaryService salaryDataService;

    private final MemberService memberService;

    public DashboardAdminController(SalaryService salaryDataService, MemberService memberService) {
        this.salaryDataService = salaryDataService;
        this.memberService = memberService;
    }

    @GetMapping("/dashboardadmin")
    public String showAdminDashboard(HttpSession session, Model model, HttpServletRequest request) {
        SessionUtils.populateSessionData(session, model, request, memberService);

        // Logika tambahan
        model.addAttribute("totalUsers", dashboardService.getTotalMembersWithUserRole());
        model.addAttribute("newSalaryData", dashboardService.getSalaryDataCount());
        model.addAttribute("totalMembers", dashboardService.getTotalMembersCount());

        return "DashboardAdmin";
    }

    @GetMapping("/viewdata")
    public String showViewData(HttpSession session, Model model, HttpServletRequest request) {
        SessionUtils.populateSessionData(session, model, request, memberService);

        // Menambahkan data tambahan ke model
        model.addAttribute("salaryData", salaryDataService.getSalaryData());

        return "ViewDatas";
    }

    @GetMapping("/prediksi")
    public String showPredictionPage(HttpSession session, Model model, HttpServletRequest request) {
        SessionUtils.populateSessionData(session, model, request, memberService);
        return "prediksi";
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

        SessionUtils.populateSessionData(session, model, request, memberService);

        // Memasukkan kembali nilai input ke model
        model.addAttribute("age", age);
        model.addAttribute("gender", gender);
        model.addAttribute("educationLevel", educationLevel);
        model.addAttribute("jobTitle", jobTitle);
        model.addAttribute("yearsExperience", yearsExperience);

        // payload untuk Flask API
        Map<String, Object> payload = Map.of(
                "age", age,
                "gender", gender,
                "educationLevel", educationLevel,
                "jobTitle", jobTitle,
                "yearsExperience", yearsExperience
        );

        // Memanggil Flask API
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

        return "prediksi";
    }

    @GetMapping("/newdata")
    public String showNewData(HttpSession session, Model model, HttpServletRequest request) {
        SessionUtils.populateSessionData(session, model, request, memberService);

        List<SalaryData> salaryDataList = salaryDataService.getSalaryDataFromDatabase();
        model.addAttribute("salaryData", salaryDataList);
        return "crudData";
    }

    @GetMapping("/add_data")
    public String showAddDataForm(HttpSession session, Model model, HttpServletRequest request) {
        SessionUtils.populateSessionData(session, model, request, memberService);

        model.addAttribute("salaryData", new SalaryData());
        return "addData";
    }

    @PostMapping("/add_data")
    public String addData(@ModelAttribute SalaryData salaryData, HttpSession session, Model model, HttpServletRequest request) {
        SessionUtils.populateSessionData(session, model, request, memberService);

        salaryDataRepository.save(salaryData);
        return "redirect:/admin/newdata";
    }


@GetMapping("/editdata/{id}")
public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session,HttpServletRequest request ) {
    SessionUtils.populateSessionData(session, model, request, memberService);

    Optional<SalaryData> salaryData = salaryDataService.findById(id);
    if (salaryData.isPresent()) {
        model.addAttribute("salaryData", salaryData.get());
        return "editData"; // Return view template
    } else {
        // Jika ID tidak ditemukan, redirect ke halaman lain atau tampilkan pesan error
        model.addAttribute("errorMessage", "Data Gaji tidak ditemukan.");
        return "redirect:/admin"; // Redirect ke halaman daftar admin atau yang sesuai
    }
}

    @PostMapping("/editdata/{id}")
    public String updateSalaryData( @Validated @ModelAttribute("salaryData") SalaryData salaryData, BindingResult bindingResult, Model model, HttpSession session, HttpServletRequest request) {
        SessionUtils.populateSessionData(session, model, request, memberService);


        if (bindingResult.hasErrors()) {
            return "editdata";
        }
        salaryDataService.save(salaryData); // Simpan perubahan data
        return "redirect:/admin/newdata"; // Redirect kembali ke halaman daftar admin
    }


    @PostMapping("/deletedata/{id}")
    public String deleteData(@PathVariable("id") Long id, HttpSession session, Model model, HttpServletRequest request) {
        SessionUtils.populateSessionData(session, model, request, memberService);

        salaryDataRepository.deleteById(id);
        return "redirect:/admin/newdata";
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportCsv() {
        List<Object> data = csvService.getDataFromAPI();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(baos);
             CSVWriter writer = new CSVWriter(osw)) {

            writer.writeNext(new String[]{"Age", "Gender", "Education Level", "Job Title", "Years of Experience", "Salary"});

            data.forEach(record -> {
                String[] row = record.toString().replaceAll("[{}]", "").split(",");
                writer.writeNext(row);
            });

            writer.flush();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=newest_salary_data.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new InputStreamResource(inputStream));

        } catch (IOException e) {
            throw new RuntimeException("Error generating CSV file", e);
        }
    }
}