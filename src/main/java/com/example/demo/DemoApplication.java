package com.example.demo;

//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
/* 
	@Bean
	public CommandLineRunner initDatabase(StudentRepository st_Repository) {
		return (args) -> {
			if (st_Repository.count() == 0) {
				st_Repository.save(new Student("jun", 90, 80, 70));
				st_Repository.save(new Student("kim", 85, 75, 95));
				st_Repository.save(new Student("lee", 70, 60, 80));
				st_Repository.save(new Student("park", 95, 85, 90));
				st_Repository.save(new Student("choi", 80, 70, 75));
				System.out.println("초기 데이터 5건 삽입 완료");
			}			else{

				System.out.println("이미 데이터가 존재합니다. 초기 데이터 삽입을 건너뜁니다."+ st_Repository.count() + "건 존재");
			}
		};
	}
*/
}
