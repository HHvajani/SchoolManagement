package com.school.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.school.dao.TeacherRepository;
import com.school.entity.Teacher;

public class TeacherDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private TeacherRepository teacherRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Teacher teacher = this.teacherRepository.getTeacherByTeacherName(username);
		if(teacher == null) {
			throw new UsernameNotFoundException("Could not found teacher");
		}
		
		CustomTeacherDetails customTeacherDetails = new CustomTeacherDetails(teacher);
		return customTeacherDetails;
	}

}
