package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; 


@Entity   // "이 클래스는 DB 테이블과 연결된다"
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
    
    public Student() {
    }

    public void setName(String name) { this.name = name; }
    public void setKor(int kor) { this.kor = kor; }
    public void setEng(int eng) { this.eng = eng; }
    public void setMath(int math) { this.math = math; }

    public double getAverage(){
        int sum = kor+eng+math;
        return (double) sum/3;
    }

    public String getName(){
        return name;
    }

    public int getKor(){
        return kor;
    }
    public int getEng(){
        return eng;
    }
    public int getMath(){
        return math;
    }

    public boolean isPass() {
        return getAverage() >= 60;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}