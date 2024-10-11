package net.javaguides.springboot.controller;

import net.javaguides.springboot.service.master.JockeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {


    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Admin Dashboard - Only accessible by users with the ADMIN role";
    }

    @GetMapping("/settings")
    public String adminSettings() {
        return "Admin Settings - Only accessible by users with the ADMIN role";
    }
}
