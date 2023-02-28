package com.school.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.school.dao.TeacherRepository;
import com.school.entity.Teacher;
import com.school.helper.Message;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private TeacherRepository teacherRepository;

	@GetMapping("/")
	public String homeController(Model model) {
		model.addAttribute("title","School Management");
		return "index";
	}
	
	@GetMapping("/about")
	public String aboutController(Model model) {
		model.addAttribute("title","About - School Management");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signupController(Model model) {
		model.addAttribute("title","Signup");
		model.addAttribute("teacher",new Teacher());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String registerTeacher(@Valid @ModelAttribute("teacher") Teacher teacher,
									BindingResult result, Model model, HttpSession session) {
		
		try {
			
			if(result.hasErrors()) {
				System.out.println("Errors : " + result.toString());
				model.addAttribute("teacher", teacher);
				return "signup";
			}
			
			teacher.setRole("ROLE_TEACHER");
			teacher.setImageUrl("contact.png");
			teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
			
			System.out.println("Teacher : " + teacher);
			
			Teacher teacher1 = this.teacherRepository.save(teacher);
			
			model.addAttribute("teacher",new Teacher());
			session.setAttribute("message", new Message("Successfully Registerd", "alert-success"));
			
			return "signup";
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("teacher",teacher);
			session.setAttribute("message", new Message("Something went wrong" +e.getMessage(), "alert-danger"));
			return "signup";
		}
		
		
	}
	
	
	@GetMapping("/signin")
	public String customLoginForm(Model model) {
		model.addAttribute("title","Login Page");
		return "login";
	}
	
	
}
