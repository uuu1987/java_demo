package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
public class HelloController {

    private final StudentService studentService;
    

    public HelloController(StudentService studentService){

        this.studentService = studentService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/bye")
    public String bye() {
        return "Goodbye, World!";
    }

    @GetMapping("/one")
    public Student one() {
        return new Student("jun", 20, 85, 95);
    }
    
    
    @GetMapping("/greet")
    public String greet(@RequestParam("name") String name) {
        return "Hello, " + name + "!";
    }
    
    @GetMapping("/add")
    public String add(@RequestParam("a") int a, @RequestParam("b") int b) {
        return "합계: " + (a + b);
    }

    @GetMapping("/student")
    public ResponseEntity<Student> student(@RequestParam("name") String name) {
       
        Student found = studentService.findByName(name);

       if (found != null) {
            return ResponseEntity.ok(found);
        } else {
            return ResponseEntity.status(404).build();   // body 없이 404만 반환하는 방법
        }

    }

    @GetMapping("/students")
    public List<Student> students() {
        return studentService.findAll();
    }
    
    @PostMapping("/student")
    public ResponseEntity<String> addStudent(@RequestParam("name") String name, @RequestParam("kor") int kor, @RequestParam("eng") int eng, @RequestParam("math") int math) {
        Student newStudent2 = new Student(name, kor, eng, math);
        studentService.addStudent(newStudent2);
        return ResponseEntity.status(201).body("학생 추가 완료: " + name);
    }

    @PostMapping("/student2")
    public ResponseEntity<String> addStudent2(@RequestBody Student s) {
        studentService.addStudent(s);
        return ResponseEntity.status(201).body("학생 추가 완료: " + s.getName());
    }

    @DeleteMapping("/student")
    public ResponseEntity<String> deleteStudent(@RequestParam("name") String name){

        boolean deleted = studentService.deleteByName(name);
        if (deleted){
            return ResponseEntity.ok("삭제 완료 : " + name);
        }else{

            return ResponseEntity.status(404).body("학생을 찾을 수 없음 : " + name);
        }
    }

    @PutMapping("/student")
    public ResponseEntity<String> updateStudent(@RequestParam("name") String name, @RequestBody Student updatedInfo) {
        boolean updated = studentService.updateByName(name, updatedInfo);
        if (updated) {
            return ResponseEntity.ok("수정 전 : " + name + " / 수정 후 : " + updatedInfo.getName());
        } else {
            return ResponseEntity.status(404).body("학생을 찾을 수 없음: " + name);
        }
    }

    
}
