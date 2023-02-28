package com.school.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.school.dao.TeacherRepository;
import com.school.entity.Teacher;
import com.school.service.EmailService;

@Controller
public class ForgotController {

	Random random = new Random(1000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@GetMapping("/forgot")
	public String forgotPassword() {
		return "forgot_password_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email, HttpSession session) {
		
		System.out.println("Email" + email);
		
		// generate 4 digit OTP
		
		int otp = random.nextInt(99999);
		
		System.out.println("Otp : " + otp);
		
		// send OTP to Registered Email id
		
		String subject = "OTP From Your School Management Team..";
		String message = ""
					+"<div style='border:1px solid #e2e2e2; padding:20px'>"
					+"<h1>"
					+"OTP is : "
					+"<b>"+otp
					+"</n>"
					+"</h1>"
					+"</div>";
		String to = email;
		
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if(flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			
			return "verify_otp";
		}else {
			session.setAttribute("message", "Check your email id ..");
			return "forgot_password_form";
		}
	
		
	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {
		
		int myOtp = (int) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");
		
		if(myOtp==otp) {
			Teacher teacher = this.teacherRepository.getTeacherByTeacherName(email);
			if(teacher== null) {
				session.setAttribute("message", "Teacher Does not exist");
				return "forgot_password_form";
			}else {
				
			}
			return "password_change_form";	
		}else {
			session.setAttribute("message", "You have entered wrong OTP");
			return "verify_otp";
		}
		
		
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
		
		String email = (String) session.getAttribute("email");
		Teacher teacher = this.teacherRepository.getTeacherByTeacherName(email);
		teacher.setPassword(this.passwordEncoder.encode(newpassword));
		this.teacherRepository.save(teacher);
		
		return "redirect:/signin?change=password changed successfully..";
	}
	
	
}













