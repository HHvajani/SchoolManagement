package com.school.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.school.entity.Student;
import com.school.entity.Teacher;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("from Student as s where s.teacher.id =:teacherId")
	public Page<Student> findStudentByTeacher(@Param("teacherId") int teacherId, Pageable pageable);

	
	// search query
	
	public List<Student> findBysnameContainingAndTeacher(String sname, Teacher teacher);


}
