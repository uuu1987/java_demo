package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 


@Entity   // "이 클래스는 DB 테이블과 연결된다"
@Getter 
@Setter 
@NoArgsConstructor 
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int kor;
    private int eng;
    private int math;

    public Student(String name, int kor, int eng, int math){
        this.name = name;
        this.kor = kor;
        this.eng = eng;
        this.math = math;
    }
    


    public double getAverage(){
        int sum = kor+eng+math;
        return (double) sum/3;
    }


    public boolean isPass() {
        return getAverage() >= 60;
    }

}