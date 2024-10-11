package net.javaguides.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

//    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobile;
}
