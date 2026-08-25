package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findByName(String name) {
        return studentRepository.findByName(name);
    }

    public void addStudent(Student s) {
        studentRepository.save(s);
    }

    public boolean deleteByName(String name) {
        Student student = studentRepository.findByName(name);
        if (student != null) {
            studentRepository.delete(student);
            return true;
        }
        return false;
    }

    public boolean updateByName(String name, Student updatedStudent) {
        Student existingStudent = studentRepository.findByName(name);
        if (existingStudent != null) {
            updatedStudent.setId(existingStudent.getId());
            studentRepository.save(updatedStudent);
            return true;
        }
        return false;
    }
}