// package com.scm.helpers;

// import org.springframework.http.HttpStatus;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.multipart.MaxUploadSizeExceededException;

// import jakarta.servlet.http.HttpSession;



// @ControllerAdvice
// public class GlobalExceptionHandler {

//     @ExceptionHandler(MaxUploadSizeExceededException.class)
//     @ResponseStatus(HttpStatus.BAD_REQUEST)
//     public String handleMaxSizeException(MaxUploadSizeExceededException exc, HttpSession session) {
//         session.setAttribute("message", "File size exceeds the maximum limit of 5MB.");
//         return "redirect:/user/add_contact"; // Redirect to the form with an error message
//     }
// }