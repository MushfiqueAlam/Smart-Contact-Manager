package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ContactForm {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email is required")
    @NotBlank(message = "Invalid is required")
    private String email;

    @NotBlank(message = "PhoneNumber is required")
    @Pattern(regexp = "((\\+*)((0[ -]*)*|((91 )*))((\\d{12})+|(\\d{10})+))|\\d{5}([- ]*)\\d{6}", 
             message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Address is Required")
    private String address;

    private String description;

    private boolean favorite;

    private String websiteLink;

    private String linkedInLink;

    private MultipartFile proFileImage;
}
