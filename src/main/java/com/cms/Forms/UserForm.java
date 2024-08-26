package com.cms.Forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Minimum 3 Characters Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid Email Address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Minimum 8 Characters Required")
    private String password;

    @NotBlank(message = "About is Required")
    private String about;

    @NotBlank(message = "Gender is Required")
    private String gender;

    @NotBlank(message = "Phone Number is Required")
    @Size(min = 8, max = 12, message = "Invalid Phone Number")
    private String phone;
}
