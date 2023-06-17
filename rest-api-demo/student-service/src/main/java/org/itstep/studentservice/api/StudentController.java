package org.itstep.studentservice.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itstep.studentservice.domain.Student;
import org.itstep.studentservice.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


@RequestMapping("/api/v1/student/")
@RequiredArgsConstructor
@RestController
@Slf4j
@CrossOrigin(origins = "${ui.host}", methods = {
        RequestMethod.GET,
        RequestMethod.DELETE,
        RequestMethod.PUT,
        RequestMethod.POST
})
public class StudentController {
    private final StudentRepository studentRepository;

    // findAll students
    @GetMapping
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    // Details for student
    @GetMapping("find/{id}")
    public ResponseEntity<?> findAll(@PathVariable Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student studentTmp = optionalStudent.get();
            return new ResponseEntity<>(studentTmp, HttpStatus.OK);
        }
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Student by id=%s not found".formatted(id));
        problemDetail.setTitle("Error find student");
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

    // create student
    @PostMapping(consumes = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> save(@RequestBody Student student) {
        String checkStudent = checkStudent(student);
        if (checkStudent.equals("")) {
            try {
                var savedStudent = studentRepository.save(student);
                return ResponseEntity
                        .created(URI.create("/api/v1/student/%s".formatted(savedStudent.getId())))
                        .build();
            } catch (Exception exception) {
                log.error(exception.getMessage(), exception);
                throw exception;
            }
        }
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(checkStudent);
        problemDetail.setTitle("Error save student");
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

    // update students
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Student student) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Error update student");

        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student studentTmp = optionalStudent.get();
            studentTmp.setFirstName(student.getFirstName());
            studentTmp.setLastName(student.getLastName());
            studentTmp.setBirthday(student.getBirthday());
            studentTmp.setPhone(student.getPhone());
            studentTmp.setEmail(student.getEmail());
            String checkStudent = checkStudent(studentTmp);
            if (checkStudent.equals("")) {
                studentRepository.save(studentTmp);
                return new ResponseEntity<>(studentTmp, HttpStatus.OK);
            } else {
                problemDetail.setDetail(checkStudent);
                return ResponseEntity.badRequest()
                        .body(problemDetail);
            }
        }
        problemDetail.setDetail("Student by id=%s not found".formatted(id));
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student studentTmp = optionalStudent.get();
            studentRepository.delete(studentTmp);
            return new ResponseEntity<>(studentTmp, HttpStatus.OK);
        }
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Student by id=%s not found".formatted(id));
        problemDetail.setTitle("Error update student");
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

    private String checkStudent(Student student) {
        // firstName and lastName
        String message = "";
        if (student.getFirstName().length() < 3 || student.getFirstName().length() > 50) {
            message += "Students first name don't situated between 3..50, ";
        }
        if (student.getLastName().length() < 3 || student.getLastName().length() > 50) {
            message += "Students last name don't situated between 3..50, ";
        }
        // Birthday
        Date currentDate = new Date();
        System.out.println("Текущая дата и время: " + currentDate);
        // Сравнение двух дат
        int comparisonResult = student.getBirthday().compareTo(currentDate);
//        if (comparisonResult < 0) {
//            System.out.println("date1 меньше, чем date2");
////            message += "Student has a wrong birthday, ";
//        } else
        if (comparisonResult >= 0) {
            System.out.println("date1 больше, чем date2");
            message += "Student has a wrong birthday, ";
        }
        // Number
        String patternPhone = "(\\+\\d{2})? *\\d{3} *\\d{3} *\\d{2} *\\d{2}$";
        boolean isMatchOfPhone = Pattern.matches(patternPhone, student.getPhone());
        if (!isMatchOfPhone) {
            message += "Student has a wrong phone, ";
        }
        // Email
//        String patternEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        String patternEmail = "\\w+@\\w+\\.\\w+";
        boolean isMatchOfEmail = Pattern.matches(patternEmail, student.getEmail());
        System.out.println("isMatchOfEmail = " + isMatchOfEmail);
        if (!isMatchOfEmail) {
            message += "Student has a wrong email";
        }
        System.out.println("message = " + message);
        return message;
    }
}
