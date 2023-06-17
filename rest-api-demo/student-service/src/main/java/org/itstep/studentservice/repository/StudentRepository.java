package org.itstep.studentservice.repository;

import org.itstep.studentservice.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}

