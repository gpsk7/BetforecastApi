package net.javaguides.springboot.service;

import net.javaguides.springboot.dto.ProfileDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private AuthenticateService authenticateService;

    public ProfileDto getProfile(String userId){
        User user = authenticateService.findByUserId(userId);
        if(user.getId() == null){
            throw new ApiException("No User Found with Email", HttpStatus.BAD_REQUEST);
        }
        return mapToProfileDto(user);
    }

    public ProfileDto mapToProfileDto(User user){
        return ProfileDto.builder().email(user.getEmail())
                .mobile(user.getMobile())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .build();
    }

    public User mapToUser(ProfileDto profileDto){
        return User.builder()
                .lastName(profileDto.getLastName())
                .firstName(profileDto.getFirstName())
                .build();
    }

    public void updateProfile(ProfileDto profileDto) {
        User user = authenticateService.findByUserId(profileDto.getEmail());
        if(user.getId() == null){
            throw new ApiException("No User Found with Email", HttpStatus.BAD_REQUEST);
        }
        user.setFirstName(profileDto.getFirstName());
        user.setLastName(profileDto.getLastName());
        authenticateService.save(user);
    }
}