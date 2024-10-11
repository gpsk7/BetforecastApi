package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.ProfileDto;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.service.AuthenticateService;
import net.javaguides.springboot.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user/profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private AuthenticateService authenticateService;

    @GetMapping("/{emailId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable("emailId") String userId){
        return ResponseEntity.ok(userProfileService.getProfile(userId));
    }

    @PostMapping
    public ResponseEntity<String> updateProfile(@RequestBody ProfileDto  profileDto){
        userProfileService.updateProfile(profileDto);
        return ResponseEntity.ok("updated successfully");
    }
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> findAll(){
      return authenticateService.getAll();
    }


}