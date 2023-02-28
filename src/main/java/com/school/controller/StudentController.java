package com.school.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.school.dao.StudentRepository;
import com.school.dao.TeacherRepository;
import com.school.entity.Student;
import com.school.entity.Teacher;
import com.school.helper.Message;

@Controller
@RequestMapping("/teacher")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	// common method for getName
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String name = principal.getName();
		System.out.println("Teachername : " + name);
		
		Teacher teacher = teacherRepository.getTeacherByTeacherName(name);
		System.out.println("Teacher : " + teacher);
		
		model.addAttribute("teacher", teacher);
	}
	
	// student/dashboard -> after login
	
	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title","Dashboard : School Management");
		return "student/dashboard";
	}
	
	
	// open add student form handler
	
	@GetMapping("/add-student")
	public String openAddStudentForm(Model model) {
		model.addAttribute("title","Add Student");
		model.addAttribute("student", new Student());
		return "student/add_student_form";
	}
	
	// save student details in database handler
	
	@PostMapping("/process-student")
	public String processStudent(@Valid @ModelAttribute("student") Student student, 
			BindingResult result, @RequestParam("image") MultipartFile file,
			Principal principal, HttpSession session, Model model) {
		
		try {
			
			String name = principal.getName();
			Teacher teacher = this.teacherRepository.getTeacherByTeacherName(name);
			
			if(result.hasErrors()) {
				System.out.println("Errors : "+result.toString());
				model.addAttribute("student",student);
				return "student/add_student_form";
			}
			
			if(file.isEmpty()) {
				System.out.println("File is empty");
				student.setProfileImage("contact.png");
			}else {
				
				// upload file to the folder
				student.setProfileImage(file.getOriginalFilename());
				File file1 = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(file1.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded");
				
			}
			
			student.setTeacher(teacher);
			teacher.getStudents().add(student);
			
			this.teacherRepository.save(teacher);
			
			System.out.println("Data : " + student);
			System.out.println("Added to the database");
			
			// message success
			
			session.setAttribute("message", new Message("Student Saved..!!", "success"));
			
		} catch (Exception e) {
			System.out.println("Error : " +e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Try Again..Wrong Details", "danger"));
		}
		
		return "student/add_student_form";
	}
	
	// display students using pagination
	
	@GetMapping("/show-students/{page}")
	public String showStudent(@PathVariable("page") Integer page,
								Model model, Principal principal) {
		model.addAttribute("title","View Students - School");
		
		String userName = principal.getName();
		Teacher teacher = this.teacherRepository.getTeacherByTeacherName(userName);
		
		Pageable pageable = PageRequest.of(page, 2);
		Page<Student> students = this.studentRepository.findStudentByTeacher(teacher.getId(),pageable);
		
		model.addAttribute("students",students);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",students.getTotalPages());
		
		return "student/show_students_form";
	}
	
	// display particular student details when hit email id (handler)
	
	@GetMapping("/{id}/student")
	public String showStudentsDetails(@PathVariable("id") Integer id,Model model,Principal principal) {
		model.addAttribute("title","Student Details - School");
		
		Optional<Student> studeOptional = this.studentRepository.findById(id);
		Student student = studeOptional.get();
		
		String userName = principal.getName();
		Teacher teacher = this.teacherRepository.getTeacherByTeacherName(userName);
		
		if(teacher.getId()==student.getTeacher().getId()) {
		model.addAttribute("students",student);
		model.addAttribute("title",student.getSname());
		}
		
		return "student/student_details";
	}
	
	
	
	
	@PostMapping("/update-student/{id}")
	public String updateStudentForm(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("title","Update Student");
		
		Student student = this.studentRepository.findById(id).get();
		model.addAttribute("students",student);
		
		return "student/update_form";
	}
	
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Student student, @RequestParam("image") MultipartFile file,
			Model model, HttpSession session, Principal principal) {
		try {
			// first we'll get old contact details...
			
			Student oldStudent = this.studentRepository.findById(student.getId()).get();
			
			// image check if old than keep or else we'll update and delete old file
			
			if(!file.isEmpty()) {
				// file rewrite and delete old profile image
				
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file2 = new File(deleteFile,oldStudent.getProfileImage());
				file2.delete();
				
				// now update new profile image
				
				File file1 = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(file1.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				student.setProfileImage(file.getOriginalFilename());		
			}else {
				student.setProfileImage(oldStudent.getProfileImage());
			}
			
			Teacher teacher = teacherRepository.getTeacherByTeacherName(principal.getName());
			
			student.setTeacher(teacher);
			
			this.studentRepository.save(student);
			
			session.setAttribute("message", new Message("Student Successfully Updated", "success"));
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Student : " + student.getSname());
		
		return "redirect:/teacher/"+student.getId()+"/student";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteStudent(@PathVariable("id") Integer id,Model model,
			HttpSession session, Principal principal) {
		
		System.out.println("Id: " + id);
		Student student = this.studentRepository.findById(id).get();
		System.out.println("Student : " + student.getId());
		
		Teacher teacher = this.teacherRepository.getTeacherByTeacherName(principal.getName());
		
		teacher.getStudents().remove(student);
		
		this.teacherRepository.save(teacher);
		System.out.println("Deleted Successfully");
		
		session.setAttribute("message", new Message("Student Deleted..", "success"));
		
		return "redirect:/teacher/show-students/0";
	}
	
	
	
	// teacher profile page...
	
	@GetMapping("/profile")
	public String teacherProfile(Model model) {
		model.addAttribute("title","Teacher Profile");
		return "student/teacher_profile";
	}
		
	// open settings tab...
		
	@GetMapping("/settings")
	public String openSettings() {
		return "student/settings";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
									@RequestParam("newPassword") String newPassword,
									Principal principal,HttpSession session) {
		
		System.out.println("Old Password : " + oldPassword);
		System.out.println("New Password : " + newPassword);
		
		String teacherName = principal.getName();
		Teacher currentTeacher = this.teacherRepository.getTeacherByTeacherName(teacherName);
		
		System.out.println(currentTeacher.getPassword());
		
		if(this.passwordEncoder.matches(oldPassword, currentTeacher.getPassword())) {

			// than change
			
			currentTeacher.setPassword(this.passwordEncoder.encode(newPassword));
			this.teacherRepository.save(currentTeacher);
			session.setAttribute("message", new Message("password change successfully", "success"));
		}else {
			// if error occurs
			session.setAttribute("message", new Message("Wrong old password", "danger"));
			return "redirect:/teacher/settings";
		}
		
		return "redirect:/teacher/dashboard";
	}
}









