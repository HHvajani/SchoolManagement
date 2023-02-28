package com.school.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.school.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer>{

	@Query("select t from Teacher t where t.email = :email")
	public Teacher getTeacherByTeacherName(@Param("email") String email);
	
}
