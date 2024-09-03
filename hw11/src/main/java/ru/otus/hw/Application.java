package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.printf("Чтобы перейти на страницу сайта открывай: %n%s%n",
				"http://localhost:8080");
		System.out.println("\nПользователь с ролью USER");
		System.out.println("Username: user");
		System.out.println("Password: user");
		System.out.println("\nПользователь с ролью ADMIN");
		System.out.println("Username: admin");
		System.out.println("Password: admin");
	}
}
