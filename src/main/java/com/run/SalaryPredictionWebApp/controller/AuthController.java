package com.run.SalaryPredictionWebApp.controller;

import com.run.SalaryPredictionWebApp.model.Member;
import com.run.SalaryPredictionWebApp.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private MemberRepository memberRepository; // Menginject MemberRepository

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        // Cari member berdasarkan email
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            // Cek apakah password yang dimasukkan cocok dengan password di database
            if (member.getPassword().equals(password)) {
                session.setAttribute("email", email);
                // Login berhasil, cek role user
                if ("admin".equalsIgnoreCase(member.getRole())) {
                    // Jika role admin, arahkan ke dashboard admin
                    redirectAttributes.addFlashAttribute("message", "Login Successful as Admin!");
                    return "redirect:/admin/dashboardadmin";
                } else if ("user".equalsIgnoreCase(member.getRole())) {
                    // Jika role user, arahkan ke dashboard user
                    redirectAttributes.addFlashAttribute("message", "Login Successful as User!");
                    return "redirect:/user/dashboard"; // Arahkan ke dashboard user
                } else {
                    // Jika role tidak dikenal
                    redirectAttributes.addFlashAttribute("error", "Unknown role.");
                    return "redirect:/login"; // Kembali ke halaman login jika role tidak dikenal
                }
            } else {
                // Password salah
                redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
                return "redirect:/login";  // Kembali ke halaman login jika password salah
            }
        } else {
            // Email tidak ditemukan
            redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login"; // Kembali ke halaman login jika email tidak ditemukan
        }
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup"; // Merender halaman signup.html
    }


    @PostMapping("/signup")
    public String processSignup(@RequestParam String email,@RequestParam String firstname, @RequestParam String lastname, @RequestParam String password, RedirectAttributes redirectAttributes) {
        // Cek apakah email sudah terdaftar di database
        if (memberRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email sudah terdaftar.");
            return "redirect:/signup"; // Kembali ke halaman signup jika email sudah terdaftar
        }

        // Jika email belum terdaftar, buat member baru
        Member newMember = new Member();
        newMember.setEmail(email);
        newMember.setFirstname(firstname); // Tambahkan ini
        newMember.setLastname(lastname);   // Tambahkan ini
        newMember.setPassword(password); // Anda bisa menambahkan hashing password di sini untuk keamanan
        newMember.setRole("user"); // Default role adalah 'user', bisa diubah sesuai kebutuhan

        memberRepository.save(newMember); // Simpan member baru ke database

        redirectAttributes.addFlashAttribute("message", "Registrasi berhasil, silakan login.");
        return "redirect:/login"; // Arahkan pengguna ke halaman login setelah registrasi berhasil
    }


    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "You have logged out successfully.");
        return "redirect:/"; // Arahkan kembali ke halaman login
    }

}
