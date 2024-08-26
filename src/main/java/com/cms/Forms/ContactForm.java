package com.cms.Forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Minimum 3 Characters Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid Email Address")
    private String email;
    private String address;
    private String about;
    @NotBlank(message = "Phone Number is Required")
    @Size(min = 8, max = 12, message = "Invalid Phone Number")
    private String phone;
    private String websiteLink;
    private String linkedinLink;


    private MultipartFile contactPic;
    private boolean favorite;
}
